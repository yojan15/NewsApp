package com.example.newsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CachedArticle::class , Article::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the cached_articles table if it doesn't exist
                database.execSQL("CREATE TABLE IF NOT EXISTS cached_articles (publishedAt TEXT, author TEXT, urlToImage TEXT, description TEXT, title TEXT, url TEXT PRIMARY KEY, content TEXT)")

                // Add the new_column to the table
                database.execSQL("ALTER TABLE cached_articles ADD COLUMN new_column TEXT")
            }
        }
    }
}