package database.model

import org.jetbrains.exposed.sql.*

data class TgChat(val id: Long)

object TgChats : Table() {
    val id = long("id")

    override val primaryKey = PrimaryKey(id)
}