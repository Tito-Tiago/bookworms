package com.ufc.quixada.bookworms.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity (
    @PrimaryKey
    val bookId: String,
    val titulo: String,
    val autor: String,
    val sinopse: String,
    val capaUrl: String?,
    val isbn: String?,
    val notaMediaComunidade: Float,
    val notaApiExterna: Float?,
    val fonteApi: String?, // "GOOGLE", "OPEN_LIBRARY"
    val numAvaliacoes: Int
)