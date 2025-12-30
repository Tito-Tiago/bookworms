package com.ufc.quixada.bookworms.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ufc.quixada.bookworms.data.repository.AuthRepositoryImpl
import com.ufc.quixada.bookworms.data.repository.BookRepositoryImpl
import com.ufc.quixada.bookworms.data.repository.FavoriteRepositoryImpl
import com.ufc.quixada.bookworms.data.repository.OpenLibraryRepositoryImpl
import com.ufc.quixada.bookworms.data.repository.UserRepositoryImpl
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.FavoriteRepository
import com.ufc.quixada.bookworms.domain.repository.OpenLibraryRepository
import com.ufc.quixada.bookworms.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository {
        return AuthRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideBookRepository(
        firestore: FirebaseFirestore
    ): BookRepository {
        return BookRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        firestore: FirebaseFirestore
    ): FavoriteRepository {
        return FavoriteRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideOpenLibraryRepository(
        client: HttpClient
    ): OpenLibraryRepository {
        return OpenLibraryRepositoryImpl(client)
    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging) { //logging plugin
                level = LogLevel.ALL
            }

            install(ContentNegotiation) { // JSON serialization/deserialization
                json(Json{
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}