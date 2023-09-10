package database.dao

import database.dao.DatabaseFactory.dbQuery
import database.model.TgChat
import database.model.TgChats

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TgChatRepositoryImpl : TgChatRepository {
    private fun resultRowToTgChat(row: ResultRow): TgChat = TgChat(
        id = row[TgChats.id],
        user = row[TgChats.user],
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

    override suspend fun createUsername(chatId: Long, user: Int): Boolean = dbQuery {
        TgChats.update({ TgChats.id eq chatId }) {
            it[TgChats.user] = user
        } > 0
    }

    override suspend fun usernameNotExists(user: Int): Boolean = dbQuery {
        TgChats.select { TgChats.user eq user }.empty()
    }

    override suspend fun isUserRegistered(chatId: Long): Boolean = dbQuery {
        TgChats.select { TgChats.id eq chatId }.firstOrNull()?.let { it[TgChats.user] } != null
    }

    override suspend fun userIdByChatId(chatId: Long): EntityID<Int> = dbQuery {
        TgChats.select { TgChats.id eq chatId }.firstOrNull()?.let { it[TgChats.user] }!!
    }

    override suspend fun chatIdByUserId(userId: Int): EntityID<Long> = dbQuery {
        TgChats.select { TgChats.user eq userId }.firstOrNull()?.let { it[TgChats.id] }!!
    }
}
