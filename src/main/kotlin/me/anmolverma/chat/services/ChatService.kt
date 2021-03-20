package me.anmolverma.chat.services

import com.google.protobuf.Empty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import me.anmolverma.chat.*

class ChatService : ChatServiceGrpcKt.ChatServiceCoroutineImplBase(Dispatchers.IO) {
    override suspend fun createGroup(request: ChatGroup): ChatGroupResponse {
        return super.createGroup(request)
    }

    override suspend fun deleteGroup(request: ChatGroup): ChatGroupResponse {
        return super.deleteGroup(request)
    }

    override suspend fun sendPresence(requests: Flow<Presence>): Empty {
        return super.sendPresence(requests)
    }
}