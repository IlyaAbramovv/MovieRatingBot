package controller

import io.ktor.server.application.*
import io.ktor.server.util.*
import service.TgChatService

class TgChatController(
    private val tgChatService: TgChatService
) {
    suspend fun registerChat(call: ApplicationCall) {
        val chatId = call.parameters.getOrFail<Long>("id")
        tgChatService.registerChat(chatId)
    }
    suspend fun deleteChat(call: ApplicationCall) {
        val chatId = call.parameters.getOrFail<Long>("id")
        tgChatService.deleteChat(chatId)

    }
}