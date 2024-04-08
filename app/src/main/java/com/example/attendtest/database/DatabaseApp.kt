package com.example.attendtest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.room.RoomDao
import com.example.attendtest.database.user.User
import com.example.attendtest.database.user.UserDao


@Database(

    entities = [User::class, Room::class],
    version = 2
)
abstract class DatabaseApp: RoomDatabase() {

    abstract val userDao: UserDao
    abstract val roomDao: RoomDao
}