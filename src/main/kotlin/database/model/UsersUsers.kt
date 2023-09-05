package database.model

import org.jetbrains.exposed.sql.Table

object UsersUsers : Table() {
    val username = reference("username", Users)
    val subscribedTo = reference("subscribed-to", Users)
    override val primaryKey = PrimaryKey(
        username, subscribedTo, name = "PK_UserUser_username_subscribed-to"
    )
}