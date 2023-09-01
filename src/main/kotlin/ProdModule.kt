import controller.MoviesController
import database.dao.RatingRepository
import database.dao.RatingRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import service.MovieRatingService


val appModule = module {
    single<RatingRepository> { RatingRepositoryImpl() }
    singleOf(::MovieRatingService)
    singleOf(::MoviesController)
}