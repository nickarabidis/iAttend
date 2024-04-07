package com.example.attendtest.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.attendtest.screens.SignUpNewScreen
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen
import com.example.attendtest.screens.HomeNewScreen
import com.example.attendtest.screens.LoginNewScreen
import com.example.attendtest.screens.TermsAndConditionsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(){

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
                    HomeNewScreen()
                }
                is Screen.TermsAndConditionsScreen -> {
                    TermsAndConditionsScreen()
                }


                Screen.RoomScreen -> TODO()


            }
        }
    }
}