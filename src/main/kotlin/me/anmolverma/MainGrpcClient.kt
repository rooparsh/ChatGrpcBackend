package me.anmolverma

import io.grpc.CallCredentials
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext
import me.anmolverma.auth.JwtCredential
import java.io.File
import java.util.logging.Logger
import kotlin.Exception

object MainGrpcClient {
    private val logger = Logger.getLogger(AllServicesAllMethods::class.java.name)

    /**
     * Greet server. If provided, the first element of `args` is the name to use in the greeting
     * and the second is the client identifier to set in JWT
     */
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val host = "localhost"
        val port = 8443
        val clientId = "default-client"
        val credentials: CallCredentials = JwtCredential(clientId)
        val client = AllServicesAllMethods(credentials, host, port)
        try {
            client.createGroup(logger)
        }
        catch (ex:Exception){
            ex.printStackTrace()
        }
        finally {
            client.shutdown()
        }
    }

    fun buildClientSslContext(): SslContext {
        return GrpcSslContexts.forClient().trustManager(File("./src/ssl/ca.crt")).build()
    }
}