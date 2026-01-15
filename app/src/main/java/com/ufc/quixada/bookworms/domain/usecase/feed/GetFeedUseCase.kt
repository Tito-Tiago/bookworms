package com.ufc.quixada.bookworms.domain.usecase.feed

import com.ufc.quixada.bookworms.domain.model.Activity
import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.model.Review
import com.ufc.quixada.bookworms.domain.repository.ActivityRepository
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import com.ufc.quixada.bookworms.domain.repository.FollowRepository
import com.ufc.quixada.bookworms.domain.repository.ReviewRepository
import com.ufc.quixada.bookworms.domain.repository.ReviewResult
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val followRepository: FollowRepository,
    private val activityRepository: ActivityRepository,
    private val reviewRepository: ReviewRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(): List<Activity> {
        val currentUser = authRepository.getCurrentUser() ?: return emptyList()

        val followedIds = followRepository.getFollowedUserIds(currentUser.uid)

        if (followedIds.isEmpty()) return emptyList()

        return activityRepository.getActivitiesFromUsers(followedIds)
    }

    suspend fun getFeedReviews(): List<Pair<Review, Book?>> {
        val currentUser = authRepository.getCurrentUser() ?: return emptyList()
        val followedIds = followRepository.getFollowedUserIds(currentUser.uid)

        if (followedIds.isEmpty()) return emptyList()

        val result = reviewRepository.getReviewsByUsers(followedIds)

        return if (result is ReviewResult.Success) {
            result.data.map { review ->
                val bookResult = bookRepository.getBook(review.bookId)
                val book = if (bookResult is SingleBookResult.Success) bookResult.data else null
                Pair(review, book)
            }
        } else {
            emptyList()
        }
    }
}