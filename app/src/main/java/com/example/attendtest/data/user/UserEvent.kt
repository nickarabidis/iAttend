package com.example.attendtest.data.user


import com.example.attendtest.database.user.User
import com.example.attendtest.database.user.userSortType

sealed interface UserEvent {
    data object SaveUser: UserEvent

    data class SetFirstName(val firstName: String): UserEvent
    data class SetLastName(val lastName: String): UserEvent
    data class SetEmail(val email: String): UserEvent
    data class SetPassword(val password: String): UserEvent

//    object ShowAddUserDialog: UserEvent
//    object HideAddUserDialog: UserEvent

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

    object RegisterButtonClicked: UserEvent
    object LoginButtonClicked: UserEvent

}