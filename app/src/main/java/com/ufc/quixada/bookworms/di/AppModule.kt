package com.ufc.quixada.bookworms.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ufc.quixada.bookworms.data.local.BookDatabase
import com.ufc.quixada.bookworms.data.local.dao.BookDao
import com.ufc.quixada.bookworms.data.repository.*
import com.ufc.quixada.bookworms.domain.repository.*
import com.ufc.quixada.bookworms.presentation.notification.NotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository = AuthRepositoryImpl(auth, firestore)

    @Provides
    @Singleton
    fun provideBookRepository(firestore: FirebaseFirestore, bookDao: BookDao): BookRepository = BookRepositoryImpl(firestore, bookDao)

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository = UserRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideFavoriteRepository(firestore: FirebaseFirestore): FavoriteRepository = FavoriteRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideFollowRepository(firestore: FirebaseFirestore): FollowRepository = FollowRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideShelfRepository(firestore: FirebaseFirestore): ShelfRepository = ShelfRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideReviewRepository(firestore: FirebaseFirestore): ReviewRepository = ReviewRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideActivityRepository(firestore: FirebaseFirestore): ActivityRepository = ActivityRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideNotificationRepository(firestore: FirebaseFirestore): NotificationRepository = NotificationRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideNotificationHelper(@ApplicationContext context: Context): NotificationHelper = NotificationHelper(context)

    @Provides
    @Singleton
    fun provideOpenLibraryRepository(client: HttpClient): OpenLibraryRepository = OpenLibraryRepositoryImpl(client)

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(CIO) {
        install(Logging) { level = LogLevel.ALL }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    @Provides
    @Singleton
    fun provideBookDatabase(@ApplicationContext context: Context): BookDatabase = Room.databaseBuilder(context, BookDatabase::class.java, "bookworms_db").build()

    @Provides
    @Singleton
    fun provideBookDao(database: BookDatabase): BookDao = database.bookDao
}