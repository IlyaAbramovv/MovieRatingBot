package database.dao

import database.model.User
import org.jetbrains.exposed.dao.id.EntityID

interface UserRepository {
    suspend fun addUser(name: String): User?
    suspend fun isUsernameExists(username: String): Boolean
    suspend fun subscribe(senderId: EntityID<Int>, subscribeToId: EntityID<Int>): Boolean
    suspend fun userIdByUsername(username: String): EntityID<Int>?
    suspend fun userById(id: EntityID<Int>): User?
}