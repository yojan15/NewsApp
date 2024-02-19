package com.example.newsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Article::class] , version =1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao() : ArticleDao
    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null
        fun getDatabase(context: Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,"app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}