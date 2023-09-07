package service

import database.dao.TgChatRepository
import database.dao.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TgChatServiceTest {
    private val tgChatRepository = mockk<TgChatRepository>()
    private val userRepository = mockk<UserRepository>()
    private val service = TgChatService(tgChatRepository, userRepository)

    @Test
    fun registerChat() = runTest {
        coEvery { tgChatRepository.register(any()) } returns  null

        service.registerChat(1)

        coVerify(exactly = 1) { tgChatRepository.register(1) }
    }

    @Test
    fun deleteChat() = runTest {
        coEvery { tgChatRepository.delete(any()) } returns true

        service.deleteChat(1)

        coVerify(exactly = 1) { tgChatRepository.delete(1) }
    }
}