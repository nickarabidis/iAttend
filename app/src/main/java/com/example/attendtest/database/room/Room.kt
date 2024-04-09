package com.example.attendtest.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Room(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val roomName: String,
    val password: String,
    val emailAdmin: String
)
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0