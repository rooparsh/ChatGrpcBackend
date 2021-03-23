package me.anmolverma

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import me.anmolverma.auth.JWTInterceptor
import me.anmolverma.services.chat.ChatService
import me.anmolverma.services.users.UsersService
import java.io.File
import me.anmolverma.domain.AppUser
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object MainGrpcServer {

    @JvmStatic
    fun main(args: Array<String>) {

        val appDatabase = appDatabase()
        val server = NettyServerBuilder.forPort(8443)
            .useTransportSecurity(
                // reference file
                File("./src/ssl/server.crt"),
                File("./src/ssl/server.pem")
            )
            .addService(UsersService(userCollection(appDatabase)))
            .addService(ChatService())
            .intercept(JWTInterceptor())
            .build()

        server.start()
        print("GRPC server on port ${server.port} ,started!")
        server.awaitTermination()
    }

    private fun userCollection(appDatabase: CoroutineDatabase) =
        appDatabase.getCollection<AppUser>("users")

    private fun appDatabase(): CoroutineDatabase {
        val client = KMongo.createClient(System.getenv("mongodbstring")).coroutine
        return client.getDatabase("whatsappdb")
    }

}