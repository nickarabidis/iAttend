package com.example.attendtest.components


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendtest.R
import com.example.attendtest.data.NavigationItem
import com.example.attendtest.database.room.Room
import com.example.iattend.ui.theme.DarkPrimary

import com.example.iattend.ui.theme.GrayColor
import com.example.iattend.ui.theme.Primary
import com.example.iattend.ui.theme.Secondary
import com.example.iattend.ui.theme.TextColor
import com.example.iattend.ui.theme.WhiteColor



@Composable
fun NormalTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(id = R.color.colorText),
        textAlign = TextAlign.Center
    )
}

@Composable
fun RoomHeaderTextComponent(value: String){
    Text(
        text = value,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .heightIn(min = 40.dp)
            .fillMaxWidth()
        ,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = WhiteColor
    )
}

@Composable
fun HeadingTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(id = R.color.colorText),
        textAlign = TextAlign.Center
    )
}

@ExperimentalMaterial3Api
@Composable
fun MyTextFieldComponent(labelValue: String, onTextSelected: (String) -> Unit, errorStatus: Boolean = false) {
    val textValue = remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 4.dp),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
//            unfocusedTextColor = Primary,
//            unfocusedBorderColor = Primary,
//            unfocusedLabelColor = Primary,
//            unfocusedLeadingIconColor = Primary
        ),
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextSelected(it)
        },
        isError = !errorStatus
//        ,
//        leadingIcon = {
//            Icon(painter = painterResource(), contentDescription = "")
//        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun PasswordTextFieldComponent(labelValue: String, onTextSelected: (String) -> Unit, errorStatus: Boolean = false) {

    val localFocusManager = LocalFocusManager.current


    val password = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 4.dp),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary
//            unfocusedTextColor = Primary,
//            unfocusedBorderColor = Primary,
//            unfocusedLabelColor = Primary,
//            unfocusedLeadingIconColor = Primary
        ),
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        singleLine = true,
        keyboardActions = KeyboardActions{
            localFocusManager.clearFocus()
        },
        maxLines = 1,
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        isError = !errorStatus,
//        ,
//        leadingIcon = {
//            Icon(painter = painterResource(), contentDescription = "")
//        },
//        trailingIcon ={
//            val iconImage = if(passwordVisible.value){
//                Icons.Filled.Visibility
//            }else{
//                Icons.Filled.VisibilityOff
//            }
//
//            var description = if(passwordVisible.value){
//                stringResource(id = R.string.hide_password)
//            }else{
//                stringResource(id = R.string.show_password)
//            }
//
//            IconButton(onClick = {passwordVisible.value = !passwordVisible.value}) {
//                Icon(imageVector = iconImage, contentDescription = description)
//
//            }
//        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
    )
}


@Composable
fun CheckboxComponent(
    value: String,
    onTextSelected: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .padding(horizontal = 28.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val checkedState = remember {
            mutableStateOf(false)
        }

        Checkbox(checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = !checkedState.value
                onCheckedChange.invoke(it)
            })

        ClickableTextComponent(value = value, onTextSelected)
    }
}

@Composable
fun ClickableTextComponent(value: String, onTextSelected: (String) -> Unit) {
    val initialText = "By continuing you accept our "
    val privacyPolicyText = "Privacy Policy"
    val andText = " and "
    val termsAndConditionsText = "Term of Use"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        append(andText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = termsAndConditionsText, annotation = termsAndConditionsText)
            append(termsAndConditionsText)
        }
    }

    ClickableText(text = annotatedString, onClick = { offset ->

        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.also { span ->
                Log.d("ClickableTextComponent", "{${span.item}}")

                if ((span.item == termsAndConditionsText) || (span.item == privacyPolicyText)) {
                    onTextSelected(span.item)
                }
            }

    })
}


@Composable
fun ButtonComponent(value: String, onButtonClicked: () -> Unit, isEnabled: Boolean = false){
    Button(
        onClick = {
            onButtonClicked.invoke()
                  },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .padding(horizontal = 28.dp, vertical = 4.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
        ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(
                color = Color(0xFF004A7F),
                shape = RoundedCornerShape(50.dp)
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun DividerTextComponent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.or),
            fontSize = 18.sp,
            color = TextColor
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp
        )
    }
}

