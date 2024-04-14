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

@Composable
fun PasswordNeededDialog(
    state: RoomState,
    onEvent: (RoomEvent) -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        onDismissRequest = {
            onEvent(RoomEvent.HidePasswordNeededDialog)
        },
        title = {
            Text(text = "Password Needed for Attendance")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                TextField(
                    value = state.passwordToEnter,
                    onValueChange = {
                        onEvent(RoomEvent.SetPasswordToEnter(it))
                    },
                    placeholder = {
                        Text(text = "Password")
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
                    onEvent(RoomEvent.CheckPassword)

                }){
                    Text(text = "Enter Password")
                }
            }
        },
        dismissButton = {
            //onEvent(RoomEvent.HideAddUserDialog)
        },
        modifier = Modifier

    )
}