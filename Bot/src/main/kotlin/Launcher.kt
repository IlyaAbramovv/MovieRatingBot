import bot.MovieReviewBot
import org.koin.core.context.startKoin

suspend fun main() {
    println("Starting bot")
    val ctx = startKoin {
        modules(botModule)
    }
    ctx.koin.get<MovieReviewBot>().start()
}
