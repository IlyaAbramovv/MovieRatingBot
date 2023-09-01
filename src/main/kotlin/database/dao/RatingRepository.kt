package database.dao

import database.model.Review

interface RatingRepository {
    suspend fun review(id: Int): Review?
    suspend fun allReviews(): List<Review>
    suspend fun addReview(movieName: String, rating: Int): Review?
    suspend fun deleteReview(id: Int): Boolean
}