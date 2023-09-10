import controller.MoviesController
import controller.TgChatController
import database.dao.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import service.MovieRatingService
import service.TgChatService


val appModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    single<RatingRepository> { RatingRepositoryImpl() }
    single<TgChatRepository> { TgChatRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }

    singleOf(::MovieRatingService)
    singleOf(::TgChatService)

    singleOf(::MoviesController)
    singleOf(::TgChatController)
}
