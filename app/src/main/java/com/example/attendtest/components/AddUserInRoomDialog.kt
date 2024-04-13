package com.example.attendtest.components

import android.util.Log
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.data.room.RoomState
import com.example.attendtest.data.user.UserViewModel

@Composable
fun AddUserInRoomDialog(
    state: RoomState,
    onEvent: (RoomEvent) -> Unit,
    modifier: Modifier = Modifier,
    userNewViewModel: UserViewModel = viewModel()
){
    AlertDialog(
        onDismissRequest = {
            onEvent(RoomEvent.HideAddUserInRoomDialog)
        },
        title = {
            Text(text = "Add User In Room")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                TextField(
                    value = state.emailOfUser,
                    onValueChange = {
                        onEvent(RoomEvent.SetEmailOfUser(it))
                    },
                    placeholder = {
                        Text(text = "EmailOfUser")
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
                    if(state.emailOfUser != userNewViewModel.emailId){
                        onEvent(RoomEvent.SaveUserInRoom)
                    }
                }){
                    if(state.emailOfUser != userNewViewModel.emailId){
                        Text(text = "Save")
                    }else{
                        Text(text = "Don't Save")
                    }
                }
            }
        },
        dismissButton = {
            //onEvent(RoomEvent.HideAddUserInRoomDialog)
        },
        modifier = Modifier

    )
}