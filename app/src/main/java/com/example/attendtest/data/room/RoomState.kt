package com.example.attendtest.data.room

import com.example.attendtest.database.room.Room
import com.example.attendtest.database.room.roomSortType
import com.example.attendtest.database.roomAndUser.RoomAndUser
import com.example.attendtest.database.roomAndUser.roomAndUserSortType


data class RoomState(
    val rooms: List<Room> = emptyList(),
    val roomName: String = "",
    val password: String = "",
    val emailAdmin: String = "",
    val emailId: String? = "",
    var id: Long = 0,
    val isAddingRoom: Boolean = false,
    val isEditingRoom: Boolean = false,
    val currentRoom: Room? = null,
    val sortType: roomSortType = roomSortType.ID,
    val isDone: Boolean = false,


    //usersInRooms
    val isAddingUserInRoom: Boolean = false,
    val emailOfUser: String = "",
    val isPresent: Boolean = false,
    val roomAndUsers: List<RoomAndUser> = emptyList(),
    val roomanduserSortType: roomAndUserSortType = roomAndUserSortType.USER_EMAIL,


    //val privacyPolicyAccepted: Boolean = false,

    var roomNameError: Boolean = false,
    var passwordError: Boolean = false,
    var emailAdminError: Boolean = false
)