package dto

import kotlinx.serialization.Serializable


@Serializable
data class PresentMovieResponse(val isPresent: Boolean, val rating: Int?)