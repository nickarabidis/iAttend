package com.example.attendtest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.room.RoomDao
import com.example.attendtest.database.roomAndUser.Converters
import com.example.attendtest.database.roomAndUser.RoomAndUser
import com.example.attendtest.database.roomAndUser.RoomAndUserDao
import com.example.attendtest.database.user.User
import com.example.attendtest.database.user.UserDao


@Database(
    entities = [User::class, Room::class, RoomAndUser::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class DatabaseApp: RoomDatabase() {

    abstract val userDao: UserDao
    abstract val roomDao: RoomDao
    abstract val roomAndUserDao: RoomAndUserDao
}