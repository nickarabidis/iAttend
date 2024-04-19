package com.example.attendtest.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Upsert
    suspend fun upsertRoom(room: Room): Long

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertRoom(room: Room): Long

    @Update
    suspend fun updateRoom(room: Room)

    @Delete
    suspend fun deleteRoom(room: Room)

    // visibility
    @Query("SELECT passwordNeeded FROM room WHERE id = :id")
    suspend fun getPasswordNeededFromId(id: Long): Boolean

//    @Query("SELECT isVisible FROM room WHERE id = :id")
//    suspend fun getIsVisibleFromId(id: Long): Boolean

    @Query("UPDATE room SET isVisible = 1, passwordNeeded = 0 WHERE id = :id")
    suspend fun setRoomToVisible(id: Long)

    @Query("UPDATE room SET isVisible = 1, passwordNeeded = 1 WHERE id = :id")
    suspend fun setRoomToPasswordNeeded(id: Long)

    @Query("UPDATE room SET isVisible = 0 WHERE id = :id")
    suspend fun setRoomToInvisible(id: Long)

    @Query("SELECT * FROM room WHERE emailAdmin = :emailAdmin")
    fun getUserRooms(emailAdmin: String?): Room

    @Query("SELECT * FROM room WHERE id = :id")
    suspend fun getRoomFromId(id: Long): Room

    @Query("SELECT passwordNeeded FROM Room WHERE id = :roomId")
    suspend fun getPasswordNeededFromRoomId(roomId: Long): Boolean




//    @Query("SELECT id FROM room WHERE id = :id")
//    suspend fun getId(id: Int): Int

//    @Query("SELECT * FROM room WHERE emailAdmin = :emailAdmin")
//    fun getUserRooms(emailAdmin: String?): Room

    @Query("SELECT roomName FROM room WHERE roomName = :roomName")
    fun getRoomName(roomName: String): String

    @Query("SELECT password FROM room WHERE roomName = :roomName")
    fun getPassword(roomName: String): String

    @Query("SELECT emailAdmin FROM room WHERE roomName = :roomName")
    fun getEmailAdmin(roomName: String): String

    @Query("SELECT * FROM room ORDER BY roomName ASC")
    fun getRoomsOrderedByRoomName(): Flow<List<Room>>

    // @Query("SELECT * FROM room ORDER BY password ASC")
    // fun getRoomsOrderedByPassword(): Flow<List<Room>>

    @Query("SELECT * FROM room ORDER BY emailAdmin ASC")
    fun getRoomsOrderedByEmailAdmin(): Flow<List<Room>>

    // TODO
    @Query("SELECT * FROM room, roomandfavorites WHERE id = roomId ORDER BY isFavorite DESC, id ASC")
    fun getRoomsOrderedByFavorites(): Flow<List<Room>>

    @Query("SELECT * FROM room ORDER BY id ASC")
    fun getRoomsOrderedById(): Flow<List<Room>>
}