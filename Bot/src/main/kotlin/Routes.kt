import controller.BotMessageController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val botMessageController: BotMessageController by inject()

    routing {
//        swaggerUI(path = "swagger", swaggerFile = "api.yaml")

        get("health") {
            call.respond(HttpStatusCode.OK, "OK")
        }

        route("send-message") {
            post("rated/{movie}/{tg-chat-id}/{rating}") {
                botMessageController.rated(call)
            }
        }
    }
}