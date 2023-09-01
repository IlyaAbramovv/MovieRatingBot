import controller.MoviesController
import controller.TgChatController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val moviesController: MoviesController by inject()
    val tgChatController: TgChatController by inject()

    routing {
        swaggerUI(path = "swagger", swaggerFile = "api.yaml")

        get("health") {
            call.respond(HttpStatusCode.OK, "OK")
        }
        route("movie") {
            get("{movieName}") {
                moviesController.movie(call)
            }
        }
        route("rateMovie") {
            post("{movieName}/{rating}" ) {
                moviesController.rateMovie(call)
                call.respond(HttpStatusCode.OK)
            }
            delete("{movieName}") {
                moviesController.deleteRating(call)
            }
        }

        route("telegram") {
            post("register-chat/{id}") {
                tgChatController.registerChat(call)
            }
            delete("{id}") {
                tgChatController.deleteChat(call)
            }
        }
    }
}