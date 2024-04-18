package com.example.attendtest.data.room

import com.example.attendtest.database.room.Room
import com.example.attendtest.database.room.RoomSortType
import com.example.attendtest.database.room.RoomVisibilityType
import com.example.attendtest.database.roomAndUser.RoomAndUser
import com.example.attendtest.database.roomAndUser.RoomAndUserSortType


sealed interface RoomEvent {
    data object SaveRoom: RoomEvent
    data object SaveEdits: RoomEvent

    //UserInRoom
    data object SaveUserInRoom: RoomEvent

    data class SetRoomName(val roomName: String): RoomEvent
    data class SetPassword(val password: String): RoomEvent
    data class SetEmailAdmin(val emailAdmin: String): RoomEvent
//    data class SetVisibility(val ): RoomEvent
    data class SetIsVisible(val isVisible: Boolean): RoomEvent
    data class SetPasswordNeeded(val passwordNeeded: Boolean): RoomEvent

    data class SetVisibilityType( val visibilityType: RoomVisibilityType) : RoomEvent

    data class GetPasswordNeededFromRoom(val room: Room) : RoomEvent

//    data class SetVisibleToggle(val isVisible: Boolean, val passwordNeeded: Boolean): RoomEvent

    data class GetFirstName(val userEmail: String): RoomEvent

    data class GetLastName(val userEmail: String): RoomEvent

    data class FavoriteRoom(val room: Room) : RoomEvent

    //usersInRoom
    data class SetEmailOfUser(val emailOfUser: String): RoomEvent

    data class ShowAddRoomDialog(val emailId: String?): RoomEvent
    data object HideAddRoomDialog: RoomEvent
    data class ShowEditRoomDialog(val room: Room): RoomEvent
    data object HideEditRoomDialog: RoomEvent

    //Password Needed
    data class ShowPasswordNeededDialog(val room: Room): RoomEvent
    data object HidePasswordNeededDialog: RoomEvent
    data class SetPasswordToEnter(val passwordToEnter: String): RoomEvent
    data class CheckPassword(val emailId: String?): RoomEvent

    data class isDone(val room: Room): RoomEvent
    data class isPresent(val room: Room, val emailId: String?): RoomEvent

    data class GetRoomIdFromUserEmail(val emailId: String?, val rooms: List<Room>): RoomEvent

    data class GetEmailFromRoom(val emailId: String?, val rooms: List<Room>): RoomEvent
    //data class RoomIdFetched(val roomId: Long?) : RoomEvent

    //usersInRoom
    data class ShowAddUserInRoomDialog(val room: Room) : RoomEvent
    data object HideAddUserInRoomDialog: RoomEvent

    data class SortRooms(val sortType: RoomSortType): RoomEvent

    data class ToggleVisibility(val visibilityType: RoomVisibilityType): RoomEvent

    data class SortRoomsAndUsers(val sortTypeRoomAndUser: RoomAndUserSortType): RoomEvent
    data class DeleteRoom(val room: Room): RoomEvent

    data class DeleteUserFromRoom(val roomAndUser: RoomAndUser): RoomEvent

    data class GetRoomName(val roomName: String): RoomEvent
    data class GetPassword(val roomName: String): RoomEvent
    data class GetEmailAdmin(val roomName: String): RoomEvent


    data class RoomNameChanged(val roomName: String) : RoomEvent
    data class PasswordChanged(val password: String) : RoomEvent
    data class EmailAdminChanged(val emailAdmin: String) : RoomEvent


    data class ClickedRoom(val room: Room) : RoomEvent


    //data class PrivacyPolicyCheckBoxClicked(val status: Boolean) : RoomEvent

    //object RegisterButtonClicked: RoomEvent
    //object LoginButtonClicked: RoomEvent

    // unused
//    data object AddRoomButtonClicked: RoomEvent

}