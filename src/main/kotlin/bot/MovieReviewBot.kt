package bot

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import io.ktor.client.*
import io.ktor.client.request.*

class MovieReviewBot(
    private val bot: TelegramBot,
    private val client: HttpClient,
) {
   suspend fun start() {
       bot.buildBehaviourWithLongPolling {
           onCommand("start") {
               client.post("http://localhost:8080/telegram/register-chat/${it.chat.id.chatId}")
           }
           onCommand("stop") {
               client.delete("http://localhost:8080/telegram/${it.chat.id.chatId}")
           }
       }
   }

}