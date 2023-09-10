package dto

import kotlinx.serialization.Serializable

@Serializable
data class SubscribersResponse(
    val username: String,
    val subscribersChats: List<Long>,
)
