package database.dao

import database.dao.DatabaseFactory.dbQuery
import database.model.TgChat
import database.model.TgChats
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore

class TgChatRepositoryImpl : TgChatRepository {
    private fun resultRowToTgChat(row: ResultRow): TgChat = TgChat(
        id = row[TgChats.id],
    )
    override suspend fun register(id: Long): TgChat? = dbQuery {
        val insertStatement = TgChats.insertIgnore {
            it[TgChats.id] = id
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTgChat)
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        TgChats.deleteWhere {TgChats.id eq id} > 0
    }
}
