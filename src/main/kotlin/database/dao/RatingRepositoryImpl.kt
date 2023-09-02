package database.dao

import database.dao.DatabaseFactory.dbQuery
import database.model.Review
import database.model.Reviews
import database.model.TgChat
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class RatingRepositoryImpl : RatingRepository {
    private fun resultRowToReview(row: ResultRow): Review = Review(
        id = row[Reviews.id],
        movieName = row[Reviews.movieName],
        rating = row[Reviews.rating],
        tgChat = TgChat(row[Reviews.tgChat])
    )

    override suspend fun review(id: Int): Review? = dbQuery {
        Reviews
            .select { Reviews.id eq id }
            .map(::resultRowToReview)
            .singleOrNull()
    }

    override suspend fun allReviews(): List<Review> = dbQuery {
        Reviews.selectAll().map(::resultRowToReview)
    }

    override suspend fun addReview(movieName: String, rating: Int, tgChatId: Long): Review? = dbQuery {
        val userReviews = Reviews
            .select { (Reviews.tgChat eq tgChatId) and (Reviews.movieName eq movieName) }
            .map(::resultRowToReview)

        val presentReview = userReviews.firstOrNull { it.movieName == movieName}
        if (presentReview != null) {
            Reviews.update ({Reviews.id eq presentReview.id}) {
                it[Reviews.rating] = rating
            }
            Review (presentReview.id, presentReview.movieName, rating, presentReview.tgChat)
        } else {
            val insertStatement = Reviews.insert {
                it[Reviews.movieName] = movieName
                it[Reviews.rating] = rating
                it[tgChat] = tgChatId
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToReview)
        }
    }

    override suspend fun deleteReview(id: EntityID<Int>): Boolean = dbQuery {
        Reviews.deleteWhere { Reviews.id eq id } > 0
    }
}