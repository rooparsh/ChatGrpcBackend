package me.anmolverma.chat.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import me.anmolverma.chat.*
import me.anmolverma.users.Response

class ChatService : ChatServiceGrpcKt.ChatServiceCoroutineImplBase(Dispatchers.IO) {
    override suspend fun createGroup(request: ChatGroup): ChatGroupResponse {
        return ChatGroupResponse.getDefaultInstance()
    }

    override suspend fun deleteGroup(request: ChatGroup): ChatGroupResponse {
        return super.deleteGroup(request)
    }

    override suspend fun fetchGroup(request: FindChatGroup): ChatGroupResponse {
        return super.fetchGroup(request)
    }

    override suspend fun sendMessage(requests: Flow<ChatMessage>): Response {
        return super.sendMessage(requests)
    }

    override suspend fun sendPresence(requests: Flow<Presence>): Response {
        return super.sendPresence(requests)
    }
}