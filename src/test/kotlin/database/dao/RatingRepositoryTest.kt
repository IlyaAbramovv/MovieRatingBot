package database.dao

import IntegrationEnvironment
import com.google.common.truth.Truth.assertThat
import database.dao.DatabaseFactory.dbQuery
import database.model.Review
import database.model.Reviews
import database.model.TgChat
import database.model.TgChats
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class RatingRepositoryTest : IntegrationEnvironment() {
    private val repository: RatingRepository = RatingRepositoryImpl()

    @BeforeTest
    fun createTable() {
        transaction {
            create(Reviews, TgChats)
        }
    }

    @AfterTest
    fun cleanUp() {
        transaction {
            drop(Reviews, TgChats)
        }
    }

    @Test
    fun review() = runTest {
        val tgChatId = dbQuery {
            TgChats.insertAndGetId { it[id] = 1 }
        }
        val reviewId = dbQuery {
            Reviews.insertAndGetId {
                it[movieName] = "movieName"
                it[rating] = 5
                it[tgChat] = tgChatId
            }
        }

        val actualReview = repository.review(reviewId.value)

        assertThat(actualReview!!.id).isEqualTo(reviewId)
    }

    @Test
    fun allReviews() = runTest {
        val tgChat1Id = dbQuery {
            TgChats.insertAndGetId { it[id] = 1 }
        }
        val tgChat2Id = dbQuery {
            TgChats.insertAndGetId { it[id] = 2 }
        }
        val review1Id = dbQuery {
            Reviews.insertAndGetId {
                it[movieName] = "movie1"
                it[rating] = 5
                it[tgChat] = tgChat1Id
            }
        }
        val review2Id = dbQuery {
            Reviews.insertAndGetId {
                it[movieName] = "movie2"
                it[rating] = 10
                it[tgChat] = tgChat2Id
            }
        }

        val expectedReviews = setOf(review1Id, review2Id)
        val actualReviews = repository.allReviews().map { it.id }.toSet()

        assertThat(actualReviews).isEqualTo(expectedReviews)
    }

    @Test
    fun `change rating in existing review`() = runTest {
        val tgChatId = dbQuery {
            TgChats.insertAndGetId { it[id] = 2 }
        }

        repository.addReview("movie1", 5, tgChatId.value)
        val review2 = repository.addReview("movie1", 10, tgChatId.value)

        assertThat(review2!!.rating).isEqualTo(10)
    }

    @Test
    fun `changing rating in existing review does not create new one`() = runTest {
        val tgChatId = dbQuery {
            TgChats.insertAndGetId { it[id] = 2 }
        }

        val review1 = repository.addReview("movie1", 5, tgChatId.value)
        val review2 = repository.addReview("movie1", 10, tgChatId.value)

        assertThat(review2!!.id).isEqualTo(review1!!.id)
    }

    @Test
    fun addReview() = runTest {
        val tgChatId = dbQuery {
            TgChats.insertAndGetId { it[id] = 1 }
        }

        val addedReview = repository.addReview("movie", 5, tgChatId.value)!!

        val actualReview = dbQuery {
            Reviews
                .select { (Reviews.movieName eq "movie") and (Reviews.tgChat eq tgChatId) }
                .map(::resultRowToReview)
                .singleOrNull()
        }

        assertThat(actualReview).isNotNull()
        assertThat(actualReview!!.id).isEqualTo(addedReview.id)
        assertThat(actualReview.movieName).isEqualTo(addedReview.movieName)
        assertThat(actualReview.rating).isEqualTo(addedReview.rating)
    }

    @Test
    fun deleteReview() = runTest {
        val tgChatId = dbQuery {
            TgChats.insertAndGetId { it[id] = 1 }
        }
        val reviewId = dbQuery {
            Reviews.insertAndGetId {
                it[movieName] = "movieName"
                it[rating] = 5
                it[tgChat] = tgChatId
            }
        }

        repository.deleteReview(reviewId)
        val remainingReviews = dbQuery {
            Reviews.selectAll().map(::resultRowToReview)
        }

        assertThat(remainingReviews).isEmpty()
    }

    @Test
    fun movieRatingByNameAndTgChatId() = runTest {
        val tgChatId = dbQuery {
            TgChats.insertAndGetId { it[id] = 1 }
        }
        dbQuery {
            Reviews.insertAndGetId {
                it[movieName] = "movieName"
                it[rating] = 5
                it[tgChat] = tgChatId
            }
        }

        val rating = repository.movieRatingByNameAndTgChatId("movieName", tgChatId.value)

        assertThat(rating).isEqualTo(5)
    }

    private fun resultRowToReview(row: ResultRow): Review = Review(
        id = row[Reviews.id],
        movieName = row[Reviews.movieName],
        rating = row[Reviews.rating],
        tgChat = TgChat(row[Reviews.tgChat])
    )
}