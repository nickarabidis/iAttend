package com.example.attendtest.navigation


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.attendtest.database.room.Room


sealed class Screen {

    data object SignUpNewScreen : Screen()
    data object LoginNewScreen : Screen()
    data object HomeNewScreen : Screen()

    data object FavoriteRoomScreen : Screen()

    data object SettingsScreen : Screen()

    data object TermsAndConditionsScreen : Screen()
    class RoomScreen(val room: Room) : Screen()

}


object AppRouter {

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUpNewScreen)

    fun navigateTo(destination : Screen){
        currentScreen.value = destination
    }


}