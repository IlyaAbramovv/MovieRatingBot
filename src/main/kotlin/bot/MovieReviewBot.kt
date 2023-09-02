package bot

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommandWithArgs
import dev.inmo.tgbotapi.extensions.utils.formatting.boldMarkdown
import dev.inmo.tgbotapi.extensions.utils.formatting.boldMarkdownV2
import io.ktor.client.*
import io.ktor.client.request.*

class MovieReviewBot(
    private val bot: TelegramBot,
    private val client: HttpClient,
) {
    suspend fun start() {
        bot.buildBehaviourWithLongPolling {
            onCommand("start") {
                client.post("http://localhost:8080/telegram/register-chat/${it.chat.id.chatId}")
                sendMessage(it.chat, "Starting an application")
            }
            onCommand("stop") {
                client.delete("http://localhost:8080/telegram/${it.chat.id.chatId}")
                sendMessage(it.chat, "Stopping an application")
            }
            onCommandWithArgs("review") { message, args ->
                when {
                    args.size < 2 -> reply(message, "Отзыв должен иметь вид /review <фильм> <оценка>")
                    !args[1].all { it.isDigit() } -> reply(message, "Отзыв должен иметь вид /review <фильм> <оценка от 1 до 10>")
                    args[1].toInt() !in 1..10 -> reply(message, "Отзыв должен иметь вид /review <фильм> <оценка от 1 до 10>")
                    else -> {
                        val movieName = args[0]
                        val rating = args[1]
                        client.post("http://localhost:8080/movie/rate/$movieName") {
                            parameter("rating", rating)
                            parameter("tg-chat-id", message.chat.id.chatId)
                        }
                        sendMessage(message.chat, "Оценка ${rating.boldMarkdown()} для фильма ${movieName.boldMarkdownV2()} успешно добавлена")
                    }
                }
            }
        }
    }

}