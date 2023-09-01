package service

import database.dao.RatingRepository

class MovieRatingService(
    private val ratingRepository: RatingRepository
) {
    suspend fun rate(movieName: String, rating: Int) {
        ratingRepository.addReview(movieName, rating)
        println("rated $movieName with $rating")
    }

    suspend fun deleteRating(movieName: String) {
        val movieId = ratingRepository.allReviews().firstOrNull { it.movieName == movieName }?.id
        if (movieId != null) {
            ratingRepository.deleteReview(movieId)
            println("Deleted rating for $movieName")
        } else {
            println("No rating found for $movieName")
        }
    }
}