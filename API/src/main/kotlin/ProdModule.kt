import controller.MoviesController
import controller.TgChatController
import database.dao.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import service.MovieRatingService
import service.TgChatService


val appModule = module {
    single<RatingRepository> { RatingRepositoryImpl() }
    single<TgChatRepository> { TgChatRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }

    singleOf(::MovieRatingService)
    singleOf(::TgChatService)

    singleOf(::MoviesController)
    singleOf(::TgChatController)
}
