package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ufc.quixada.bookworms.domain.model.User
import com.ufc.quixada.bookworms.domain.model.UserRole
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.AuthResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun register(nome: String, email: String, password: String): AuthResult {
        return try {
            // Criar usuário no Firebase Auth
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return AuthResult.Error("Erro ao criar usuário")

            // Criar documento do usuário no Firestore
            val user = User(
                uid = firebaseUser.uid,
                nome = nome,
                email = email,
                role = UserRole.ALUNO
            )

            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(user)
                .await()

            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Erro desconhecido ao cadastrar")
        }
    }

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            // Fazer login no Firebase Auth
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return AuthResult.Error("Erro ao fazer login")

            // Buscar dados do usuário no Firestore
            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()

            val user = userDoc.toObject(User::class.java)
                ?: return AuthResult.Error("Usuário não encontrado no banco de dados")

            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Erro desconhecido ao fazer login")
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return try {
            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            userDoc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}