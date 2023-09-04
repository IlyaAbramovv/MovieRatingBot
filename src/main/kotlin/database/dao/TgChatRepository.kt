package database.dao

import database.model.TgChat

interface TgChatRepository {
    suspend fun register(id: Long): TgChat?
    suspend fun delete(id: Long): Boolean
    suspend fun createUsername(chatId: Long, username: String): Boolean
    suspend fun usernameNotExists(username: String): Boolean
    suspend fun isUserRegistered(chatId: Long): Boolean
}