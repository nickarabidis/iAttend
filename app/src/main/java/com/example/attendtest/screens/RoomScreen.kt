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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.R
import com.example.attendtest.components.AddRoomDialog
import com.example.attendtest.components.AddUserInRoomDialog
import com.example.attendtest.components.EditRoomDialog
import com.example.attendtest.components.NormalTextComponent
import com.example.attendtest.components.RoomHeaderTextComponent
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
import java.text.SimpleDateFormat
import java.util.Locale


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RoomScreen(room: Room,
               state: RoomState,
               onEvent: (RoomEvent) -> Unit,
               userNewViewModel: UserViewModel = viewModel()
){

    // retrieve user data like: email
    userNewViewModel.getUserData()

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
                            contentDescription = stringResource(id = R.string.add_participant)
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
                        Row (modifier = Modifier
                            .fillMaxWidth()
                        ) {
                            RoomHeaderTextComponent(
                                value = stringResource(id = R.string.room_name) +
                                        ": ${room.roomName} | " +
                                        stringResource(id = R.string.admin) +
                                        ": ${room.emailAdmin}"
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.sort) + ":",
                                fontWeight = FontWeight.Bold
                            )
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
                    items(state.roomAndUsers.filter { it.roomId == room.id }){  userInRoom ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    ///AppRouter.navigateTo(Screen.RoomScreen(roomAndUsers))
                                }
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                val dateFormat = SimpleDateFormat("MMM dd HH:mm", Locale.getDefault())
                                val presentDateString = userInRoom.presentDate?.let { dateFormat.format(it) } ?: ""
                                Text(
                                    text = buildAnnotatedString {
                                        append(userInRoom.userEmail)
                                        if (userInRoom.isPresent == true) {
                                            // Append a space
                                            append(" ")
                                            // Append the icon
                                            withStyle(style = SpanStyle(fontSize = 28.sp)) {
                                                append("✓") // Use the desired Unicode character for your checkmark
                                            }
                                            withStyle(style = SpanStyle(fontSize = 20.sp)) {
                                                append (" ")
                                                append (presentDateString)
                                            }
                                        }else{
                                            // Append a space
                                            append(" ")
                                            // Append the icon
                                            withStyle(style = SpanStyle(fontSize = 28.sp)) {
                                                append("✗") // Use the desired Unicode character for your checkmark
                                            }
                                        }
                                    },
                                    fontSize = 20.sp
                                )


//                                if (userInRoom.isPresent == true){
////                                    Icon(
////                                        painter = painterResource(R.drawable.done),
////                                        contentDescription = "Done Attendance"
////                                    )
//                                    Log.d(userNewViewModel.TAG,"$userInRoom.presentDate")
//                                    Text(
//                                        text = "${presentDateString}",
//                                        fontSize = 16.sp
//                                    )
//                                }else{
////                                    Icon(
////                                        painter = painterResource(R.drawable.done_outline),
////                                        contentDescription = "Not Done Attendance"
////                                    )
//                                }
//                                Text(
//                                    text = "${userInRoom.userEmail + ", " + userInRoom.isPresent + ", " + userInRoom.presentDate}",
//                                    fontSize = 20.sp
//                                )
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
                                    contentDescription = stringResource(id = R.string.remove_participant)
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