package database

class RatingRepositoryImpl : RatingRepository {
    override fun addRating(movieName: String, rating: Int) {
        println("Adding rating $rating for movie $movieName")
    }

    override fun removeRating(movieName: String) {
        println("Removing rating for movie $movieName")
    }
}