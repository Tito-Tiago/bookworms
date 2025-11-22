package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class Book(
    val bookId: String = "",
    val titulo: String = "",
    val autor: String = "",
    val sinopse: String = "",
    val capaUrl: String? = null,
    val isbn: String? = null,
    val notaMediaComunidade: Float = 0f,
    val notaApiExterna: Float? = null,
    val fonteApi: String? = null,
    val numAvaliacoes: Int = 0,
    val dataCadastro: Date = Date()
)