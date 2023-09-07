package database.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

class TgChat(id: EntityID<Long>, val user: EntityID<Int>?) : LongEntity(id)

object TgChats : IdTable<Long>() {
    override val id: Column<EntityID<Long>> = long("id").entityId()
    val user = reference("user", Users).nullable()
    override val primaryKey = PrimaryKey(id)
}