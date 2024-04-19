package com.example.attendtest.database.roomAndFavorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.roomAndUser.RoomAndUser

@Dao
interface RoomAndFavoritesDao {

    @Upsert
    suspend fun upsertFavoriteRoom(roomAndFavorites: RoomAndFavorites)

    @Query("DELETE FROM roomandfavorites WHERE roomId = :id AND userEmail = :userEmail")
    suspend fun deleteFavoriteRoom(id: Long, userEmail: String)

    @Query("SELECT isFavorite FROM RoomAndFavorites WHERE userEmail = :userEmail AND roomId = :roomId")
    suspend fun getIsFavorite(userEmail: String, roomId: Long): Boolean

    @Query("SELECT COUNT(*) FROM roomandfavorites WHERE roomId = :id AND userEmail = :userEmail")
    suspend fun isRoomFavorite(id: Long, userEmail: String): Int

    @Query("SELECT roomId FROM RoomAndFavorites WHERE userEmail = :userEmail AND roomId = :roomId")
    suspend fun getFavoriteRoomIdFromUserEmail(userEmail: String?, roomId: Long): Long

    @Query("SELECT isPresent FROM RoomAndUser WHERE userEmail = :userEmail AND roomId = :roomId")
    suspend fun getUserEmailFromRoomId(userEmail: String, roomId: Long): Boolean

    @Query("SELECT * FROM RoomAndFavorites WHERE roomId = :roomId AND userEmail = :userEmail")
    suspend fun getRoomAndFavoriteFromId(roomId: Long, userEmail: String): RoomAndFavorites



}