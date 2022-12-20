package com.example.repetitiontest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.repetitiontest.database.results.ResultDao
import com.example.repetitiontest.database.results.ResultEntity
import com.example.repetitiontest.database.subjects.SubjectDao
import com.example.repetitiontest.database.subjects.SubjectEntity
import com.example.repetitiontest.database.users.UserDao
import com.example.repetitiontest.database.users.UserEntity


@Database(
    entities = [
        UserEntity::class,
        SubjectEntity::class,
        ResultEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun resultDao(): ResultDao
    abstract fun subjectDao(): SubjectDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE!!
        }
    }
}