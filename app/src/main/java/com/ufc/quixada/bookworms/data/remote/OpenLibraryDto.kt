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
    val key: String,
    val title: String,
    @SerialName("author_name")
    val authorName: List<String>? = null,
    @SerialName("cover_i")
    val coverId: Int? = null,
    val editions: OpenLibraryEditionsDto? = null
)

@Serializable
data class OpenLibraryEditionsDto(
    val docs: List<OpenLibraryEditionDocDto> = emptyList()
)

@Serializable
data class OpenLibraryEditionDocDto(
    val key: String,
    val title: String,
    val language: List<String>? = null,
    @SerialName("cover_i")
    val coverId: Int? = null,
    val isbn: List<String>? = null
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
    val description: JsonElement? = null, // Pode ser String ou Objeto
    @SerialName("isbn_13")
    val isbn13: List<String>? = null,
    @SerialName("isbn_10")
    val isbn10: List<String>? = null,
    val works: List<OpenLibraryWorkDto>? = null
)

@Serializable
data class OpenLibraryWorkDto(
    val key: String
)

@Serializable
data class DescriptionDto(
    val value: String? = null
)
