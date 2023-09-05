package service

import database.dao.TgChatRepository
import database.dao.UserRepository

class TgChatService(
    private val tgChatRepository: TgChatRepository,
    private val userRepository: UserRepository,
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
        return !isUsernameExists(username)
    }

    suspend fun createUsername(chatId: Long, username: String) {
        userRepository.addUser(username)?.apply {
            tgChatRepository.createUsername(chatId, id.value)
        }
    }

    suspend fun isUsernameExists(username: String): Boolean {
        return userRepository.isUsernameExists(username)
    }

    suspend fun subscribe(senderUsername: String, username: String) {
        val senderId = userRepository.userIdByUsername(senderUsername)!!
        val subscribeToId = userRepository.userIdByUsername(username)!!
        userRepository.subscribe(senderId, subscribeToId)
    }

    suspend fun userNameByChatId(chatId: Long): String? {
        val userId = tgChatRepository.userIdByChatId(chatId)
        return userRepository.userById(userId)?.name
    }
}