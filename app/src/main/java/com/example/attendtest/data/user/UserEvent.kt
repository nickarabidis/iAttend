package com.example.attendtest.data.user


import com.example.attendtest.database.user.User
import com.example.attendtest.database.user.userSortType
import com.example.attendtest.database.userSettings.Languages
import com.example.attendtest.database.userSettings.Themes

sealed interface UserEvent {
    data object SaveUser: UserEvent

    data object SaveSettings: UserEvent

    data class SetFirstName(val firstName: String): UserEvent
    data class SetLastName(val lastName: String): UserEvent
    data class SetEmail(val email: String): UserEvent
    data class SetPassword(val password: String): UserEvent

    data class SetLanguage(val language: Languages): UserEvent

    data class SetTheme(val theme: Themes): UserEvent

    data class SetUserTheme(val email: String, val theme: Themes)

//    object ShowAddRoomDialog: UserEvent
//    object HideAddRoomDialog: UserEvent

    data class SortUsers(val sortType: userSortType): UserEvent
    data class DeleteUser(val user: User): UserEvent


    data class GetEmail(val email: String): UserEvent
    data class GetPassword(val email: String): UserEvent
    data class GetFirstName(val email: String): UserEvent
    data class GetLastName(val email: String): UserEvent

    data class FirstNameChanged(val firstName: String) : UserEvent
    data class LastNameChanged(val lastName: String) : UserEvent
    data class EmailChanged(val email: String) : UserEvent
    data class PasswordChanged(val password: String) : UserEvent

    data class PrivacyPolicyCheckBoxClicked(val status: Boolean) : UserEvent

    data object RegisterButtonClicked: UserEvent
    data object LoginButtonClicked: UserEvent

}