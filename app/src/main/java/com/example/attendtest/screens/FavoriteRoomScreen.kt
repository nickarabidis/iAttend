package com.example.attendtest.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.R
import com.example.attendtest.components.AddRoomDialog
import com.example.attendtest.components.AppToolbar
import com.example.attendtest.components.EditRoomDialog
import com.example.attendtest.components.NavigationDrawerBody
import com.example.attendtest.components.NavigationDrawerHeader
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.data.room.RoomState
import com.example.attendtest.data.user.UserViewModel
import com.example.attendtest.database.room.RoomSortType
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.attendtest.components.PasswordNeededDialog
import com.example.attendtest.data.room.RoomViewModel
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.roomAndFavorites.RoomAndFavorites
import com.example.attendtest.database.roomAndUser.RoomAndUser
import com.example.attendtest.database.roomAndUser.RoomAndUserDao
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen
import com.example.attendtest.navigation.SystemBackButtonHandler
import com.example.iattend.ui.theme.DarkPrimary
import com.example.iattend.ui.theme.DrawerPrimary
import com.example.iattend.ui.theme.WhiteColor


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteRoomScreen(state: RoomState,
                       onEvent: (RoomEvent) -> Unit,
                       userNewViewModel: UserViewModel = viewModel(),
                       roomNewViewModel: RoomViewModel = viewModel()
){
    //val snackbarHostState  = remember { SnackbarHostState() }
    //val scope = rememberCoroutineScope()
    //val scaffoldState = rememberScaffoldState()
    val roomAndUserDao: RoomAndUserDao
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    userNewViewModel.getUserData()

    // Collect the favorite room IDs from RoomViewModel
//    val favoriteRoomIds by remember { roomNewViewModel.loadFavoriteRoomIds(LocalContext.current) }.collectAsState(emptyList())

    Log.d(userNewViewModel.TAG,"userNewViewModel.emailId= ${userNewViewModel.emailId}")

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
                            R.string.settings -> {
                                AppRouter.navigateTo(Screen.SettingsScreen)
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
                        toolbarTitle = stringResource(id = R.string.favorites),
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

                            Log.d(userNewViewModel.TAG,"state.isAddingRoom= ${state.isAddingRoom}")
                            Log.d(userNewViewModel.TAG,"state.isEditingRoom= ${state.isEditingRoom}")
                            Log.d(userNewViewModel.TAG,"state.isEditingRoom= ${state.isPasswordNeeded}")
                            if(state.isAddingRoom){
                                AddRoomDialog(state = state, onEvent = onEvent)
                            } else if(state.isEditingRoom){
                                EditRoomDialog(state = state, onEvent = onEvent)
                            } else if(state.isPasswordNeeded){
                                PasswordNeededDialog(state = state, onEvent = onEvent)
                            }

                            LazyColumn(
                                contentPadding = padding,
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ){
                                item{
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState()),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(
                                            text = stringResource(id = R.string.sort) + ":",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Row(
                                            modifier = Modifier
                                                .clickable{
                                                    onEvent(RoomEvent.SortRooms(RoomSortType.FAVORITES))
                                                },
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            RadioButton(
                                                selected = state.sortType == RoomSortType.FAVORITES,
                                                onClick = { }
                                            )

                                            Text(text = stringResource(id = R.string.sort_favorites))
                                        }

                                    }
                                }//&& state.emailOfUser ==
                                onEvent(RoomEvent.GetFavoriteRoomIdFromUserEmail(userNewViewModel.emailId, state.rooms))
                                items(state.rooms.filter {room ->
                                    val currentFavoriteRoomIds = state.currentFavoriteRoomIds
                                    val emailAdminMatches = room.emailAdmin == userNewViewModel.emailId
                                    val roomIdMatches = currentFavoriteRoomIds.contains(room.id)
                                    emailAdminMatches || roomIdMatches
                                    //it.emailAdmin == userNewViewModel.emailId || state.currentRoomId == it.id
                                }.distinctBy { it.id }){ room ->

                                    /*
                                        visibility check
                                        - if user is admin, he can see the rooms either way
                                        - if he is not then the room has to be visible
                                    */
                                    if (userNewViewModel.emailId == room.emailAdmin || room.isVisible) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    if (userNewViewModel.emailId == room.emailAdmin) {
                                                        onEvent(RoomEvent.SortRooms(RoomSortType.ID))
                                                        AppRouter.navigateTo(Screen.RoomScreen(room))
                                                    }
                                                }
                                        ) {
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = room.roomName,
                                                    fontSize = 22.sp
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.admin) +
                                                            ": " +
                                                            room.emailAdmin +
                                                            ",",
                                                    fontSize = 12.sp
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.room_id) +
                                                            ": " +
                                                            room.id,
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

                                            onEvent(RoomEvent.GetEmailFromRoom(userNewViewModel.emailId, state.rooms))
                                            //onEvent(RoomEvent.GetFavoriteRoomIdFromUserEmail(userNewViewModel.emailId, state.rooms))

                                            IconButton(onClick = {
                                                scope.launch {
                                                    if (!roomNewViewModel.checkFavoriteRoom(room.id, userNewViewModel.emailId!!)) {
                                                        Log.d("Pressed favorite", "Favorite room: ${room.roomName}")
                                                        onEvent(RoomEvent.FavoriteRoom(room, userNewViewModel.emailId))
                                                    } else {
                                                        Log.d("Pressed unfavorite", "Unfavorite room: ${room.roomName}")
                                                        onEvent(RoomEvent.UnfavoriteRoom(room, userNewViewModel.emailId))
                                                    }
                                                }
                                            }) {
                                                FavoriteAndUnfavoriteIcon(viewModel = roomNewViewModel, userEmail = userNewViewModel.emailId!!, room)
//                                                if (favoriteRoomIds.contains(room.id)){
//                                                    Icon(
//                                                        imageVector = Icons.Default.Favorite,
//                                                        contentDescription = stringResource(id = R.string.add_favorites)
//                                                    )
//                                                }else{
//                                                    Icon(
//                                                        imageVector = Icons.Default.FavoriteBorder,
//                                                        contentDescription = stringResource(id = R.string.remove_favorites)
//                                                    )
//                                                }
                                            }


                                            //CHECK IF ADMIN EMAIL IS THE SAME WITH USER
                                            if (room.emailAdmin != userNewViewModel.emailId){
                                                IconButton(onClick = {
                                                    Log.d("press attendance", "hi!")
                                                    scope.launch {
                                                        Log.d(roomNewViewModel.TAG,"Home room.id:  ${room.id}")

                                                        val presentNeeded = roomNewViewModel.getPresentNeededFromRoom(room)
                                                        val passwordNeeded = roomNewViewModel.getPasswordNeededFromRoom(room)

                                                        //onEvent(RoomEvent.GetPasswordNeededFromRoom(room))
                                                        Log.d(roomNewViewModel.TAG,"Home presentNeeded:  ${presentNeeded}")
                                                        Log.d(roomNewViewModel.TAG,"Home passwordNeeded:  ${passwordNeeded}")
                                                        if (presentNeeded == false){
                                                            if (passwordNeeded == true){
                                                                Log.d(roomNewViewModel.TAG,"Home if passwordNeeded:  ${passwordNeeded}")
                                                                onEvent(RoomEvent.ShowPasswordNeededDialog(room))
                                                            }else{
                                                                Log.d(userNewViewModel.TAG,"Home else passwordNeeded:  ${passwordNeeded}")
                                                                onEvent(RoomEvent.isPresent(room, userNewViewModel.emailId))
                                                            }
                                                        }
                                                    }
                                                }) {
                                                    val currentPresentRoomIds = state.currentPresentRoomIds
                                                    val roomIdPresent = currentPresentRoomIds.contains(room.id)
                                                    if (roomIdPresent){
                                                        Icon(
                                                            painter = painterResource(R.drawable.done),
                                                            contentDescription = "Done Attendance"
                                                        )
                                                    }else{
                                                        Icon(
                                                            painter = painterResource(R.drawable.done_outline),
                                                            contentDescription = "Not Done Attendance"
                                                        )
                                                    }
//                                                if(state.isDone && state.currentRoom == room) {
//                                                    Icon(
//                                                        painter = painterResource(R.drawable.done),
//                                                        contentDescription = "Done Attendance"
//                                                    )
//                                                }else{
//                                                    Icon(
//                                                        painter = painterResource(R.drawable.done_outline),
//                                                        contentDescription = "Not Done Attendance"
//                                                    )
//
//                                                }
                                                }
                                            }

                                            if (userNewViewModel.emailId == room.emailAdmin) {
                                                IconButton(onClick = {
                                                    Log.d("Pressed edit", "Editing room: ${room.roomName}")
                                                    onEvent(RoomEvent.ShowEditRoomDialog(room))
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = stringResource(id = R.string.edit_room)
                                                    )
                                                }
                                                IconButton(onClick = {
                                                    Log.d("Pressed delete", "Deleting room: ${room.roomName}")
                                                    onEvent(RoomEvent.DeleteRoom(room))
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = stringResource(id = R.string.delete_room)
                                                    )
                                                }
                                            }

                                        }

                                    }


                                }
                            }
                        }

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

    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.LoginNewScreen)
    }

}