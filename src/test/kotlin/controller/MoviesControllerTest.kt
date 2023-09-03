package controller

import com.google.common.truth.Truth.assertThat
import dto.PresentMovieResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import service.MovieRatingService

class MoviesControllerTest {
    private val movieRatingService = mockk<MovieRatingService>()
    private val controller = MoviesController(movieRatingService)
    private val presentMovieResponse = PresentMovieResponse(true, 10)
    private val serverContentNegotiation = io.ktor.server.plugins.contentnegotiation.ContentNegotiation
    private val clientContentNegotiation = io.ktor.client.plugins.contentnegotiation.ContentNegotiation

    @Test
    fun rateMovie() = testApplication {
        routing {
            route("test") {
                get("{movieName}") {
                    controller.rateMovie(call)
                }
            }
        }
        coEvery { movieRatingService.rate(any(), any(), any()) } returns Unit

        val response = client.get("test/movie") {
            parameter("rating", 10)
            parameter("tg-chat-id", 1L)
        }

        assertThat(response.status.isSuccess()).isTrue()
        coVerify(exactly = 1) { movieRatingService.rate(movieName = "movie", rating = 10, tgChatId = 1L) }
    }

    @Test
    fun `rateMovie fails without required params`() = testApplication {
        routing {
            route("test") {
                get("") {
                    controller.rateMovie(call)
                }
                get("{movieName}") {
                    controller.rateMovie(call)
                }
            }
        }
        install(StatusPages) {
            status(HttpStatusCode.BadRequest) { call, status ->
                call.respondText(text = "400: Bad Request", status = status)
            }
        }

        val noMovieNameResponse = client.get("test") {
            parameter("rating", 10)
            parameter("tg-chat-id", 1L)
        }
        val noRatingResponse = client.get("test/movie") {
            parameter("tg-chat-id", 1L)
        }
        val noTgChatIdResponse = client.get("test/movie") {
            parameter("rating", 10)
        }

        assertThat(noMovieNameResponse.status == HttpStatusCode.BadRequest).isTrue()
        assertThat(noRatingResponse.status == HttpStatusCode.BadRequest).isTrue()
        assertThat(noTgChatIdResponse.status == HttpStatusCode.BadRequest).isTrue()
        coVerify(exactly = 0) { movieRatingService.rate(any(), any(), any()) }

    }

    @Test
    fun deleteRating() = testApplication {
        routing {
            route("test") {
                get("{movieName}") {
                    controller.deleteRating(call)
                }
            }
        }
        coEvery { movieRatingService.deleteRating(any(), any()) } returns Unit

        val response = client.get("test/movie") {
            parameter("tg-chat-id", 1L)
        }

        assertThat(response.status.isSuccess()).isTrue()
        coVerify(exactly = 1) { movieRatingService.deleteRating(movieName = "movie", tgChatId = 1L) }
    }

    @Test
    fun `deleteRating fails without required params`() = testApplication {
        routing {
            route("test") {
                get("") {
                    controller.deleteRating(call)
                }
                get("{movieName}") {
                    controller.deleteRating(call)
                }
            }
        }
        install(StatusPages) {
            status(HttpStatusCode.BadRequest) { call, status ->
                call.respondText(text = "400: Bad Request", status = status)
            }
        }

        val noMovieNameResponse = client.get("test") {
            parameter("tg-chat-id", 1L)
        }
        val noTgChatIdResponse = client.get("test/movie")

        assertThat(noMovieNameResponse.status == HttpStatusCode.BadRequest).isTrue()
        assertThat(noTgChatIdResponse.status == HttpStatusCode.BadRequest).isTrue()
        coVerify(exactly = 0) { movieRatingService.deleteRating(any(), any()) }

    }

    @Test
    fun present() = testApplication {
        val client = createClient {
            install(clientContentNegotiation) {
                json()
            }
        }
        install(serverContentNegotiation) {
            json()
        }
        routing {
            route("test") {
                get("{movieName}") {
                    controller.present(call)
                }
            }
        }
        coEvery { movieRatingService.present(any(), any()) } returns presentMovieResponse

        val response = client.get("test/movie") {
            parameter("tg-chat-id", 1L)
        }

        assertThat(response.status.isSuccess()).isTrue()
        assertThat(response.body<PresentMovieResponse>()).isEqualTo(presentMovieResponse)
        coVerify(exactly = 1) { movieRatingService.present(movieName = "movie", tgChatId = 1L) }

    }

    @Test
    fun `present fails without required params`() = testApplication {
        routing {
            route("test") {
                get("") {
                    controller.present(call)
                }
                get("{movieName}") {
                    controller.present(call)
                }
            }
        }
        install(StatusPages) {
            status(HttpStatusCode.BadRequest) { call, status ->
                call.respondText(text = "400: Bad Request", status = status)
            }
        }

        val noMovieNameResponse = client.get("test") {
            parameter("tg-chat-id", 1L)
        }
        val noTgChatIdResponse = client.get("test/movie")

        assertThat(noMovieNameResponse.status == HttpStatusCode.BadRequest).isTrue()
        assertThat(noTgChatIdResponse.status == HttpStatusCode.BadRequest).isTrue()
        coVerify(exactly = 0) { movieRatingService.rate(any(), any(), any()) }

    }
}