import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    println("Starting bot")
    embeddedServer(Netty, port = 8081, module = Application::module).start(wait = true)
}
