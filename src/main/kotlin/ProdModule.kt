import controller.MoviesController
import database.RatingRepository
import database.RatingRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import service.MovieRatingService
import kotlin.math.sin

val appModule = module {
    single<RatingRepository> { RatingRepositoryImpl() }
    singleOf(::MovieRatingService)
    singleOf(::MoviesController)
}