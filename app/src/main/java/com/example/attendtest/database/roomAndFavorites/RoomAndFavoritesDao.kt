package com.example.attendtest.database.roomAndFavorites

import androidx.room.Dao
import androidx.room.Query
import com.example.attendtest.database.room.Room

@Dao
interface RoomAndFavoritesDao {
    @Query("SELECT isFavorite FROM RoomAndFavorites, room WHERE roomId = :id")
    suspend fun isRoomFavorite(id: Long): Boolean
}