package com.example.attendtest.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.components.AddRoomDialog
import com.example.attendtest.components.AddUserInRoomDialog
import com.example.attendtest.components.EditRoomDialog
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.data.room.RoomState
import com.example.attendtest.data.user.UserEvent
import com.example.attendtest.data.user.UserViewModel
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.room.roomSortType
import com.example.attendtest.database.roomAndUser.roomAndUserSortType
import com.example.attendtest.database.user.userSortType
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen
import com.example.attendtest.navigation.SystemBackButtonHandler


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RoomScreen(room: Room,
               state: RoomState,
               onEvent: (RoomEvent) -> Unit,
               userNewViewModel: UserViewModel = viewModel()
){

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
//            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        onEvent(RoomEvent.ShowAddUserInRoomDialog(room))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add User In Room"
                        )
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) { padding ->

                //Log.d(userNewViewModel.TAG, "state.isAddingRoom= ${state.isAddingRoom}")
                //Log.d(userNewViewModel.TAG, "state.isEditingRoom= ${state.isEditingRoom}")
                if (state.isAddingUserInRoom) {
                    AddUserInRoomDialog(state = state, onEvent = onEvent)
                }


                LazyColumn(
                    contentPadding = padding,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            roomAndUserSortType.entries.forEach { sortType ->
                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            onEvent(RoomEvent.SortRoomsAndUsers(sortType))
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = state.roomanduserSortType == sortType,
                                        onClick = { RoomEvent.SortRoomsAndUsers(sortType) }
                                    )
                                    Text(text = sortType.name)
                                }
                            }
                        }
                    }
                    items(state.roomAndUsers) { userInRoom ->

                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clickable {
                                    ///AppRouter.navigateTo(Screen.RoomScreen(roomAndUsers))
                                }
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "${userInRoom.userEmail + ", " + userInRoom.isPresent}",
                                    fontSize = 20.sp
                                )
//                                Text(
//                                    text = "admin email: " + room.emailAdmin + ",",
//                                    fontSize = 12.sp
//                                )
//                                Text(text = "room id: " + room.id, fontSize = 12.sp)
                            }
                            IconButton(onClick = {
                                onEvent(RoomEvent.DeleteUserFromRoom(userInRoom))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Room"
                                )
                            }
                        }

                    }
                }
            }
        }
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.HomeNewScreen)
    }
}


//@Preview
//@Composable
//fun RoomScreenPreview(){
//    RoomScreen()
//}