package com.example.attendtest.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.R
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.data.room.RoomState
import com.example.attendtest.data.user.UserViewModel
import com.example.attendtest.database.room.RoomSortType
import kotlinx.coroutines.launch
import androidx.compose.ui.res.vectorResource
import com.example.attendtest.components.AppToolbar
import com.example.attendtest.components.NavigationDrawerBody
import com.example.attendtest.components.NavigationDrawerHeader
import com.example.attendtest.data.room.RoomViewModel
import com.example.attendtest.database.roomAndUser.RoomAndUserDao
import com.example.attendtest.database.user.UserDao
import com.example.attendtest.database.userSettings.Languages
import com.example.attendtest.database.userSettings.Themes
import com.example.attendtest.database.userSettings.UserSettingsType
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen
import com.example.attendtest.navigation.SystemBackButtonHandler
import com.example.iattend.ui.theme.DarkPrimary
import com.example.iattend.ui.theme.DrawerPrimary
import com.example.iattend.ui.theme.WhiteColor


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(state: RoomState,
                       onEvent: (RoomEvent) -> Unit,
                       userNewViewModel: UserViewModel = viewModel(),
                       roomNewViewModel: RoomViewModel = viewModel()
) {
    val roomAndUserDao: RoomAndUserDao
    val userDao: UserDao
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    userNewViewModel.getUserData()

    Log.d(userNewViewModel.TAG, "userNewViewModel.emailId= ${userNewViewModel.emailId}")

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight(),
                drawerContainerColor = DrawerPrimary
            ) {
                NavigationDrawerHeader(userNewViewModel.emailId)
                NavigationDrawerBody(navigationDrawerItems = userNewViewModel.navigationItemsList,
                    onNavigationItemClicked = {
                        when (it.titleResId) {
                            R.string.home -> {
                                onEvent(RoomEvent.SortRooms(RoomSortType.ID))
                                AppRouter.navigateTo(Screen.HomeNewScreen)
                            }
                            R.string.favorites -> {
                                onEvent(RoomEvent.SortRooms(RoomSortType.FAVORITES))
                                AppRouter.navigateTo(Screen.FavoriteRoomScreen)
                            }
                        }
                        Log.d("ComingHere", "inside_onNavigationItemClicked")
                        Log.d("ComingHere", "${it.itemId} ${it.titleResId}")
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
                        toolbarTitle = stringResource(id = R.string.settings),
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

                        Scaffold(
                            floatingActionButton = {
                                FloatingActionButton(onClick = {
                                    onEvent(RoomEvent.SortRooms(RoomSortType.ID))
                                    AppRouter.navigateTo(Screen.HomeNewScreen)
                                }){
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(DarkPrimary)
                                            .padding(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Home,
                                            contentDescription = stringResource(id = R.string.back_to_home),
                                            tint = WhiteColor
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = stringResource(id = R.string.back_to_home), color = WhiteColor)
                                    }
                                }
                            },
                            modifier = Modifier.padding(16.dp)
                        ){
                            padding ->
                            LazyColumn(
                                contentPadding = padding,
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ){
                                item {
                                    UserSettingsType.entries.forEach { settingType ->
                                        val settingFlair = when (settingType.name) {
                                            UserSettingsType.LANGUAGE.toString() -> {
                                                stringResource(id = R.string.settings_language_desc)
                                            }
                                            UserSettingsType.THEME.toString() -> {
                                                stringResource(id = R.string.settings_theme_desc)
                                            }
                                            else -> {"settingFlair: Error detecting settingType"}
                                        }

                                        val settingName = when (settingType.name) {
                                            UserSettingsType.LANGUAGE.toString() -> {
                                                stringResource(id = R.string.setting_language)
                                            }
                                            UserSettingsType.THEME.toString() -> {
                                                stringResource(id = R.string.setting_theme)
                                            }
                                            else -> {"settingName: Error detecting settingType"}
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = settingName,
                                                    fontSize = 22.sp
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.setting_desc) +
                                                            ": " +
                                                            settingFlair +
                                                            ".",
                                                    fontSize = 12.sp
                                                )
                                                // Visibility info for admin
    //                                                if (room.emailAdmin == userNewViewModel.emailId) {
    //                                                    Text(
    //                                                        text = "Visibility: ${room.isVisible}",
    //                                                        fontSize = 20.sp
    //                                                    )
    //                                                    Text(
    //                                                        text = "Password Needed: ${room.passwordNeeded}",
    //                                                        fontSize = 20.sp
    //                                                    )
    //                                                }
                                            }

                                            //onEvent(RoomEvent.GetEmailFromRoom(userNewViewModel.emailId, state.rooms))
                                            //onEvent(RoomEvent.GetFavoriteRoomIdFromUserEmail(userNewViewModel.emailId, state.rooms))

                                            if (settingType == UserSettingsType.LANGUAGE) {
                                                Languages.entries.forEach { language ->
                                                    Row(
                                                        modifier = Modifier
                                                            .clickable{
                                                                onEvent(RoomEvent.SetLanguage(language))
                                                                onEvent(RoomEvent.UpdateLanguage(userNewViewModel.emailId!!, language))
                                                            },
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ){
                                                        RadioButton(
                                                            selected = state.languageChosen == language,
                                                            onClick = {
                                                                onEvent(RoomEvent.SetLanguage(language))
                                                                onEvent(RoomEvent.UpdateLanguage(userNewViewModel.emailId!!, language))
                                                            }
                                                        )
                                                        Log.d("ChangeLanguageTo", state.languageChosen.toString())
                                                        // Custom sort names
                                                        val langIcon = when (language.name) {
                                                            Languages.EN.toString() -> R.drawable.en_flag
                                                            Languages.GR.toString() -> R.drawable.gr_flag
                                                            else -> Log.e("Lang Error: ", "Can't find asked Language")
                                                        }

                                                        val langDesc = when (language.name) {
                                                            Languages.EN.toString() -> R.string.en
                                                            Languages.GR.toString() -> R.string.gr
                                                            else -> Log.e("Lang Error: ", "Can't find asked Language")
                                                        }

                                                        Icon(
                                                            imageVector = ImageVector.vectorResource(id = langIcon),
                                                            contentDescription = stringResource(id = langDesc),
                                                            tint = Color.Unspecified
                                                        )
                                                    }
                                                }
                                            } else if (settingType == UserSettingsType.THEME) {
                                                Themes.entries.forEach { theme ->
                                                    Row(
                                                        modifier = Modifier
                                                            .clickable {
                                                                //onEvent(RoomEvent.SetTheme(theme))
                                                                onEvent(RoomEvent.UpdateTheme(userNewViewModel.emailId!!, theme))
                                                            },
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        RadioButton(
                                                            selected = state.themeChosen == theme,
                                                            onClick = {
                                                                //onEvent(RoomEvent.SetTheme(theme))
                                                                onEvent(RoomEvent.UpdateTheme(userNewViewModel.emailId!!, theme))
                                                            }
                                                        )

                                                        Log.d("ChangeThemeTo", state.themeChosen.toString())

                                                        // Custom sort names
                                                        val themeText = when (theme.name) {
                                                            Themes.LIGHT.toString() -> R.string.light
                                                            Themes.DARK.toString() -> R.string.dark
                                                            else -> Log.e("Theme Error: ", "Can't find asked Theme")
                                                        }

                                                        Text(text = stringResource(id = themeText))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )

    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.LoginNewScreen)
    }
}