package database.dao

import IntegrationEnvironment
import com.google.common.truth.Truth.assertThat
import database.dao.DatabaseFactory.dbQuery
import database.model.TgChat
import database.model.TgChats
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class TgChatRepositoryTest : IntegrationEnvironment() {
    private val repository = TgChatRepositoryImpl()

    @BeforeTest
    fun createTable() {
        transaction {
            create(TgChats)
        }
    }

    @AfterTest
    fun cleanUp() {
        transaction {
            drop(TgChats)
        }
    }

    @Test
    fun register() = runTest {
        val registerTgChat = repository.register(1)!!

        val actualTgChat = dbQuery {
            TgChats.selectAll().singleOrNull()?.let { TgChat(it[TgChats.id]) }
        }

        assertThat(actualTgChat).isNotNull()
        assertThat(actualTgChat!!.id).isEqualTo(registerTgChat.id)
    }

    @Test
    fun delete() = runTest {
        repository.register(1)

        repository.delete(1)
        val remainingChats = dbQuery { TgChats.selectAll().toList() }

        assertThat(remainingChats).isEmpty()
    }
}
