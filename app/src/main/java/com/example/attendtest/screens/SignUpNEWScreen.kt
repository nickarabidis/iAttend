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
import com.example.attendtest.components.ButtonComponent
import com.example.attendtest.components.CheckboxComponent
import com.example.attendtest.components.ClickableLoginTextComponent
import com.example.attendtest.components.DividerTextComponent
import com.example.attendtest.components.HeadingTextComponent
import com.example.attendtest.components.MyTextFieldComponent
import com.example.attendtest.components.NormalTextComponent
import com.example.attendtest.components.PasswordTextFieldComponent
import com.example.attendtest.data.user.UserEvent
import com.example.attendtest.data.user.UserViewModel
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen


@SuppressLint("StateFlowValueCalledInComposition")
@ExperimentalMaterial3Api
@Composable
fun SignUpNewScreen(UserViewModel: UserViewModel = viewModel()){

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center)
    {

        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ){
            Column(
                modifier = Modifier.fillMaxSize()
            ){
                NormalTextComponent(value = stringResource(id = R.string.hello))
                HeadingTextComponent(value = stringResource(id = R.string.create_account))

                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.first_name),
                    onTextSelected = {
                        UserViewModel.onEvent(UserEvent.FirstNameChanged(it))
                    },
                    errorStatus = UserViewModel._state.value.firstNameError
                )
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.last_name),
                    onTextSelected = {
                        UserViewModel.onEvent(UserEvent.LastNameChanged(it))
                    },
                    errorStatus = UserViewModel._state.value.lastNameError
                )
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    onTextSelected = {
                        UserViewModel.onEvent(UserEvent.EmailChanged(it))
                    },
                    errorStatus = UserViewModel._state.value.emailError
                )
                PasswordTextFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    onTextSelected = {
                        UserViewModel.onEvent(UserEvent.PasswordChanged(it))
                    },
                    errorStatus = UserViewModel._state.value.passwordError
                )

                CheckboxComponent(value = stringResource(id = R.string.terms_and_conditions),
                    onTextSelected = {
                        AppRouter.navigateTo(Screen.TermsAndConditionsScreen)
                    },
                    onCheckedChange = {
                        UserViewModel.onEvent(UserEvent.PrivacyPolicyCheckBoxClicked(it))
                    }
                )

                Spacer(modifier =Modifier.height(80.dp))

                ButtonComponent(value = stringResource(id = R.string.register),
                    onButtonClicked ={
                        UserViewModel.onEvent(UserEvent.RegisterButtonClicked)
                    },
                    isEnabled = UserViewModel.allValidationsPassed.value
                )

                DividerTextComponent()

                ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
                    AppRouter.navigateTo(Screen.LoginNewScreen)
                })
            }
        }

        if(UserViewModel.InProgress.value){
            CircularProgressIndicator()
        }

    }


}





@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultPreviewOfSignUpNewScreen(){
    SignUpNewScreen()
}