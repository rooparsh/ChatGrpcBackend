package me.anmolverma.chat

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoDatabase

interface ChatRepository {
    fun createGroup(request: ChatGroup)
}

class ChatRepositoryImpl : ChatRepository {
    private val mongoClient by lazy {
        val uri = MongoClientURI(System.getenv("mongodbstring"))
        MongoClient(uri)
    }
    private val database: MongoDatabase by lazy {
        mongoClient.getDatabase("test")
    }

    override fun createGroup(request: ChatGroup) {
        print(database)
    }
}