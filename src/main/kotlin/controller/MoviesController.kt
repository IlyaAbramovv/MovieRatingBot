package controller

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.util.*

class MoviesController {
    suspend fun movie(call: ApplicationCall) {
        val name = call.parameters.getOrFail("movieName")
        call.respond("Movie: $name")
    }
}