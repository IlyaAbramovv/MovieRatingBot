package controller

import io.ktor.server.application.*
import io.ktor.server.util.*
import service.BotMessageService

class BotMessageController(
    val botMessageService: BotMessageService,
) {
    suspend fun rated(call: ApplicationCall) {
        val tgChatId = call.parameters.getOrFail("tg-chat-id")
        val movieName = call.parameters.getOrFail("movie")
        val rating = call.parameters.getOrFail("rating")

        botMessageService.rated(tgChatId, movieName, rating)
    }
}