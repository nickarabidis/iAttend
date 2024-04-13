package com.example.attendtest.screens


import android.annotation.SuppressLint
import android.nfc.Tag
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.attendtest.database.room.roomSortType
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.attendtest.data.room.RoomViewModel
import com.example.attendtest.database.roomAndUser.RoomAndUserDao
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen
import com.example.attendtest.navigation.SystemBackButtonHandler


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNewScreen(state: RoomState,
                  onEvent: (RoomEvent) -> Unit,
                  userNewViewModel: UserViewModel = viewModel(),
                  roomNewViewModel: RoomViewModel = viewModel()){
    //val snackbarHostState  = remember { SnackbarHostState() }
    //val scope = rememberCoroutineScope()
    //val scaffoldState = rememberScaffoldState()
    val roomAndUserDao: RoomAndUserDao
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

                        Scaffold(
                            floatingActionButton = {
                                FloatingActionButton(onClick = {
                                    onEvent(RoomEvent.ShowAddRoomDialog(userNewViewModel.emailId))
                                }){
                                    Icon(imageVector = Icons.Default.Add,
                                        contentDescription = stringResource(id = R.string.add_room)
                                    )
                                }
                            },
                            modifier = Modifier.padding(16.dp)
                        ){
                                padding ->

                            Log.d(userNewViewModel.TAG,"state.isAddingRoom= ${state.isAddingRoom}")
                            Log.d(userNewViewModel.TAG,"state.isEditingRoom= ${state.isEditingRoom}")
                            if(state.isAddingRoom){
                                AddRoomDialog(state = state, onEvent = onEvent)
                            } else if(state.isEditingRoom){
                                EditRoomDialog(state = state, onEvent = onEvent)
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
                                        roomSortType.entries.forEach { sortType ->
                                            Row(
                                                modifier = Modifier
                                                    .clickable{
                                                        onEvent(RoomEvent.SortRooms(sortType))
                                                    },
                                                verticalAlignment = Alignment.CenterVertically
                                            ){
                                                RadioButton(
                                                    selected = state.sortType == sortType,
                                                    onClick = { RoomEvent.SortRooms(sortType) }
                                                )
                                                Text(text = sortType.name)
                                            }
                                        }
                                    }
                                }//&& state.emailOfUser ==
                                onEvent(RoomEvent.GetRoomIdFromUserEmail(userNewViewModel.emailId, state.rooms))
                                items(state.rooms.filter {room ->
                                    val currentRoomIds = state.currentRoomIds
                                    val emailAdminMatches = room.emailAdmin == userNewViewModel.emailId
                                    val roomIdMatches = currentRoomIds.contains(room.id)
                                    emailAdminMatches || roomIdMatches
                                    //it.emailAdmin == userNewViewModel.emailId || state.currentRoomId == it.id
                                }){ room ->

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
                                                        AppRouter.navigateTo(Screen.RoomScreen(room))
                                                    }
                                                }
                                        ) {
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = "${room.roomName} ${room.password}",
                                                    fontSize = 20.sp
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
                                                if (room.emailAdmin == userNewViewModel.emailId) {
                                                    Text(
                                                        text = "Visibility: ${room.isVisible}",
                                                        fontSize = 20.sp
                                                    )
                                                    Text(
                                                        text = "Password Needed: ${room.passwordNeeded}",
                                                        fontSize = 20.sp
                                                    )
                                                }
                                            }

                                            onEvent(RoomEvent.GetEmailFromRoom(userNewViewModel.emailId, state.rooms))

                                            //CHECK IF ADMIN EMAIL IS THE SAME WITH USER
                                            if (room.emailAdmin != userNewViewModel.emailId){
                                                IconButton(onClick = {
                                                    Log.d("press attendance", "hi!")
                                                    onEvent(RoomEvent.isPresent(room, userNewViewModel.emailId))
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


//
//@Preview
//@Composable
//fun HomeNewScreenPreview(){
//    HomeNewScreen(state = , onEvent = )
//}