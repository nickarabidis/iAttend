package com.example.attendtest.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.data.room.RoomState
import com.example.attendtest.database.room.RoomSortType
import com.example.attendtest.screens.SignUpNewScreen
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen
import com.example.attendtest.screens.FavoriteRoomScreen
import com.example.attendtest.screens.HomeNewScreen
import com.example.attendtest.screens.LoginNewScreen
import com.example.attendtest.screens.RoomScreen
import com.example.attendtest.screens.SettingsScreen
import com.example.attendtest.screens.TermsAndConditionsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(state: RoomState,
        onEvent: (RoomEvent) -> Unit){

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ){


        Crossfade(targetState = AppRouter.currentScreen) { currentState->
            when(currentState.value){
                is Screen.SignUpNewScreen -> {
                    SignUpNewScreen()
                }
                is Screen.LoginNewScreen -> {
                    LoginNewScreen()
                }
                is Screen.HomeNewScreen -> {
                    HomeNewScreen(state, onEvent)
                }
                is Screen.TermsAndConditionsScreen -> {
                    TermsAndConditionsScreen()
                }
                is Screen.RoomScreen ->{
                    RoomScreen((currentState.value as Screen.RoomScreen).room, state, onEvent)
                }
                is Screen.FavoriteRoomScreen -> {
//                    onEvent(RoomEvent.SortRooms(RoomSortType.FAVORITES))
                    FavoriteRoomScreen(state, onEvent)
                }
                is Screen.SettingsScreen -> {
                    SettingsScreen(state, onEvent)
                }
            }
        }
    }
}