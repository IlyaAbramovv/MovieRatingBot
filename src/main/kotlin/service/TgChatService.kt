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
}