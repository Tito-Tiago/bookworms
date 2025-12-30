package com.ufc.quixada.bookworms.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val converId: Int? = null,
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
data class OpenLibraryRatingResponseO(
    val sumary: RatingSumary? = null
)

@Serializable
data class RatingSumary(
    val average: Float? = null
)

@Serializable
data class OpenLibraryBookDetailsDto(
    val description: DescriptionDto? = null, //pode ser um objeto ou string
    @SerialName("isbn_13")
    val isbn13: List<String>? = null,
    @SerialName("isbn_10")
    val isbn10: List<String>? = null

)

data class DescriptionDto(
    val value: String? = null
)
