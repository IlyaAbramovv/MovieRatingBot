package service

import database.dao.RatingRepository
import dto.PresentMovieResponse

class MovieRatingService(
    private val ratingRepository: RatingRepository
) {
    suspend fun rate(movieName: String, rating: Int, tgChatId: Long) {
        ratingRepository.addReview(movieName, rating, tgChatId)
    }

    suspend fun deleteRating(movieName: String, tgChatId: Long) {
        val movieId = ratingRepository
            .allReviews()
            .firstOrNull { it.movieName == movieName && it.tgChat.id.value == tgChatId}?.id
        if (movieId != null) {
            ratingRepository.deleteReview(movieId)
        }
    }

    suspend fun present(movieName: String, tgChatId: Long): PresentMovieResponse {
        val rating = ratingRepository.movieRatingByNameAndTgChatId(movieName, tgChatId)
        return PresentMovieResponse(rating != null, rating)
    }
}