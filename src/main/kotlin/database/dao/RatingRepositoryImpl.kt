package database.dao

import database.dao.DatabaseFactory.dbQuery
import database.model.Review
import database.model.Reviews
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class RatingRepositoryImpl : RatingRepository {
    private fun resultRowToReview(row: ResultRow): Review = Review(
        id = row[Reviews.id],
        movieName = row[Reviews.movieName],
        rating = row[Reviews.rating]
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

    override suspend fun addReview(movieName: String, rating: Int): Review? = dbQuery {
        val insertStatement = Reviews.insert {
            it[Reviews.movieName] = movieName
            it[Reviews.rating] = rating
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToReview)
    }

    override suspend fun deleteReview(id: Int): Boolean  = dbQuery {
        Reviews.deleteWhere { Reviews.id eq id } > 0
    }
}