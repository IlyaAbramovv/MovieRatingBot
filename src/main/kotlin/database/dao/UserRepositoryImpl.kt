package database.dao

import database.dao.DatabaseFactory.dbQuery
import database.model.User
import database.model.Users
import database.model.UsersUsers
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select

class UserRepositoryImpl : UserRepository {
    private fun resultRowToUser(row: ResultRow): User = User(row[Users.id])

    override suspend fun addUser(name: String): User? = dbQuery {
        val insertStatement = Users.insertIgnore {
            it[Users.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    override suspend fun isUsernameExists(username: String) = dbQuery {
        Users.select { Users.name eq username }.empty()
    }

    override suspend fun userIdByUsername(username: String): EntityID<Int>? = dbQuery {
        Users.select { Users.name eq username }.map { it[Users.id] }.singleOrNull()
    }

    override suspend fun userById(id: EntityID<Int>): User? = dbQuery {
        Users.select { Users.id eq id }.map(::resultRowToUser).singleOrNull()
    }

    override suspend fun subscribe(senderId: EntityID<Int>, subscribeToId: EntityID<Int>): Boolean = dbQuery {
        val insertStatement = UsersUsers.insertIgnore {
            it[username] = senderId
            it[subscribedTo] = subscribeToId
        }
        insertStatement.resultedValues?.singleOrNull()?.let { true } ?: false
    }
}