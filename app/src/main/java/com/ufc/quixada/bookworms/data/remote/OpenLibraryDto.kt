package com.ufc.quixada.bookworms.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class OpenLibrarySearchResponse(
    val docs: List<OpenLibraryDocDto> = emptyList()
)

@Serializable
data class OpenLibraryDocDto(
    val key: String? = null,
    val title: String? = null,
    @SerialName("author_name")
    val authorName: List<String>? = null,
    @SerialName("cover_i")
    val coverId: Int? = null,
    val isbn: List<String>? = null,
    val language: List<String>? = null
)

@Serializable
data class OpenLibraryRatingResponse(
    @SerialName("summary")
    val summary: RatingSummary? = null
)

@Serializable
data class RatingSummary(
    val average: Float? = null
)

@Serializable
data class OpenLibraryBookDetailsDto(
    val description: JsonElement? = null,
    @SerialName("isbn_13")
    val isbn13: List<String>? = null,
    @SerialName("isbn_10")
    val isbn10: List<String>? = null
)