package me.anmolverma.services.users

import kotlinx.coroutines.Dispatchers
import me.anmolverma.domain.AppUser
import me.anmolverma.domain.toAppUser
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.eq

class UsersService(private val mongoCollection: CoroutineCollection<AppUser>) :
    UsersServiceGrpcKt.UsersServiceCoroutineImplBase(Dispatchers.IO) {
    override suspend fun registerUser(request: User): UserResponse {
        return try {
            val user: AppUser? = mongoCollection.findOne(AppUser::uname eq request.uname)
            if (user?.userId != null) {
                print("user doc found")
                UserResponse.newBuilder()
                    .setResponse(Response.newBuilder().setCode(409).setMessage("User Already exists").build())
                    .setUser(request)
                    .build()
            } else {
                mongoCollection.insertOne(request.toAppUser())
                UserResponse.newBuilder()
                    .setResponse(Response.newBuilder().setCode(201).setMessage("User created\"").build())
                    .setUser(request)
                    .build()
            }
        } catch (ex: Exception) {
            UserResponse.newBuilder()
                .setResponse(Response.newBuilder().setCode(500).setMessage("error occured ${ex.message}").build())
                .setUser(request)
                .build()

        }
    }

    override suspend fun loginUser(request: User): UserResponse {
        return super.loginUser(request)
    }

    override suspend fun findUser(request: UserSearch): Users {
        return super.findUser(request)
    }
}
