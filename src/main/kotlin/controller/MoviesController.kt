package controller

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import service.MovieRatingService

class MoviesController(
    private val movieRatingService: MovieRatingService
) {

    suspend fun movie(call: ApplicationCall) {
        val name = call.parameters.getOrFail("movieName")
        call.respond("Movie: $name")
    }

    suspend fun rateMovie(call: ApplicationCall ) {
        val movieName = call.parameters.getOrFail("movieName")
        val rating = call.parameters.getOrFail("rating").toInt()
        movieRatingService.rate(movieName, rating)
        call.respond("Movie rated")
    }

    suspend fun deleteRating(call: ApplicationCall) {
        val movieName = call.parameters.getOrFail("movieName")
        movieRatingService.deleteRating(movieName)
        call.respond("Movie rating deleted")
    }
}