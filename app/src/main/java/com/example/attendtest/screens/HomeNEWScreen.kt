package com.example.attendtest.screens


import android.annotation.SuppressLint
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

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNewScreen(state: RoomState,
                  onEvent: (RoomEvent) -> Unit,
                  userNewViewModel: UserViewModel = viewModel()){
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

                        Scaffold(
                            floatingActionButton = {
                                FloatingActionButton(onClick = {
                                    onEvent(RoomEvent.ShowAddUserDialog)
                                }){
                                    Icon(imageVector = Icons.Default.Add,
                                        contentDescription = "Add Room"
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
                                                    onClick = { RoomEvent.SortRooms(sortType)}
                                                )
                                                Text(text = sortType.name)
                                            }
                                        }
                                    }
                                }
                                items(state.rooms){ room ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                    ){
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ){
                                            Text(
                                                text = "${room.roomName} ${room.password}",
                                                fontSize = 20.sp
                                            )
                                            Text(text = "admin email: " + room.emailAdmin + ",", fontSize = 12.sp)
                                            Text(text = "room id: " + room.id, fontSize = 12.sp)
                                        }
                                        IconButton(onClick = {
                                            Log.d("press edit", "hi!")
                                            onEvent(RoomEvent.ShowEditRoomDialog(room))
                                        }){
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Room"
                                            )
                                        }
                                        IconButton(onClick = {
                                            onEvent(RoomEvent.DeleteRoom(room))
                                        }){
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Room"
                                            )
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

}


//
//@Preview
//@Composable
//fun HomeNewScreenPreview(){
//    HomeNewScreen(state = , onEvent = )
//}