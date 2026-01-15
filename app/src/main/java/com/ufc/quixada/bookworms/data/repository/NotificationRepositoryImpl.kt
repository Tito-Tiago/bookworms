package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ufc.quixada.bookworms.domain.model.Notification
import com.ufc.quixada.bookworms.domain.repository.NotificationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NotificationRepository {

    override fun getNotifications(userId: String): Flow<List<Notification>> = callbackFlow {
        val subscription = firestore.collection("notifications")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    val notifications = it.toObjects(Notification::class.java)
                    trySend(notifications)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun sendNotification(notification: Notification) {
        val docRef = firestore.collection("notifications").document()
        firestore.collection("notifications")
            .document(docRef.id)
            .set(notification.copy(id = docRef.id))
            .await()
    }

    override suspend fun markAsRead(notificationId: String) {
        firestore.collection("notifications")
            .document(notificationId)
            .update("read", true)
            .await()
    }
}