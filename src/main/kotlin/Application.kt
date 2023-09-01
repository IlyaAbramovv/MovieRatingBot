import io.ktor.server.application.*
import org.koin.core.context.startKoin

fun Application.module() {
    startKoin {
        modules(appModule)
    }
    configureRouting()
}