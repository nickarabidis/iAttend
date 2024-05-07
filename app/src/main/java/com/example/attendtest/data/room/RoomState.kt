package com.example.attendtest.data.room

import com.example.attendtest.database.room.Room
import com.example.attendtest.database.room.RoomSortType
import com.example.attendtest.database.room.RoomVisibilityType
import com.example.attendtest.database.roomAndFavorites.RoomAndFavorites
import com.example.attendtest.database.roomAndUser.RoomAndUser
import com.example.attendtest.database.roomAndUser.RoomAndUserSortType
import com.example.attendtest.database.userSettings.Languages
import com.example.attendtest.database.userSettings.Themes
import com.example.attendtest.database.userSettings.UserSettings
import java.util.Date


data class RoomState(
    var languageChosen: Languages = Languages.EN,
    var themeChosen: Themes = Themes.LIGHT,
    val rooms: List<Room> = emptyList(),
    val roomName: String = "",
    val password: String = "",
    val emailAdmin: String = "",
    val emailId: String? = "",
    var id: Long = 0,
    val isAddingRoom: Boolean = false,
    val isEditingRoom: Boolean = false,
    val currentRoom: Room? = null,
    val currentSettings: UserSettings? = null,
    val sortType: RoomSortType = RoomSortType.ID,
    val isDone: Boolean = false,

    val currentUser: String = "",

    // visibility
    var isVisible: Boolean = true,
    var visibilityType: RoomVisibilityType = RoomVisibilityType.VISIBLE,

    // isVisible -> true
    val isPasswordNeeded: Boolean = false,
    var passwordNeeded: Boolean = false,
    var passwordToEnter: String = "",
    var validPassword: Boolean = false,

    //usersInRooms
    val isAddingUserInRoom: Boolean = false,
    val emailOfUser: String = "",
    val isPresent: Boolean = false,
    val userFirstName: String = "",
    val userLastName: String = "",
    val presentDate: Date? = null,
    val roomAndUsers: List<RoomAndUser> = emptyList(),
    val roomanduserSortType: RoomAndUserSortType = RoomAndUserSortType.USER_EMAIL,
    val currentRoomIds: List<Long> = emptyList(),
    val currentPresentRoomIds: List<Long> = emptyList(),

    val currentFavoriteRoomIds: List<Long> = emptyList(),
    val isFavorite: Boolean = false,

    //val privacyPolicyAccepted: Boolean = false,

    var roomNameError: Boolean = false,
    var passwordError: Boolean = false,
    var emailAdminError: Boolean = false
)