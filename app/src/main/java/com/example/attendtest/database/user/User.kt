package com.example.attendtest.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,

)
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0