package database.dao

import IntegrationEnvironment
import com.google.common.truth.Truth.assertThat
import database.dao.DatabaseFactory.dbQuery
import database.model.User
import database.model.Users
import database.model.UsersUsers
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class UserRepositoryTest : IntegrationEnvironment() {
    private val repository = UserRepositoryImpl()

    @BeforeTest
    fun createTable() {
        transaction {
            create(Users, UsersUsers)
        }
    }

    @AfterTest
    fun cleanUp() {
        transaction {
            drop(Users, UsersUsers)
        }
    }

    @Test
    fun `subscribedTo and subscribers processed correctly`() = runTest {
        val user1 = dbQuery { User(Users.insert { it[name] = "user1" }.resultedValues!!.single()[Users.id]) }
        val user2 = dbQuery { User(Users.insert { it[name] = "user2" }.resultedValues!!.single()[Users.id]) }
        val user3 = dbQuery { User(Users.insert { it[name] = "user3" }.resultedValues!!.single()[Users.id]) }

        repository.subscribe(user2.id, user1.id)
        repository.subscribe(user3.id, user1.id)

        assertThat(user2.subscribedTo.map { it.id }).containsExactly(user1.id)
        assertThat(user3.subscribedTo.map { it.id }).containsExactly(user1.id)
        assertThat(user1.subscribers.map { it.id }).containsExactly(user2.id, user3.id)
    }
}