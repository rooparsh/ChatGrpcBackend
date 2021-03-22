package me.anmolverma.auth

import io.grpc.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws

class JWTInterceptor : ServerInterceptor {

    private val parser: JwtParser = Jwts.parser().setSigningKey(AuthConstants.JWT_SIGNING_KEY)

    override fun <ReqT, RespT> interceptCall(
        serverCall: ServerCall<ReqT, RespT>,
        metadata: Metadata, serverCallHandler: ServerCallHandler<ReqT, RespT>?
    ): ServerCall.Listener<ReqT>? {
        val value = metadata.get(AuthConstants.AUTHORIZATION_METADATA_KEY)
        var status: Status = Status.OK
        when {
            value == null -> {
                status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing")
            }
            !value.startsWith(AuthConstants.BEARER_TYPE) -> {
                status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type")
            }
            else -> {
                // remove authorization type prefix
                val token = value.substring(AuthConstants.BEARER_TYPE.length).trim { it <= ' ' }
                val pair = getJWSClaims(token)
                val claims = pair.first
                pair.first?.let {
                    // set client id into current context
                    val ctx: Context = Context.current()
                        .withValue(AuthConstants.CLIENT_ID_CONTEXT_KEY, claims?.body?.subject)
                    return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler)
                }
                pair.second?.let {
                    status = it
                }
            }
        }
        serverCall.close(status, Metadata())
        return object : ServerCall.Listener<ReqT>() {
            // noop
        }
    }

    private fun getJWSClaims(
        token: String,
    ): Pair<Jws<Claims>?, Status?> {
        return try {
            // verify token signature and parse claims
            Pair(parser.parseClaimsJws(token), null)
        } catch (e: JwtException) {
            Pair(null, Status.UNAUTHENTICATED.withDescription(e.message).withCause(e))
        }
    }

}
