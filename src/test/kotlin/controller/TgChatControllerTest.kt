package controller

import com.google.common.truth.Truth.assertThat
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import service.TgChatService

class TgChatControllerTest {
    private val tgChatService = mockk<TgChatService>()
    private val controller = TgChatController(tgChatService)

    @Test
    fun registerChat() = testApplication {
        routing {
            route("test") {
                get("{id}") {
                    controller.registerChat(call)
                }
            }
        }
        coEvery { tgChatService.registerChat(any()) } returns Unit


        val response = client.get("test/123")

        assertThat(response.status.isSuccess()).isTrue()
        coVerify(exactly = 1) { tgChatService.registerChat(123) }
    }

    @Test
    fun `registerChat fails without required params`() = testApplication {
        routing {
            route("test") {
                get("") {
                    controller.registerChat(call)
                }
                get("{id}") {
                    controller.registerChat(call)
                }
            }
        }
        install(StatusPages) {
            status(HttpStatusCode.BadRequest) { call, status ->
                call.respondText(text = "400: Bad Request", status = status)
            }
        }

        val noChatIdResponse = client.get("test")

        assertThat(noChatIdResponse.status == HttpStatusCode.BadRequest).isTrue()
        coVerify(exactly = 0) { tgChatService.registerChat(any()) }
    }

    @Test
    fun deleteChat() = testApplication {
        routing {
            route("test") {
                get("{id}") {
                    controller.deleteChat(call)
                }
            }
        }
        coEvery { tgChatService.deleteChat(any()) } returns Unit

        val response = client.get("test/123")

        assertThat(response.status.isSuccess()).isTrue()
        coVerify(exactly = 1) { tgChatService.deleteChat(123) }
    }

    @Test
    fun `deleteChat fails without required params`() = testApplication {
        routing {
            route("test") {
                get("") {
                    controller.deleteChat(call)
                }
                get("{id}") {
                    controller.deleteChat(call)
                }
            }
        }
        install(StatusPages) {
            status(HttpStatusCode.BadRequest) { call, status ->
                call.respondText(text = "400: Bad Request", status = status)
            }
        }

        val noChatIdResponse = client.get("test")

        assertThat(noChatIdResponse.status == HttpStatusCode.BadRequest).isTrue()
        coVerify(exactly = 0) { tgChatService.deleteChat(any()) }

    }
}