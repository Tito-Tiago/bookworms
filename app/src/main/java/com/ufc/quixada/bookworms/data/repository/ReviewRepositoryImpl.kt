package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.ufc.quixada.bookworms.domain.model.Review
import com.ufc.quixada.bookworms.domain.repository.ReviewRepository
import com.ufc.quixada.bookworms.domain.repository.ReviewResult
import com.ufc.quixada.bookworms.domain.repository.SimpleResult
import com.ufc.quixada.bookworms.domain.repository.SingleReviewResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReviewRepository {
    override suspend fun getReviewsByBook(bookId: String): ReviewResult {
        return try {
            val snapshot = firestore.collection("reviews")
                .whereEqualTo("bookId", bookId)
                .limit(20)
                .get()
                .await()

            val bookReviews = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Review::class.java)?.copy(reviewId = doc.id)
            }

            ReviewResult.Success(bookReviews)
        } catch (e: Exception) {
            ReviewResult.Error(e.message ?: "Erro ao buscar reviews do livro")
        }
    }

    override suspend fun addReview(review: Review): SingleReviewResult {
        return try {
            firestore.runTransaction { transaction ->
                val bookRef = firestore.collection("books").document(review.bookId)
                val bookSnapshot = transaction.get(bookRef)

                val currentCount = bookSnapshot.getLong("numAvaliacoes") ?: 0
                val currentAvg = bookSnapshot.getDouble("notaMediaComunidade") ?: 0.0
                val newCount = currentCount + 1
                val newAvg = ((currentCount * currentAvg) + review.nota) / newCount

                val newReviewRef = firestore.collection("reviews").document(review.reviewId)

                transaction.set(newReviewRef, review)
                transaction.update(bookRef, mapOf(
                    "numAvaliacoes" to newCount,
                    "notaMediaComunidade" to newAvg
                ))
            }

            SingleReviewResult.Success(review)
        } catch (e: Exception) {
            SingleReviewResult.Error(e.message ?: "Erro ao salvar avaliação")
        }
    }

    override suspend fun updateReview(review: Review): SingleReviewResult {
        return try {
            firestore.runTransaction { transaction ->
                val bookRef = firestore.collection("books").document(review.bookId)
                val bookSnapshot = transaction.get(bookRef)

                val oldReviewRef = firestore.collection("reviews").document(review.reviewId)
                val oldReviewSnapshot = transaction.get(oldReviewRef)

                val oldReviewNota: Double = oldReviewSnapshot.getDouble("nota") ?: 0.0
                val currentCount = bookSnapshot.getLong("numAvaliacoes") ?: 0
                val currentAvg = bookSnapshot.getDouble("notaMediaComunidade") ?: 0.0
                val newAvg = ((currentCount * currentAvg) + review.nota - oldReviewNota) / currentCount

                transaction.set(oldReviewRef, review)
                transaction.update(bookRef, mapOf(
                    "notaMediaComunidade" to newAvg
                ))
            }

            SingleReviewResult.Success(review)
        } catch (e: Exception) {
            SingleReviewResult.Error(e.message ?: "Erro ao salvar avaliação")
        }
    }

    override suspend fun deleteReview(reviewId: String, bookId: String): SimpleResult {
        return try {
            firestore.runTransaction { transaction ->
                val oldReviewRef = firestore.collection("reviews").document(reviewId)
                val oldReviewSnapshot = transaction.get(oldReviewRef)

                val bookRef = firestore.collection("books").document(bookId)
                val bookSnapshot = transaction.get(bookRef)

                val oldReviewNota = oldReviewSnapshot.getDouble("nota") ?: 0.0
                val currentCount = bookSnapshot.getLong("numAvaliacoes") ?: 0
                val currentAvg = bookSnapshot.getDouble("notaMediaComunidade") ?: 0.0
                val newCount = currentCount - 1
                val newAvg = if (newCount > 0) {
                    ((currentCount * currentAvg) - oldReviewNota) / newCount
                } else {
                    0.0
                }

                transaction.delete(oldReviewRef)
                transaction.update(bookRef, mapOf(
                    "numAvaliacoes" to newCount,
                    "notaMediaComunidade" to newAvg
                ))
            }

            SimpleResult.Success
        } catch (e: Exception) {
            SimpleResult.Error(e.message ?: "Erro ao deletar review")
        }
    }

    override suspend fun getReviewsByUser(userId: String): ReviewResult {
        return try {
            val snapshot = firestore.collection("reviews")
                .whereEqualTo("userId", userId)
                .limit(20)
                .get()
                .await()

            val userReviews = snapshot.mapNotNull { doc ->
                doc.toObject(Review::class.java).copy(reviewId = doc.id)
            }

            ReviewResult.Success(userReviews)
        } catch (e: Exception) {
            ReviewResult.Error(e.message ?: "Erro ao buscar reviews do usuário")
        }
    }

    override suspend fun getReviewById(reviewId: String): SingleReviewResult {
        return try {
            val document = firestore.collection("reviews")
                .document(reviewId)
                .get()
                .await()

            val review = document.toObject(Review::class.java)

            if (review != null) {
                val reviewWithId = review.copy(reviewId = reviewId)
                SingleReviewResult.Success(reviewWithId)
            } else {
                SingleReviewResult.Error("Review não encontrada")
            }
        } catch (e: Exception) {
            SingleReviewResult.Error(e.message ?: "Erro ao buscar review")
        }
    }
}