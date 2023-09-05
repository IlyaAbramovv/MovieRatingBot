package controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import service.TgChatService

class TgChatController(
    private val tgChatService: TgChatService
) {
    suspend fun registerChat(call: ApplicationCall) {
        val chatId = call.parameters.getOrFail<Long>("id")
        tgChatService.registerChat(chatId)
        call.respond(HttpStatusCode.OK, "Chat successfully registered")
    }

    suspend fun deleteChat(call: ApplicationCall) {
        val chatId = call.parameters.getOrFail<Long>("id")
        tgChatService.deleteChat(chatId)
        call.respond(HttpStatusCode.OK, "Chat successfully deleted")
    }

    suspend fun createUsername(call: ApplicationCall) {
        val chatId = call.parameters.getOrFail<Long>("id")
        val username = call.request.queryParameters.getOrFail<String>("username")

        when {
            tgChatService.isUserRegistered(chatId) -> call.respond(
                HttpStatusCode.ExpectationFailed,
                "Already registered"
            )

            !tgChatService.isUsernameAvailable(username) -> call.respond(
                HttpStatusCode.Conflict,
                "Username already exists"
            )

            else -> {
                call.respond(HttpStatusCode.OK, "Created username successfully")
                tgChatService.createUsername(chatId, username)
            }
        }
    }

    suspend fun subscribe(call: ApplicationCall) {
        val chatId = call.parameters.getOrFail<Long>("id")
        val username = call.parameters.getOrFail<String>("username")
        val senderUsername = tgChatService.userNameByChatId(chatId)

        when {
            senderUsername == null -> call.respond(HttpStatusCode.Unauthorized, "Sender user is not registered")
            username == senderUsername -> call.respond(HttpStatusCode.BadRequest, "Can't subscribe to yourself")
            !tgChatService.isUsernameExists(username) -> call.respond(HttpStatusCode.NotFound, "Username not found")
            else -> {
                tgChatService.subscribe(senderUsername, username)
                call.respond(HttpStatusCode.OK, "Subscribed successfully")
            }
        }
    }
}
