import database.dao.DatabaseFactory
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.koin.core.context.startKoin

fun Application.module() {
    log.info("Starting application")

    startKoin {
        modules(appModule)
    }
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "404: Page Not Found", status = status)
        }
        status(HttpStatusCode.BadRequest) { call, status ->
            call.respondText(text = "400: Bad Request", status = status)
        }
    }
    DatabaseFactory.init()
    configureRouting()

    log.info("Application started")
}
