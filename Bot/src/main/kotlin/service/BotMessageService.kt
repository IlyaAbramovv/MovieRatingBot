package service

import bot.MovieReviewBot
import dev.inmo.tgbotapi.extensions.utils.formatting.boldMarkdownV2
import dto.SubscribersResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class BotMessageService(
    private val httpClient: HttpClient,
    private val bot: MovieReviewBot,
) {
    suspend fun rated(tgChatId: String, movieName: String, rating: String) {
        val subscribersResponse = httpClient.get("http://localhost:8080/telegram/subscribers/$tgChatId")

        if (subscribersResponse.status.isSuccess()) {
            val subscribers = subscribersResponse.body<SubscribersResponse>()
            val message =
                "${subscribers.username.boldMarkdownV2()} оценил фильм ${movieName.boldMarkdownV2()} на ${rating.boldMarkdownV2()}"

            subscribers.subscribersChats.forEach {
                bot.sendMessage(it, message)
            }
        }
    }
}