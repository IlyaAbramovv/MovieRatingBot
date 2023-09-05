package database.dao

import database.model.TgChat
import org.jetbrains.exposed.dao.id.EntityID

interface TgChatRepository {
    suspend fun register(id: Long): TgChat?
    suspend fun delete(id: Long): Boolean
    suspend fun createUsername(chatId: Long, user: Int): Boolean
    suspend fun usernameNotExists(user: Int): Boolean
    suspend fun isUserRegistered(chatId: Long): Boolean
    suspend fun userIdByChatId(chatId: Long): EntityID<Int>
}