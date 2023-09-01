package database

interface RatingRepository {
    fun addRating(movieName: String, rating: Int)

    fun removeRating(movieName: String)
}