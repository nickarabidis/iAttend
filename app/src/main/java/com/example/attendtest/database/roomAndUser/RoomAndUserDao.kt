package com.example.attendtest.database.roomAndUser

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.user.User
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomAndUserDao {

    @Upsert
    suspend fun upsertRoomAndUser(roomAndUser: RoomAndUser)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertRoom(room: Room): Long

//    @Update
//    suspend fun updateRoom(room: Room)

    @Delete
    suspend fun deleteRoomAndUser(roomAndUser: RoomAndUser)

    @Query("SELECT * FROM RoomAndUser WHERE roomId = :roomId AND userEmail = :userEmail")
    suspend fun getRoomAndUserFromId(roomId: Long, userEmail: String): RoomAndUser

//    @Query("SELECT * FROM room WHERE id = :id")
//    suspend fun getRoomFromId(id: Long): Room

    @Query("SELECT * FROM RoomAndUser ORDER BY userEmail ASC")
    fun getRoomsOrderedByUserEmail(): Flow<List<RoomAndUser>>


    @Query("SELECT isPresent FROM RoomAndUser WHERE roomId = :roomId AND userEmail = :userEmail")
    fun getIsPresent(roomId: Long, userEmail: String): Boolean

//    @Query("SELECT * FROM RoomAndUser ORDER BY roomId ASC")
//    fun getRoomsOrderedByRoomId(): Flow<List<RoomAndUser>>

//    @Query("SELECT id FROM room WHERE id = :id")
//    suspend fun getId(id: Int): Int

//    @Query("SELECT roomName FROM RoomAndUser WHERE roomName = :roomName")
//    fun getRoomName(roomName: String): String
//
//    @Query("SELECT password FROM RoomAndUser WHERE roomName = :roomName")
//    fun getPassword(roomName: String): String
//
//    @Query("SELECT emailAdmin FROM RoomAndUser WHERE roomName = :roomName")
//    fun getEmailAdmin(roomName: String): String


}