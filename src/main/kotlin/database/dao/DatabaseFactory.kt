package database.dao

import database.model.Reviews
import database.model.TgChats
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://localhost:5432/reviews"
        val database = Database.connect(
            url = jdbcURL,
            driver = driverClassName,
            user = "postgres1",
            password = "postgres1"
        )

        transaction(database) {
            create(Reviews)
            create(TgChats)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}