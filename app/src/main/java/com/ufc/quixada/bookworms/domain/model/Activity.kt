package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class Activity(
    val activityId: String = "",
    val userIdDono: String = "", // Usuário dono do feed
    val userIdOrigem: String = "", // Usuário que gerou a atividade
    val idReferencia: String = "", // ID da entidade referenciada (Review, ShelfItem, Favorite)
    val tipoReferencia: ActivityReferenceType = ActivityReferenceType.REVIEW,
    val bookId: String? = null, // Para facilitar queries
    val tipoAtividade: String = "", // Ex: "avaliou", "adicionou à estante", "favoritou"
    val descricao: String = "",
    val dataAtividade: Date = Date()
)

enum class ActivityReferenceType {
    REVIEW,
    SHELF_ITEM,
    FAVORITE
}