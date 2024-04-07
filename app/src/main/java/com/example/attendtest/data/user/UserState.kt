package com.example.attendtest.data.user

import com.example.attendtest.database.user.User
import com.example.attendtest.database.user.userSortType


data class UserState (
    val users: List<User> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isAddingUser: Boolean = false,
    val currentUser: String = "",
    val sortType: userSortType = userSortType.FIRST_NAME,

//    var firstName: String = "",
//    var lastName: String = "",
//    var email: String = "",
//    var password: String = "",
    val privacyPolicyAccepted: Boolean = false,

    var firstNameError: Boolean = false,
    var lastNameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var privacyPolicyError: Boolean = false
)