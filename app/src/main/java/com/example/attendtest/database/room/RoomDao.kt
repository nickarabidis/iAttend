package com.example.attendtest.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Upsert
    suspend fun upsertRoom(room: Room)

    @Delete
    suspend fun deleteRoom(room: Room)

    @Query("SELECT roomName FROM room WHERE roomName = :roomName")
    fun getRoomName(roomName: String): String

    @Query("SELECT password FROM room WHERE roomName = :roomName")
    fun getPassword(roomName: String): String

    @Query("SELECT emailAdmin FROM room WHERE roomName = :roomName")
    fun getEmailAdmin(roomName: String): String


    @Query("SELECT * FROM room ORDER BY roomName ASC")
    fun getUsersOrderedByRoomName(): Flow<List<Room>>

    @Query("SELECT * FROM room ORDER BY password ASC")
    fun getUsersOrderedByPassword(): Flow<List<Room>>

    @Query("SELECT * FROM room ORDER BY emailAdmin ASC")
    fun getUsersOrderedByEmailAdmin(): Flow<List<Room>>


}