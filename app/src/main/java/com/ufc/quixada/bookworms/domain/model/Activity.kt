package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class Activity(
    val activityId: String = "",
    val userIdDono: String = "",
    val userIdOrigem: String = "",
    val idReferencia: String = "",
    val tipoReferencia: ActivityReferenceType = ActivityReferenceType.REVIEW,
    val bookId: String? = null,
    val tipoAtividade: String = "",
    val descricao: String = "",
    val dataAtividade: Date = Date()
)

enum class ActivityReferenceType {
    REVIEW,
    SHELF_ITEM,
    FAVORITE
}