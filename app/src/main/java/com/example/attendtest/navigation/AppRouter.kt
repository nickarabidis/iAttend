package com.example.attendtest.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen {

    object SignUpNewScreen : Screen()
    object LoginNewScreen : Screen()
    object HomeNewScreen : Screen()
    object TermsAndConditionsScreen : Screen()
    object RoomScreen : Screen()

}


object AppRouter {

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUpNewScreen)

    fun navigateTo(destination : Screen){
        currentScreen.value = destination
    }


}