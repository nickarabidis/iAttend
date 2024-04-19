package com.example.attendtest.screens

import android.annotation.SuppressLint
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.R
import com.example.attendtest.components.AddUserInRoomDialog
import com.example.attendtest.components.RoomHeaderTextComponent
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.data.room.RoomState
import com.example.attendtest.data.room.RoomViewModel
import com.example.attendtest.data.user.UserViewModel
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.roomAndUser.RoomAndUserSortType
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen
import com.example.attendtest.navigation.SystemBackButtonHandler
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.*
import com.example.attendtest.database.roomAndUser.RoomAndUser
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RoomScreen(room: Room,
               state: RoomState,
               onEvent: (RoomEvent) -> Unit,
               userNewViewModel: UserViewModel = viewModel(),
               roomNewViewModel: RoomViewModel = viewModel()
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
                                        ": ${room.roomName}"
                            )
                        }
                        Row (modifier = Modifier
                            .fillMaxWidth()
                        ) {
                            RoomHeaderTextComponent(
                                value = stringResource(id = R.string.admin) +
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
                            RoomAndUserSortType.entries.forEach { sortType ->
                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            onEvent(RoomEvent.SortRoomsAndUsers(sortType))
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = state.roomanduserSortType == sortType,
                                        onClick = { onEvent(RoomEvent.SortRoomsAndUsers(sortType)) }
                                    )

                                    // Custom sort names
                                    val sortText = when (sortType.name) {
                                        RoomAndUserSortType.USER_EMAIL.toString() -> stringResource(id = R.string.sort_user_email)
                                        RoomAndUserSortType.IS_PRESENT.toString() -> stringResource(id = R.string.sort_is_present)
                                        else -> {"Can't find asked sortType"}
                                    }

                                    Text(text = sortText)
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
                                        if (userInRoom.isPresent) {
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

                                FirstNameAndLastName(viewModel = roomNewViewModel, userEmail = userInRoom.userEmail, userInRoom)

                                // deprecated
//                                Text(
//                                    text = buildAnnotatedString {
//                                            val firstName = roomNewViewModel.getFirstName(userInRoom.userEmail)
//                                            val lastName = roomNewViewModel.getLastName(userInRoom.userEmail)
//                                            // retrieve full name of user
//                                            val userInRoomFullName = "$firstName $lastName"
//                                            val presentOrNot = when (userInRoom.isPresent) {
//                                                true -> {
//                                                    "$userInRoomFullName is Present" //+ stringResource(id = R.string.is_present)
//                                                }
//                                                else -> {
//                                                    "$userInRoomFullName is Absent" //+ stringResource(id = R.string.is_not_present)
//                                                }
//                                            }
//                                            append(presentOrNot)
//                                    }
//                                )


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


// Fetch First and Last Name
@Composable
fun FirstNameAndLastName(
    viewModel: RoomViewModel,
    userEmail: String,
    userInRoom: RoomAndUser
) {
    val firstName = remember(userEmail) { mutableStateOf("") }
    val lastName = remember(userEmail) { mutableStateOf("") }

    LaunchedEffect(userEmail) {
        val fetchedFirstName = viewModel.getFirstName(userEmail)
        val fetchedLastName = viewModel.getLastName(userEmail)
        firstName.value = fetchedFirstName
        lastName.value = fetchedLastName
    }

    // Use the first name and last name in your UI
    // For example:
    when (userInRoom.isPresent) {
        true -> {
            Text(text = "${firstName.value} ${lastName.value}" + " " + stringResource(id = R.string.is_present))
        }
        else -> {
            Text(text = "${firstName.value} ${lastName.value}" + " " + stringResource(id = R.string.is_not_present))
        }
    }
}