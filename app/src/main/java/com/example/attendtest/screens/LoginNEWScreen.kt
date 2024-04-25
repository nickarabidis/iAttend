package com.example.attendtest.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.R
import com.example.attendtest.components.BackgroundCredits
import com.example.attendtest.components.BackgroundWithText
import com.example.attendtest.components.ButtonComponent
import com.example.attendtest.components.ClickableLoginTextComponent
import com.example.attendtest.components.DividerTextComponent
import com.example.attendtest.components.HeadingTextComponent
import com.example.attendtest.components.MyTextFieldComponent
import com.example.attendtest.components.NormalTextComponent
import com.example.attendtest.components.PasswordTextFieldComponent
import com.example.attendtest.components.UnderLinedTextComponent
import com.example.attendtest.data.user.UserEvent
import com.example.attendtest.data.user.UserViewModel
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen
import com.example.attendtest.navigation.SystemBackButtonHandler


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginNewScreen(userNewViewModel: UserViewModel = viewModel()){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center)
    {
        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ){

//                NormalTextComponent(value = stringResource(id = R.string.sign_in))
//                HeadingTextComponent(value = stringResource(id = R.string.welcome))
                BackgroundWithText(
                    text1 = "iAttend",
                    text2 = stringResource(id = R.string.motto),
                    text3 = stringResource(id = R.string.welcome)
                )

                Spacer(modifier = Modifier.height(40.dp))

                MyTextFieldComponent(labelValue = stringResource(id = R.string.email),
                    onTextSelected = {
                        userNewViewModel.onEvent(UserEvent.EmailChanged(it))
                    },
                    errorStatus = userNewViewModel._state.value.emailError
                )
                PasswordTextFieldComponent(labelValue = stringResource(id = R.string.password),
                    onTextSelected = {
                        userNewViewModel.onEvent(UserEvent.PasswordChanged(it))
                    },
                    errorStatus = userNewViewModel._state.value.passwordError
                )

                Spacer(modifier = Modifier.height(40.dp))

                UnderLinedTextComponent(value = stringResource(id = R.string.forgot_password))

                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(value = stringResource(id = R.string.sign_in),
                    onButtonClicked ={
                        userNewViewModel.onEvent(UserEvent.LoginButtonClicked)
                    },
                    isEnabled = userNewViewModel.allValidationsPassed.value
                )

                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent()

                ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                    AppRouter.navigateTo(Screen.SignUpNewScreen)
                })

                Spacer(modifier = Modifier.height(80.dp))

                BackgroundCredits(text1 = "© iAttend 2024", text2 = "Georgios Ntolias, Nikolaos Arampidis")
            }

        }
        if(userNewViewModel.InProgress.value) {
            CircularProgressIndicator()
        }
    }

    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.SignUpNewScreen)
    }

}

@Preview
@Composable
fun LoginNEWScreenPreview(){
    LoginNewScreen()
}