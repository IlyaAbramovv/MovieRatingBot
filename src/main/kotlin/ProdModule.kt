import controller.MoviesController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import service.MovieRatingService

val appModule = module {
    singleOf(::MovieRatingService)
    singleOf(::MoviesController)
}