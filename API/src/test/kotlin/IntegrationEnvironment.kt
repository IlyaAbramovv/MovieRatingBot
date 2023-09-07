import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class IntegrationEnvironment {
    companion object {
        protected val postgreSQLContainer: PostgreSQLContainer<Nothing> = PostgreSQLContainer<Nothing>("postgres:14")

        @BeforeAll
        @JvmStatic
        fun setupContainer() {
            postgreSQLContainer.start()
            Database.connect(
                url = postgreSQLContainer.jdbcUrl,
                driver = postgreSQLContainer.driverClassName,
                user = postgreSQLContainer.username,
                password = postgreSQLContainer.password,
            )
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            postgreSQLContainer.stop()
        }
    }
}
