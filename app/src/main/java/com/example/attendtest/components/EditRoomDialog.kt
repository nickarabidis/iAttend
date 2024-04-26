package com.example.attendtest.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
// see
// see
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendtest.R
import com.example.attendtest.data.room.RoomEvent
import com.example.attendtest.data.room.RoomState
import com.example.attendtest.database.room.RoomVisibilityType
import com.example.iattend.ui.theme.DrawerPrimary

@Composable
fun EditRoomDialog(
    state: RoomState,
    onEvent: (RoomEvent) -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        onDismissRequest = {
            onEvent(RoomEvent.HideEditRoomDialog)
        },
        title = {
            Text(text = stringResource(id = R.string.edit_room), fontWeight = FontWeight.SemiBold)
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
                Log.d("visibility type: ", "${state.visibilityType}")

                TextField(
                        value = state.password,
                        onValueChange = {
                            onEvent(RoomEvent.SetPassword(it))
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.password))
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                Column {
                    Row {
                        Column {
                            Text(
                                text = stringResource(id = R.string.change_visibility),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        RoomVisibilityType.entries.forEach { visibilityType ->
                            Row(
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
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
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
                                        contentDescription = stringResource(id = visibilityTypeDesc),
                                        tint = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))

                                    val visibilityText = when (visibilityType.name) {
                                        RoomVisibilityType.VISIBLE.toString() -> R.string.visible
                                        RoomVisibilityType.PASSWORD.toString() -> R.string.password_needed
                                        RoomVisibilityType.INVISIBLE.toString() -> R.string.invisible
                                        else -> {
                                            Log.e("Type name description error: ", "can't find visibilityType")
                                        }
                                    }

                                    Text(
                                        text = stringResource(id = visibilityText),
                                        fontSize = 18.sp,
                                        color = Color.Black
                                    )
                                }

                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){
                Button(colors = ButtonDefaults.buttonColors(Color.Transparent),onClick = {
//                    Log.d("SELECTEDVISIBILITYTYPE", "$selectedVisibilityType")
                    onEvent(RoomEvent.SaveEdits)
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
                        Text(text = stringResource(id = R.string.save_changes), fontSize = 16.sp)
                    }
                }
            }
        },
        dismissButton = {
            //onEvent(RoomEvent.HideAddRoomDialog)
        },
        containerColor = DrawerPrimary,
        modifier = Modifier

    )
}