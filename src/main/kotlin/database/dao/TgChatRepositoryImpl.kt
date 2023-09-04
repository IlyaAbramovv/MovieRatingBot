package database.dao

import database.dao.DatabaseFactory.dbQuery
import database.model.TgChat
import database.model.TgChats
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TgChatRepositoryImpl : TgChatRepository {
    private fun resultRowToTgChat(row: ResultRow): TgChat = TgChat(
        id = row[TgChats.id],
        username = row[TgChats.username],
    )

    override suspend fun register(id: Long): TgChat? = dbQuery {
        val insertStatement = TgChats.insertIgnore {
            it[TgChats.id] = id
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTgChat)
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        TgChats.deleteWhere { TgChats.id eq id } > 0
    }

    override suspend fun createUsername(chatId: Long, username: String): Boolean = dbQuery {
        TgChats.update({ TgChats.id eq chatId }) {
            it[TgChats.username] = username
        } > 0
    }

    override suspend fun usernameNotExists(username: String): Boolean = dbQuery {
        TgChats.select { TgChats.username eq username }.empty()
    }

    override suspend fun isUserRegistered(chatId: Long): Boolean = dbQuery {
        TgChats.select { TgChats.id eq chatId }.firstOrNull()?.let { it[TgChats.username] } != null
    }
}
