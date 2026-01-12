package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ufc.quixada.bookworms.domain.model.Activity
import com.ufc.quixada.bookworms.domain.repository.ActivityRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ActivityRepository {

    private val collection = firestore.collection("activities")

    override suspend fun createActivity(activity: Activity) {
        try {
            collection.add(activity).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getActivitiesFromUsers(userIds: List<String>): List<Activity> {
        if (userIds.isEmpty()) return emptyList()

        val safeUserIds = userIds.take(30)

        return try {
            collection
                .whereIn("userIdOrigem", safeUserIds)
                .orderBy("dataAtividade", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .await()
                .toObjects(Activity::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}