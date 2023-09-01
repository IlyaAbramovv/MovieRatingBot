package database.dao

import database.model.TgChat

interface TgChatRepository {
    suspend fun register(id: Long): TgChat?
    suspend fun delete(id: Long): Boolean
}