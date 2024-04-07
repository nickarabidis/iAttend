package com.example.attendtest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.attendtest.database.user.User
import com.example.attendtest.database.user.UserDao


@Database(
    entities = [User::class],
    version = 1
)
abstract class DatabaseApp: RoomDatabase() {

    abstract val userDao: UserDao
}