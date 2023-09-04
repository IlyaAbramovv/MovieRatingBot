package service

import database.dao.TgChatRepository

class TgChatService(
    private val tgChatRepository: TgChatRepository
) {
    suspend fun registerChat(chatId: Long) {
        tgChatRepository.register(chatId)
    }

    suspend fun deleteChat(chatId: Long) {
        tgChatRepository.delete(chatId)
    }

    suspend fun isUserRegistered(chatId: Long): Boolean {
        return tgChatRepository.isUserRegistered(chatId)
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        return tgChatRepository.usernameNotExists(username)
    }

    suspend fun createUsername(chatId: Long, username: String) {
        tgChatRepository.createUsername(chatId, username)
    }
}