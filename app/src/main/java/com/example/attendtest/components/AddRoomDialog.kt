package com.example.attendtest.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.R
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.data.room.RoomState
import com.example.attendtest.data.user.UserViewModel
import com.example.attendtest.database.room.RoomVisibilityType

@Composable
fun AddRoomDialog(
    state: RoomState,
    onEvent: (RoomEvent) -> Unit,
    userNewViewModel: UserViewModel = viewModel(),
    modifier: Modifier = Modifier
){


    AlertDialog(
        onDismissRequest = {
            onEvent(RoomEvent.HideAddRoomDialog)
        },
        title = {
            Text(text = stringResource(id = R.string.add_room))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                TextField(
                    value = state.roomName,
                    onValueChange = {
                        onEvent(RoomEvent.SetRoomName(it))
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.room_name))
                    }
                )
                TextField(
                    value = state.password,
                    onValueChange = {
                        onEvent(RoomEvent.SetPassword(it))
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.password))
                    }
                )
                Column {
                    Row {
                        Column {
                            Text(
                                text = stringResource(id = R.string.change_visibility),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RoomVisibilityType.entries.forEach { visibilityType ->
                            Column(
                                modifier = Modifier
                                    .clickable{
                                        when (visibilityType) {
                                            RoomVisibilityType.VISIBLE -> {
                                                onEvent(RoomEvent.SetVisibilityType(visibilityType))
                                                onEvent(RoomEvent.SetIsVisible(true))
                                                onEvent(RoomEvent.SetPasswordNeeded(false))
                                            }
                                            RoomVisibilityType.INVISIBLE -> {
                                                onEvent(RoomEvent.SetVisibilityType(visibilityType))
                                                onEvent(RoomEvent.SetIsVisible(false))
                                                onEvent(RoomEvent.SetPasswordNeeded(false))
                                            }
                                            RoomVisibilityType.PASSWORD -> {
                                                onEvent(RoomEvent.SetVisibilityType(visibilityType))
                                                onEvent(RoomEvent.SetIsVisible(true))
                                                onEvent(RoomEvent.SetPasswordNeeded(true))
                                            }
                                        }
                                    },
                                verticalArrangement = Arrangement.Center
                            ){
                                RadioButton(
                                    selected = state.visibilityType == visibilityType,
                                    onClick = {
                                        onEvent(RoomEvent.SetVisibilityType(visibilityType))
                                        when (visibilityType) {
                                            RoomVisibilityType.VISIBLE -> {
                                                onEvent(RoomEvent.SetIsVisible(true))
                                                onEvent(RoomEvent.SetPasswordNeeded(false))
                                            }
                                            RoomVisibilityType.INVISIBLE -> {
                                                onEvent(RoomEvent.SetIsVisible(false))
                                                onEvent(RoomEvent.SetPasswordNeeded(false))
                                            }
                                            RoomVisibilityType.PASSWORD -> {
                                                onEvent(RoomEvent.SetIsVisible(true))
                                                onEvent(RoomEvent.SetPasswordNeeded(true))
                                            }
                                        }
                                    }
                                )
                                val visibilityTypeIcon = when (visibilityType.name) {
                                    RoomVisibilityType.VISIBLE.toString() -> R.drawable.visibility
                                    RoomVisibilityType.PASSWORD.toString() -> R.drawable.visibility_lock
                                    RoomVisibilityType.INVISIBLE.toString() -> R.drawable.visibility_off
                                    else -> {
                                        Log.e("Icon Type error: ", "can't find visibilityType")
                                    }
                                }

                                val visibilityTypeDesc = when (visibilityType.name) {
                                    RoomVisibilityType.VISIBLE.toString() -> R.string.turn_to_visible
                                    RoomVisibilityType.PASSWORD.toString() -> R.string.turn_to_password_needed
                                    RoomVisibilityType.INVISIBLE.toString() -> R.string.turn_to_invisible
                                    else -> {
                                        Log.e("Icon Description error: ", "can't find visibilityType")
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = visibilityTypeIcon),
                                        contentDescription = stringResource(id = visibilityTypeDesc)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(text = visibilityType.name)
                                }

                            }
                        }
                    }
                }
//                TextField(
//                    value = state.emailAdmin,
//                    onValueChange = {
//                        onEvent(RoomEvent.SetEmailAdmin(it))
//                    },
//                    placeholder = {
//                        Text(text = "EmailAdmin")
//                    }
//                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){
                Button(onClick = {
                    onEvent(RoomEvent.SaveRoom(userNewViewModel.emailId))
                }){
                    Text(text = "Save")
                }
            }
        },
        dismissButton = {
            //onEvent(RoomEvent.HideAddUserDialog)
        },
        modifier = Modifier

    )
}