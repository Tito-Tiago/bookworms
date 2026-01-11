package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.Review

sealed class ReviewResult {
    data class Success(val data: List<Review>) : ReviewResult()
    data class Error(val message: String) : ReviewResult()
}

sealed class SingleReviewResult {
    data class Success(val data: Review) : SingleReviewResult()
    data class Error(val message: String) : SingleReviewResult()
}

sealed class SimpleResult {
    object Success : SimpleResult()
    data class Error(val message: String) : SimpleResult()
}
interface ReviewRepository {
    suspend fun getReviewsByBook(bookId: String) : ReviewResult
    suspend fun addReview(review: Review) : SingleReviewResult
    suspend fun updateReview(review: Review) : SingleReviewResult
    suspend fun deleteReview(reviewId: String, bookId: String) : SimpleResult
    suspend fun getReviewsByUser(userId: String) : ReviewResult
    suspend fun getReviewById(reviewId: String) : SingleReviewResult
}