package com.example.attendtest.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.R
import com.example.attendtest.components.AppToolbar
import com.example.attendtest.components.NavigationDrawerBody
import com.example.attendtest.components.NavigationDrawerHeader
import com.example.attendtest.data.user.UserViewModel


import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNewScreen(userNewViewModel: UserViewModel = viewModel() ){
    //val snackbarHostState  = remember { SnackbarHostState() }
    //val scope = rememberCoroutineScope()
    //val scaffoldState = rememberScaffoldState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    userNewViewModel.getUserData()

    Log.d(userNewViewModel.TAG,"userNewViewModel.emailId= ${userNewViewModel.emailId}")

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
            ) {
                NavigationDrawerHeader(userNewViewModel.emailId)
                NavigationDrawerBody(navigationDrawerItems = userNewViewModel.navigationItemsList,
                    onNavigationItemClicked = {
                        Log.d("ComingHere", "inside_onNavigationItemClicked")
                        Log.d("ComingHere", "${it.itemId} ${it.title}")
                    })
            }

        },
        //drawerContainerColor = Color.Transparent,
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,

        content = {
            Scaffold(
                //snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    AppToolbar(
                        toolbarTitle = stringResource(id = R.string.home),
                        logoutButtonClicked = {
                            userNewViewModel.logoutDatabase()
                        },
                        navigationIconClicked = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    )
                }
            ) {paddingValues ->

                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(paddingValues)
                ){
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ){
                        //HeadingTextComponent(value = stringResource(R.string.home))

//                ButtonComponent(value = stringResource(R.string.logout), onButtonClicked = {
//                    signupViewModel.logout()
//                },
//                    isEnabled = true
//                )
                    }
                }
            }
        }
    )

}



@Preview
@Composable
fun HomeNewScreenPreview(){
    HomeNewScreen()
}