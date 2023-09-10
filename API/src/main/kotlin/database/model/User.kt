package database.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    val name: String
        get() = transaction {
            Users.select { Users.id eq super.id }
                .single()[Users.name]
        }
    val subscribedTo: List<User>
        get() = transaction {
            UsersUsers
                .select { UsersUsers.username eq super.id }
                .map { User(it[UsersUsers.subscribedTo]) }
        }

    val subscribers: List<User>
        get() = transaction {
            UsersUsers
                .select { UsersUsers.subscribedTo eq super.id }
                .map { User(it[UsersUsers.username]) }
        }
}

object Users : IntIdTable() {
    val name = varchar("name", 128)
}