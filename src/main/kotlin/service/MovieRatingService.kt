package service

import database.RatingRepository

class MovieRatingService(
    private val ratingRepository: RatingRepository
) {
    fun rate(movieName: String, rating: Int) {
        ratingRepository.addRating(movieName, rating)
        println("rated $movieName with $rating")
    }

    fun deleteRating(movieName: String) {
        ratingRepository.removeRating(movieName)
        println("deleted rating for $movieName")
    }
}