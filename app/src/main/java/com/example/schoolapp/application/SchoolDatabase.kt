package com.example.sekolahan.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.schoolapp.dao.SchoolDao
import com.example.schoolapp.model.School


@Database(entities = [School::class], version = 2, exportSchema = false)
abstract class SchoolDatabase: RoomDatabase(){
    abstract fun schoolDao(): SchoolDao

    companion object{
        private var INSTANCE: SchoolDatabase? = null

        private val migration1To2: Migration = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE school_table ADD COLUMN latitude Double DEFAULT 0.0")
                database.execSQL("ALTER TABLE school_table ADD COLUMN longitude Double DEFAULT 0.0")
            }
        }

        fun getDatabase(context: Context): SchoolDatabase {
            return  INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDatabase::class.java,
                    "school_database_1"
                )
                    .addMigrations(migration1To2)
                    .allowMainThreadQueries()
                    .build()

                INSTANCE= instance
                instance
            }
        }
    }
}