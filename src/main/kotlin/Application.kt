import database.dao.DatabaseFactory
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.core.context.startKoin
import io.ktor.server.plugins.contentnegotiation.*

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
    DatabaseFactory.init()
    configureRouting()

    log.info("Application started")
}