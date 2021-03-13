package me.anmolverma

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import me.anmolverma.users.services.UsersService

object MainGrpcServer {

    @JvmStatic
    fun main() {
        val server = NettyServerBuilder.forPort(8443)
            .addService(UsersService()).build()
        server.start()

        server.awaitTermination()
    }

}