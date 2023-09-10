package service

import com.google.common.truth.Truth.assertThat
import database.dao.RatingRepository
import database.model.Review
import database.model.Reviews
import database.model.TgChats
import dto.PresentMovieResponse
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.dao.id.EntityID
import org.junit.jupiter.api.Test


class MovieRatingServiceTest {
    private val ratingRepository = mockk<RatingRepository>()
    private val httpClient: HttpClient
    private val service: MovieRatingService
    private val review: Review = Review(
        EntityID(1, Reviews),
        "movie",
        10,
        EntityID(1L, TgChats),
    )

    init {
        val mockEngine = MockEngine {
            respond(
                content = it.url.toString(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        httpClient = HttpClient(mockEngine)
        service = MovieRatingService(ratingRepository, httpClient)
    }


    @Test
    fun rate() = runTest {
        coEvery { ratingRepository.addReview(any(), any(), any()) } returns null

        httpClient.engine
        service.rate(movieName = "", rating = 1, tgChatId = 1L)
        coVerify(exactly = 1) { ratingRepository.addReview("", 1, 1L) }
    }

    @Test
    fun `delete rating if exists`() = runTest {
        coEvery { ratingRepository.allReviews() } returns listOf(review)
        coEvery { ratingRepository.deleteReview(any()) } returns true

        service.deleteRating("movie", 1L)

        coVerify(exactly = 1) {
            ratingRepository.deleteReview(review.id)
        }
    }

    @Test
    fun `delete rating if not exists`() = runTest {
        coEvery { ratingRepository.allReviews() } returns listOf()
        coEvery { ratingRepository.deleteReview(any()) } returns false

        service.deleteRating("movie", 1L)

        coVerify(exactly = 0) {
            ratingRepository.deleteReview(review.id)
        }
    }

    @Test
    fun `present for present review`() = runTest {
        val presentRating = 10
        coEvery { ratingRepository.movieRatingByNameAndTgChatId(any(), any()) } returns presentRating

        assertThat(service.present("movie", 1L)).isEqualTo(PresentMovieResponse(true, 10))
    }

    @Test
    fun `present for non present review`() = runTest {
        coEvery { ratingRepository.movieRatingByNameAndTgChatId(any(), any()) } returns null

        assertThat(service.present("movie", 1L)).isEqualTo(PresentMovieResponse(false, null))
    }
}