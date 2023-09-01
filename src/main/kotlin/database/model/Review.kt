package database.model

import org.jetbrains.exposed.sql.*

data class Review(val id: Int, val movieName: String, val rating: Int)

object Reviews : Table() {
    val id = integer("id").autoIncrement()
    val movieName = varchar("movie_name", 128)
    val rating = integer("rating")

    override val primaryKey = PrimaryKey(id)
}