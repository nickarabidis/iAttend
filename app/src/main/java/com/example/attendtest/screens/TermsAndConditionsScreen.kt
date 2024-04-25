package com.example.attendtest.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.attendtest.R
import com.example.attendtest.components.HeadingTextComponent
import com.example.attendtest.components.NormalTextComponent
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen
import com.example.attendtest.navigation.SystemBackButtonHandler

@Composable
fun TermsAndConditionsScreen(){
    Surface(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
        .padding(16.dp)){

        Column {
            HeadingTextComponent(value = stringResource(id = R.string.terms_and_conditions_header))
            Spacer(modifier = Modifier.height(50.dp))
            HeadingTextComponent(value = stringResource(id = R.string.wip_terms))
            Spacer(modifier = Modifier.height(50.dp))
            NormalTextComponent(value = "iAttend 2024")
            Image(painterResource(id = R.drawable.iattend_logo), contentDescription = "Application Logo", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "Georgios Ntolias", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(text = "Nikolaos Arampidis", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }

    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.SignUpNewScreen)
    }

}

@Preview
@Composable
fun TermsAndConditionsScreenPreview(){
    TermsAndConditionsScreen()
}