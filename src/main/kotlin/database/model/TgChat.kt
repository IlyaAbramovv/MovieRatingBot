package database.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

class TgChat(id: EntityID<Long>, val username: String?) : LongEntity(id)

object TgChats : IdTable<Long>() {
    override val id: Column<EntityID<Long>> = long("id").entityId()
    val username = varchar("username", 128).nullable()
    override val primaryKey = PrimaryKey(id)
}