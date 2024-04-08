package com.example.attendtest.data.room

import com.example.attendtest.database.room.Room
import com.example.attendtest.database.room.roomSortType


sealed interface RoomEvent {
    object SaveRoom: RoomEvent

    data class SetRoomName(val roomName: String): RoomEvent
    data class SetPassword(val password: String): RoomEvent
    data class SetEmailAdmin(val emailAdmin: String): RoomEvent

    object ShowDialog: RoomEvent
    object HideDialog: RoomEvent

    data class SortRooms(val sortType: roomSortType): RoomEvent
    data class DeleteRoom(val room: Room): RoomEvent

    data class GetRoomName(val roomName: String): RoomEvent
    data class GetPassword(val roomName: String): RoomEvent
    data class GetEmailAdmin(val roomName: String): RoomEvent


    data class RoomNameChanged(val roomName: String) : RoomEvent
    data class PasswordChanged(val password: String) : RoomEvent
    data class EmailAdminChanged(val emailAdmin: String) : RoomEvent

    //data class PrivacyPolicyCheckBoxClicked(val status: Boolean) : RoomEvent

    //object RegisterButtonClicked: RoomEvent
    //object LoginButtonClicked: RoomEvent

    object AddRoomButtonClicked: RoomEvent

}