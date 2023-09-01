import bot.MovieReviewBot
import controller.MoviesController
import controller.TgChatController
import database.dao.RatingRepository
import database.dao.RatingRepositoryImpl
import database.dao.TgChatRepository
import database.dao.TgChatRepositoryImpl
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.telegramBot
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import service.MovieRatingService
import service.TgChatService


val appModule = module {
    single<Config> {
        Config(
            System.getenv("TELEGRAM_BOT_TOKEN") ?: throw RuntimeException("TELEGRAM_BOT_TOKEN is missing")
        )
    }
    single<HttpClient> { HttpClient(CIO) }
    single<TelegramBot> { telegramBot(get<Config>().telegramBotToken) }
    singleOf(::MovieReviewBot)

    single<RatingRepository> { RatingRepositoryImpl() }
    single<TgChatRepository> { TgChatRepositoryImpl() }

    singleOf(::MovieRatingService)
    singleOf(::TgChatService)

    singleOf(::MoviesController)
    singleOf(::TgChatController)
}