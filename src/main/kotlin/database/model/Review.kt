package database.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Review(id: EntityID<Int>, val movieName: String, val rating: Int, val tgChat: TgChat): IntEntity(id)

object Reviews : IntIdTable() {
    val movieName = varchar("movie_name", 128)
    val rating = integer("rating")
    val tgChat = reference("tg-chat-id", TgChats)
}