package service

import database.dao.RatingRepository
import dto.PresentMovieResponse
import io.ktor.client.*
import io.ktor.client.request.*

class MovieRatingService(
    private val ratingRepository: RatingRepository,
    private val httpClient: HttpClient,
) {
    suspend fun rate(movieName: String, rating: Int, tgChatId: Long) {
        ratingRepository.addReview(movieName, rating, tgChatId)
        httpClient.post("http://localhost:8081/send-message/rated/$movieName/$tgChatId/$rating")
    }

    suspend fun deleteRating(movieName: String, tgChatId: Long) {
        val movieId = ratingRepository
            .allReviews()
            .firstOrNull { it.movieName == movieName && it.tgChat.value == tgChatId }?.id
        if (movieId != null) {
            ratingRepository.deleteReview(movieId)
        }
    }

    suspend fun present(movieName: String, tgChatId: Long): PresentMovieResponse {
        val rating = ratingRepository.movieRatingByNameAndTgChatId(movieName, tgChatId)
        return PresentMovieResponse(rating != null, rating)
    }
}