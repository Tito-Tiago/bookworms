package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class Notification(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "REVIEW",
    val timestamp: Date = Date(),
    val read: Boolean = false,
    val senderId: String = "",
    val bookId: String = ""
)