package me.anmolverma.auth

import io.grpc.CallCredentials
import io.grpc.Metadata
import io.grpc.Status
import java.util.concurrent.Executor
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * gRCP uses terms Metadata and Context keys. Metadata is set of key-value pairs that are
 * transported between client and server, et vice versa. So it's like HTTP headers.
 * On other hand, Context key is set of values that are available during request processing.
 * By default, a Storage implementation based on ThreadLocal is used.
 * Thanks to this, you can just call get() method on a Context key and you immediately get the value
 * because it read the value from Context.current().
 * So when implementing interceptors, you must be sure that you read Context values from the right thread.
 * It's actually no issue for us because: The right thread is automatically handled by gRPC-core when usingCallCredentials.
 * So you can call applier.apply() method on any thread. Our ServerInterceptor implementation handles it correctly.
 */

/**
 * CallCredentials implementation, which carries the JWT value that will be propagated to the
 * server in the request metadata with the "Authorization" key and the "Bearer" prefix.
 */
class JwtCredential(private val subject: String) : CallCredentials() {
    override fun applyRequestMetadata(
        requestInfo: RequestInfo, executor: Executor,
        metadataApplier: MetadataApplier
    ) {
        // Make a JWT compact serialized string.
        //set one day as expiration time
        val jwt: String = Jwts.builder()
            .setSubject(subject)
            .setExpiration(Date.from(Instant.now().plusMillis(TimeUnit.DAYS.toMillis(1))))
            .signWith(SignatureAlgorithm.HS256, AuthConstants.JWT_SIGNING_KEY)
            .compact()
        executor.execute {
            try {
                val headers = Metadata()
                headers.put(
                    AuthConstants.AUTHORIZATION_METADATA_KEY,
                    java.lang.String.format("%s %s", AuthConstants.BEARER_TYPE, jwt)
                )
                metadataApplier.apply(headers)
            } catch (e: Throwable) {
                metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e))
            }
        }
    }

    override fun thisUsesUnstableApi() {
        // noop
    }
}