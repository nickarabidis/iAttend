package com.example.attendtest.database.roomAndUser

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.user.User

@Entity(
    primaryKeys = ["roomId", "userEmail"],
    foreignKeys = [
        ForeignKey(entity = Room::class, parentColumns = ["id"], childColumns = ["roomId"]),
        ForeignKey(entity = User::class, parentColumns = ["email"], childColumns = ["userEmail"])
    ],
    indices = [
        Index(value = ["roomId"]),
        Index(value = ["userEmail"])
    ]
)
data class RoomAndUser(
    val roomId: Long,
    val userEmail: String,
    val isPresent: Boolean
)

//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0