package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ufc.quixada.bookworms.domain.model.Follow
import com.ufc.quixada.bookworms.domain.repository.FollowRepository
import com.ufc.quixada.bookworms.domain.repository.FollowResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FollowRepository {

    private val collection = firestore.collection("follows")

    override suspend fun followUser(currentUserId: String, targetUserId: String): FollowResult {
        return try {
            val follow = Follow(
                userIdSeguidor = currentUserId,
                userIdSeguido = targetUserId
            )
            // Cria um ID único composto para evitar duplicatas facilmente ou deixe o Firestore gerar
            val docId = "${currentUserId}_${targetUserId}"

            collection.document(docId).set(follow).await()
            FollowResult.Success
        } catch (e: Exception) {
            FollowResult.Error(e.message ?: "Erro ao seguir usuário")
        }
    }

    override suspend fun unfollowUser(currentUserId: String, targetUserId: String): FollowResult {
        return try {
            val docId = "${currentUserId}_${targetUserId}"
            collection.document(docId).delete().await()
            FollowResult.Success
        } catch (e: Exception) {
            FollowResult.Error(e.message ?: "Erro ao deixar de seguir")
        }
    }

    override suspend fun isFollowing(currentUserId: String, targetUserId: String): Boolean {
        return try {
            val docId = "${currentUserId}_${targetUserId}"
            val doc = collection.document(docId).get().await()
            doc.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getFollowersCount(userId: String): Int {
        return try {
            val query = collection.whereEqualTo("userIdSeguido", userId).get().await()
            query.size()
        } catch (e: Exception) {
            0
        }
    }

    override suspend fun getFollowingCount(userId: String): Int {
        return try {
            val query = collection.whereEqualTo("userIdSeguidor", userId).get().await()
            query.size()
        } catch (e: Exception) {
            0
        }
    }
}