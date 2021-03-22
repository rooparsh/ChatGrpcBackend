package me.anmolverma

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import me.anmolverma.auth.JWTInterceptor
import me.anmolverma.chat.services.ChatService
import me.anmolverma.users.services.UsersService
import java.io.File

object MainGrpcServer {

    @JvmStatic
    fun main(args: Array<String>) {

        val server = NettyServerBuilder.forPort(8443)
            .useTransportSecurity(
                // reference file
                File("./src/ssl/server.crt"),
                File("./src/ssl/server.pem")
            )
            .addService(UsersService())
            .addService(ChatService())
            .intercept(JWTInterceptor())
            .build()

        server.start()
        print("GRPC server on port ${server.port} ,started!")
        server.awaitTermination()
    }

}