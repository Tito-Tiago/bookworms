package com.ufc.quixada.bookworms.domain.usecase.book

import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import javax.inject.Inject

class SaveBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(book: Book) {
        repository.saveBook(book)
    }
}