package com.example.attendtest.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Room(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val roomName: String,
    // password can be null
    val password: String?,
    val emailAdmin: String,
    // visibility
    val isVisible: Boolean,
    val passwordNeeded: Boolean
)
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0