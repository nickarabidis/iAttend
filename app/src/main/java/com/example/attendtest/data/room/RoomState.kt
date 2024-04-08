package com.example.attendtest.data.room

import com.example.attendtest.database.room.Room
import com.example.attendtest.database.room.roomSortType


data class RoomState (
    val rooms: List<Room> = emptyList(),
    val roomName: String = "",
    val password: String = "",
    val emailAdmin: String = "",
    val isAddingRoom: Boolean = false,
    val currentRoom: String = "",
    val sortType: roomSortType = roomSortType.ROOM_NAME,

    //val privacyPolicyAccepted: Boolean = false,

    var roomNameError: Boolean = false,
    var passwordError: Boolean = false,
    var emailAdminError: Boolean = false
)