@Composable
fun ClickableLoginTextComponent(tryingToLogin: Boolean = true, onTextSelected: (String) -> Unit) {
    val initialText =
        if (tryingToLogin) "Already have an account? " else "Don’t have an account yet? "
    val loginText = if (tryingToLogin) stringResource(id = R.string.sign_in) else stringResource(id = R.string.sign_up)

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .padding(horizontal = 28.dp, vertical = 4.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{${span.item}}")

                    if (span.item == loginText) {
                        onTextSelected(span.item)
                    }
                }

        },
    )
}

@Composable
fun UnderLinedTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(id = R.color.colorGray),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )
}


@ExperimentalMaterial3Api
@Composable
fun AppToolbar(toolbarTitle: String,
               logoutButtonClicked: () -> Unit,
               navigationIconClicked: () -> Unit){
    TopAppBar(
        //backgroundColor = Primary,
        colors = topAppBarColors(
            containerColor = DarkPrimary,
            titleContentColor = DarkPrimary
        ),
        title = {
            Text(text = toolbarTitle, color = WhiteColor)
        },
        navigationIcon = {
            IconButton(onClick = {
                navigationIconClicked.invoke()
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = WhiteColor
                )
            }

        },
        actions = {
            IconButton(onClick = {
                logoutButtonClicked.invoke()
            }) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = stringResource(id = R.string.logout),
                    tint = WhiteColor)
            }

        }

    )
}

@Composable
fun NavigationDrawerHeader(value: String?){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(32.dp)
    ){
        Column {
            NavigationDrawerText(title = stringResource(R.string.greeting), 40.sp)
            NavigationDrawerText(title = value?:stringResource(R.string.navigation_header), 26.sp)
        }
    }
}

@Composable
fun NavigationDrawerBody(navigationDrawerItems : List<NavigationItem>,
                         onNavigationItemClicked: (NavigationItem) -> Unit){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
    ){
        items(navigationDrawerItems){
            NavigationItemRow(item = it, onNavigationItemClicked)
        }
    }
}

@Composable
fun NavigationItemRow(item: NavigationItem,
                      onNavigationItemClicked: (NavigationItem) -> Unit){

    val shadowOffset = Offset(4f,6f)

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onNavigationItemClicked.invoke(item)
        }
        .padding(all = 16.dp)
        //.background(Primary)
    ){
        Icon(imageVector = item.icon, contentDescription = item.description, modifier = Modifier.size(34.dp))
        Spacer(modifier = Modifier.width(18.dp))

        NavigationDrawerText(title = item.title, 24.sp)

    }
}


@Composable
fun NavigationDrawerText(title:String, textUnit: TextUnit){
    val shadowOffset = Offset(2f,3f)

    Text(text = title, style = TextStyle(
        color = Color.Black,
        fontSize = textUnit,
        fontStyle = FontStyle.Normal,
        shadow = Shadow(
            color = Primary,
            offset = shadowOffset, 1f
        )
    )
    )
}

@Composable
fun BackgroundWithText(text1: String, text2: String, text3: String) {
    Box(modifier = Modifier
        .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
        .fillMaxWidth()
        .height(186.dp)
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.iattend_bg),
            contentDescription = "App Background",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize() // Cover the entire box
        )

        // Text Components
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text1,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                color = colorResource(id = R.color.colorWhite),
                textAlign = TextAlign.Center
            )
            Text(
                text = text2,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                ),
                color = colorResource(id = R.color.colorWhite),
                textAlign = TextAlign.Center
            )
            Text(
                text = text3,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                color = colorResource(id = R.color.colorWhite),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BackgroundCredits(text1: String, text2: String) {
    Box(modifier = Modifier
        .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
        .fillMaxWidth()
        .height(186.dp)
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.iattend_bg),
            contentDescription = "App Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,// Cover the entire box
        )

        // Text Components
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text1,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                color = colorResource(id = R.color.colorWhite),
                textAlign = TextAlign.Center
            )
            Text(
                text = text2,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                ),
                color = colorResource(id = R.color.colorWhite),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BackgroundRoomText(room: Room) {
    Box(modifier = Modifier
        .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
        .fillMaxWidth()
        .height(100.dp)
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.iattend_bg),
            contentDescription = "App Background",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize() // Cover the entire box
        )

        // Text Components
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RoomHeaderTextComponent(
                value = stringResource(id = R.string.room_name) +
                        ": ${room.roomName}"
            )
            RoomHeaderTextComponent(
                value = stringResource(id = R.string.admin) +
                        ": ${room.emailAdmin}"
            )
        }
    }
}
