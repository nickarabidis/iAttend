package com.example.attendtest.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
fun PasswordNeededDialog(
    state: RoomState,
    onEvent: (RoomEvent) -> Unit,
    modifier: Modifier = Modifier,
    userNewViewModel: UserViewModel = viewModel()
){
    AlertDialog(
        onDismissRequest = {
            onEvent(RoomEvent.HidePasswordNeededDialog)
        },
        title = {
            Text(text = "Password Needed for Attendance", fontWeight = FontWeight.SemiBold)
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
                    },
                    shape = RoundedCornerShape(12.dp)
                )

            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){
                Button(colors = ButtonDefaults.buttonColors(Color.Transparent),onClick = {
                    onEvent(RoomEvent.CheckPassword(userNewViewModel.emailId))

                }){
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
                        Text(text = "Enter Password", fontSize = 16.sp)
                    }
                }
            }
        },
        dismissButton = {
            //onEvent(RoomEvent.HideAddUserDialog)
        },
        containerColor = DrawerPrimary,
        modifier = Modifier

    )
}