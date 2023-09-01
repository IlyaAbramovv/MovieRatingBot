import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.core.context.startKoin
import io.ktor.server.plugins.contentnegotiation.*

fun Application.module() {
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
    configureRouting()
    log.info("Application started")
}