package com.example.attendtest.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.data.room.RoomState
import com.example.attendtest.data.user.UserState
import com.example.attendtest.data.user.UserEvent

@Composable
fun AddRoomDialog(
    state: RoomState,
    onEvent: (RoomEvent) -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        onDismissRequest = {
            onEvent(RoomEvent.HideDialog)
        },
        title = {
            Text(text = "Add Room")
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
                        Text(text = "Room Name")
                    }
                )
                TextField(
                    value = state.password,
                    onValueChange = {
                        onEvent(RoomEvent.SetPassword(it))
                    },
                    placeholder = {
                        Text(text = "Password")
                    }
                )
                TextField(
                    value = state.emailAdmin,
                    onValueChange = {
                        onEvent(RoomEvent.SetEmailAdmin(it))
                    },
                    placeholder = {
                        Text(text = "EmailAdmin")
                    }
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){
                Button(onClick = {
                    onEvent(RoomEvent.SaveRoom)
                }){
                    Text(text = "Save")
                }
            }
        },
        dismissButton = {
            //onEvent(RoomEvent.HideDialog)
        },
        modifier = Modifier

    )
}