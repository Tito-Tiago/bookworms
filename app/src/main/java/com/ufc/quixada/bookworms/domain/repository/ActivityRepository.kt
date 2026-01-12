package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.Activity

interface ActivityRepository {
    suspend fun createActivity(activity: Activity)
    suspend fun getActivitiesFromUsers(userIds: List<String>): List<Activity>
}