package com.example.attendtest.database.roomAndUser

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.user.User
import java.util.Date

@Entity(
    primaryKeys = ["roomId", "userEmail"],
    foreignKeys = [
        ForeignKey(
            entity = Room::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["email"],
            childColumns = ["userEmail"]
        )
    ],
    indices = [
        Index(value = ["roomId"]),
        Index(value = ["userEmail"])
    ]
)
data class RoomAndUser(
    val roomId: Long,
    val userEmail: String,
    val isPresent: Boolean,
    val presentDate: Date? = null
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0