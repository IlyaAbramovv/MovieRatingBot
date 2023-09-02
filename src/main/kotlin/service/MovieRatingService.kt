package service

import database.dao.RatingRepository

class MovieRatingService(
    private val ratingRepository: RatingRepository
) {
    suspend fun rate(movieName: String, rating: Int, tgChatId: Long) {
        ratingRepository.addReview(movieName, rating, tgChatId)
        println("rated $movieName with $rating")
    }

    suspend fun deleteRating(movieName: String, tgChatId: Long) {
        val movieId = ratingRepository
            .allReviews()
            .firstOrNull { it.movieName == movieName && it.tgChat.id.value == tgChatId}?.id
        if (movieId != null) {
            ratingRepository.deleteReview(movieId)
            println("Deleted rating for $movieName")
        } else {
            println("No rating found for $movieName")
        }
    }
}