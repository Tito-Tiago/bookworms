package com.ufc.quixada.bookworms.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ufc.quixada.bookworms.data.local.dao.BookDao
import com.ufc.quixada.bookworms.data.local.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1)
abstract class BookDatabase: RoomDatabase() {
    abstract val bookDao: BookDao
}