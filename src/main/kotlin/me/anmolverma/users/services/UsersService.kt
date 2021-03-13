package me.anmolverma.users.services

import kotlinx.coroutines.Dispatchers
import me.anmolverma.users.*

class UsersService : UsersServiceGrpcKt.UsersServiceCoroutineImplBase(Dispatchers.IO) {
    override suspend fun registerUser(request: User): Response {
        return super.registerUser(request)
    }

    override suspend fun loginUser(request: User): Response {
        return super.loginUser(request)
    }

    override suspend fun findUser(request: UserSearch): Users {
        return super.findUser(request)
    }
}