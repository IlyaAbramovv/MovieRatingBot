import bot.MovieReviewBot
import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin

@OptIn(DelicateCoroutinesApi::class)
fun Application.module() {
    log.info("Starting application")

    val ctx = startKoin {
        modules(botModule)
    }

    GlobalScope.launch {
        ctx.koin.get<MovieReviewBot>().start()
    }

    log.info("Application started")
}
