package com.example.attendtest.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen {

    data object SignUpNewScreen : Screen()
    data object LoginNewScreen : Screen()
    data object HomeNewScreen : Screen()
    data object TermsAndConditionsScreen : Screen()
    data object RoomScreen : Screen()

}


object AppRouter {

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUpNewScreen)

    fun navigateTo(destination : Screen){
        currentScreen.value = destination
    }


}