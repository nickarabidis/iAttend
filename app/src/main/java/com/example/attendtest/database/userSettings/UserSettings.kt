package com.example.attendtest.database.userSettings

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.attendtest.database.user.User

@Entity(
    primaryKeys = ["userEmail"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["email"],
            childColumns = ["userEmail"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userEmail"])
    ]
)
data class UserSettings(
    val userEmail: String,
    val language: String = "EN",
    val theme: String = "LIGHT"
)