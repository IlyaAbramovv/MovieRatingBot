package service

class MovieRatingService {
    fun rate(movieName: String, rating: Int) {
        println("rated $movieName with $rating")
    }

    fun deleteRating(movieName: String) {
        println("deleted rating for $movieName")
    }
}