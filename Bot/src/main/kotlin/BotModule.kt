import bot.MovieReviewBot
import controller.BotMessageController
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.telegramBot
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import service.BotMessageService


val botModule = module {
    single<Config> { Config(getEnvSafe("TELEGRAM_BOT_TOKEN")) }
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
    }
    single<TelegramBot> { telegramBot(get<Config>().telegramBotToken) }

    singleOf(::MovieReviewBot)

    singleOf(::BotMessageService)

    singleOf(::BotMessageController)

}

fun getEnvSafe(name: String): String {
    return System.getenv(name) ?: throw RuntimeException("$name env variable is missing")
}
