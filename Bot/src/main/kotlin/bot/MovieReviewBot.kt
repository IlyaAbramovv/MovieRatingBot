package bot

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommandWithArgs
import dev.inmo.tgbotapi.extensions.utils.formatting.boldMarkdownV2
import dev.inmo.tgbotapi.extensions.utils.formatting.italicMarkdownV2
import dev.inmo.tgbotapi.types.message.MarkdownV2
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import dto.PresentMovieResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class MovieReviewBot(
    private val bot: TelegramBot,
    private val client: HttpClient,
) {
    suspend fun start() {
        bot.buildBehaviourWithLongPolling {
            onCommand("start") {
                handleStart(it)
            }
            onCommand("stop") {
                handleStop(it)
            }
            onCommandWithArgs("rate") { message, args ->
                handleRate(message, args)
            }
            onCommandWithArgs("register") { message, args ->
                handleRegister(message, args)
            }
            onCommandWithArgs("subscribe") { message, args ->
                handleSubscribe(args, message)
            }
        }.join()
    }

    private suspend fun BehaviourContext.handleSubscribe(
        args: Array<String>,
        message: CommonMessage<TextContent>
    ) {
        val subscribeTo = args[0]
        val request = client.post("http://localhost:8080/telegram/subscribe/${message.chat.id.chatId}/${subscribeTo}")
        when (request.status) {
            HttpStatusCode.OK -> sendMessage(
                message.chat,
                "Успешно подписаны на пользователя ${subscribeTo.boldMarkdownV2()}",
                parseMode = MarkdownV2
            )

            HttpStatusCode.Unauthorized -> reply(
                message,
                "Сначала нужно зарегистрироваться с помощью /register ${"имя_пользователя".italicMarkdownV2()}",
                parseMode = MarkdownV2
            )

            HttpStatusCode.NotFound -> reply(message, "Пользователь с таким именем не найден")
        }
    }

    private suspend fun BehaviourContext.handleRegister(message: CommonMessage<TextContent>, args: Array<String>) {
        when {
            args.isEmpty() -> reply(message, "Укажите имя под которым хотите зарегистрироваться")
            else -> {
                val request = client.post("http://localhost:8080/telegram/create-username/${message.chat.id.chatId}") {
                    parameter("username", args[0])
                }
                when (request.status) {
                    HttpStatusCode.OK -> sendMessage(message.chat, "Успешно зарегистрирован")
                    HttpStatusCode.Conflict -> reply(message, "Пользователь с таким именем уже существует")
                    HttpStatusCode.ExpectationFailed -> reply(message, "Вы уже зарегистрированы")
                    else -> reply(message, "Что-то пошло не так, попробуйте позже")
                }
            }
        }
    }

    private suspend fun BehaviourContext.handleRate(
        message: CommonMessage<TextContent>,
        args: Array<String>
    ) {
        val notEnoughArgumentsMsg = "Отзыв должен иметь вид: /review ${"фильм оценка".italicMarkdownV2()}"
        val incorrectRatingMsg =
            "Отзыв должен иметь вид /review ${"фильм оценка".italicMarkdownV2()} \\(от 1 до 10\\)"

        when {
            args.size < 2 -> reply(message, notEnoughArgumentsMsg, parseMode = MarkdownV2)
            !args[1].all { it.isDigit() } -> reply(message, incorrectRatingMsg, parseMode = MarkdownV2)
            args[1].toInt() !in 1..10 -> reply(message, incorrectRatingMsg, parseMode = MarkdownV2)
            else -> getPreviousRatingIfExistsAndRateAgain(args, message)
        }
    }

    private suspend fun BehaviourContext.getPreviousRatingIfExistsAndRateAgain(
        args: Array<String>,
        message: CommonMessage<TextContent>
    ) {
        fun successfullyAddedRating(rating: String, movieName: String) =
            "Оценка ${rating.boldMarkdownV2()} для фильма ${movieName.boldMarkdownV2()} успешно добавлена"

        fun changeRatingMsg(movieName: String, present: PresentMovieResponse, rating: String) =
            "Оценка для фильма ${movieName.boldMarkdownV2()} измена c ${
                present.rating!!.toString().boldMarkdownV2()
            } на ${rating.boldMarkdownV2()}"

        val movieName = args[0]
        val rating = args[1]
        val response = client.get("http://localhost:8080/movie/present/$movieName") {
            parameter("tg-chat-id", message.chat.id.chatId)
        }
        val present: PresentMovieResponse = response.body()

        client.post("http://localhost:8080/movie/rate/$movieName") {
            parameter("rating", rating)
            parameter("tg-chat-id", message.chat.id.chatId)
        }
        if (present.isPresent) {
            sendMessage(
                message.chat,
                changeRatingMsg(movieName, present, rating),
                parseMode = MarkdownV2
            )
        } else {
            sendMessage(
                message.chat,
                successfullyAddedRating(rating, movieName),
                parseMode = MarkdownV2
            )
        }
    }

    private suspend fun BehaviourContext.handleStop(it: CommonMessage<TextContent>) {
        client.delete("http://localhost:8080/telegram/${it.chat.id.chatId}")
        sendMessage(it.chat, "Бот прекратил действие. Все обзоры, связанные с этим чатом, удалены")
    }

    private suspend fun BehaviourContext.handleStart(it: CommonMessage<TextContent>) {
        client.post("http://localhost:8080/telegram/register-chat/${it.chat.id.chatId}")
        sendMessage(it.chat, "Starting an application")
    }
}
