package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ufc.quixada.bookworms.domain.model.User
import com.ufc.quixada.bookworms.domain.repository.UserRepository
import com.ufc.quixada.bookworms.domain.repository.UserResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import android.util.Log

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : UserRepository {
    private val usersCollection = firestore.collection("users")

    override suspend fun saveFCMToken(token: String) {
        val user = authRepository.getCurrentUser()
        val userId = user?.uid
        if (userId != null) {
            try {
                usersCollection.document(userId)
                    .update("fcmToken", token)
                    .await()
                Log.d("UserRepositoryImpl", "FCM token saved for user $userId")
            } catch (e: Exception) {
                Log.e("UserRepositoryImpl", "Error saving FCM token for user $userId: ${e.message}")
            }
        } else {
            Log.e("UserRepositoryImpl", "No current user to save FCM token")
        }
    }

    override suspend fun getUser(uid: String): UserResult {
        return try {
            val document = usersCollection
                .document(uid)
                .get()
                .await()

            val user = document.toObject(User::class.java)
            if (user != null) {
                UserResult.Success(user)
            } else {
                UserResult.Error("Usuário não encontrado")
            }
        } catch (e: Exception) {
            UserResult.Error(e.message ?: "Erro ao buscar perfil")
        }
    }

    override suspend fun updateUser(user: User): UserResult {
        return try {
            usersCollection
                .document(user.uid)
                .set(user)
                .await()

            UserResult.Success(user)
        } catch (e: Exception) {
            UserResult.Error(e.message ?: "Erro ao atualizar perfil")
        }
    }

    override suspend fun searchUsers(query: String): List<User> {
        if (query.isBlank()) return emptyList()

        return try {
            usersCollection
                .orderBy("nome")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .limit(20)
                .get()
                .await()
                .toObjects(User::class.java)
        } catch (e: Exception) {
            Log.e("UserRepository", "Erro na busca: ${e.message}")
            emptyList()
        }
    }
}