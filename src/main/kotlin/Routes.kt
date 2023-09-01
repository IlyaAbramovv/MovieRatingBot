import controller.MoviesController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val moviesController: MoviesController by inject()

    routing {
        get("health") {
            call.respond(HttpStatusCode.OK, "OK")
        }
        route("api") {
            get("hello") {
                call.respond("Hello")
            }
        }
        route("movie") {
            get("{movieName}") {
                moviesController.movie(call)
            }
        }
    }
}