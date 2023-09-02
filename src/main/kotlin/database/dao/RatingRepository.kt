package database.dao

import database.model.Review
import org.jetbrains.exposed.dao.id.EntityID

interface RatingRepository {
    suspend fun review(id: Int): Review?
    suspend fun allReviews(): List<Review>
    suspend fun addReview(movieName: String, rating: Int, tgChatId: Long): Review?
    suspend fun deleteReview(id: EntityID<Int>): Boolean
}