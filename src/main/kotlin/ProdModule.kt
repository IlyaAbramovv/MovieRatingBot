import controller.MoviesController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single<String> { "Hello World" }
    single<HelloClass> { HelloClass(get()) }

    singleOf(::MoviesController)
}