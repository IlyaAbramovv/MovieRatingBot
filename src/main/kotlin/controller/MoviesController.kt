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
        val rating = call.request.queryParameters.getOrFail<Int>("rating")
        val tgChatId = call.request.queryParameters.getOrFail<Long>("tg-chat-id")
        movieRatingService.rate(movieName, rating, tgChatId)
        call.respond("Movie rated")
    }

    suspend fun deleteRating(call: ApplicationCall) {
        val movieName = call.parameters.getOrFail("movieName")
        val tgChatId = call.request.queryParameters.getOrFail<Long>("tg-chat-id")
        movieRatingService.deleteRating(movieName, tgChatId)
        call.respond("Movie rating deleted")
    }
}