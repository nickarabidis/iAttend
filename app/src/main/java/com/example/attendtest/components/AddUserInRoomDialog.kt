package com.example.attendtest.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.data.room.RoomState
import com.example.attendtest.data.user.UserViewModel
import com.example.iattend.ui.theme.DrawerPrimary

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
            Text(text = "Add User In Room", fontWeight = FontWeight.SemiBold)
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
                    },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Button(colors = ButtonDefaults.buttonColors(Color.Transparent),onClick = {
                    if(state.emailOfUser != userNewViewModel.emailId){
                        onEvent(RoomEvent.SaveUserInRoom)
                    }
                }) {
                    Box(
                        modifier = Modifier
                            .heightIn(50.dp)
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF004A7F),
                                shape = RoundedCornerShape(50.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.emailOfUser != userNewViewModel.emailId) {
                            Text(text = "Save", fontSize = 16.sp)
                        } else {
                            Text(text = "Don't Save", fontSize = 16.sp)
                        }
                    }
                }
            }
        },
        dismissButton = {
            //onEvent(RoomEvent.HideAddUserInRoomDialog)
        },
        containerColor = DrawerPrimary,
        modifier = Modifier

    )
}