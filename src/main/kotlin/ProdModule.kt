import bot.MovieReviewBot
import controller.MoviesController
import controller.TgChatController
import database.dao.*
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.telegramBot
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import service.MovieRatingService
import service.TgChatService


val appModule = module {
    single<Config> { Config(getEnvSafe("TELEGRAM_BOT_TOKEN")) }
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
    }
    single<TelegramBot> { telegramBot(get<Config>().telegramBotToken) }

    single<RatingRepository> { RatingRepositoryImpl() }
    single<TgChatRepository> { TgChatRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }

    singleOf(::MovieRatingService)
    singleOf(::TgChatService)

    singleOf(::MoviesController)
    singleOf(::TgChatController)
    singleOf(::MovieReviewBot)
}

fun getEnvSafe(name: String): String {
    return System.getenv(name) ?: throw RuntimeException("$name env variable is missing")
}