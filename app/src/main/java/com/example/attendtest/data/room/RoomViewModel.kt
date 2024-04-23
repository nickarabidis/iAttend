package com.example.attendtest.data.room

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendtest.data.user.UserViewModel
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.room.RoomDao
import com.example.attendtest.database.room.RoomSortType
import com.example.attendtest.database.room.RoomVisibilityType
import com.example.attendtest.database.roomAndFavorites.RoomAndFavorites
import com.example.attendtest.database.roomAndFavorites.RoomAndFavoritesDao
import com.example.attendtest.database.roomAndUser.RoomAndUser
import com.example.attendtest.database.roomAndUser.RoomAndUserDao
import com.example.attendtest.database.roomAndUser.RoomAndUserSortType
import com.example.attendtest.database.user.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class RoomViewModel (
    private val dao: RoomDao,
    private val roomAndUserDao: RoomAndUserDao,
    private val userDao: UserDao,
    private val roomAndFavoritesDao: RoomAndFavoritesDao
) : ViewModel() {

    val TAG = RoomViewModel::class.simpleName

    var InProgress = mutableStateOf(false)
    //var loginUIState = mutableStateOf(LoginNewUIState())
    //var registrationUIState = mutableStateOf(RegistrationNewUIState())

    var roomState = mutableStateOf(RoomState())

    //private val _sortTypeRoomsAndUsers = MutableStateFlow(RoomAndUserSortType.USER_EMAIL)

    private val _sortType = MutableStateFlow(RoomSortType.ID)

    private val _favoriteRoomIds = MutableStateFlow<List<Long>>(emptyList())
    val favoriteRoomIds: StateFlow<List<Long>> = _favoriteRoomIds

//    private val context = application.applicationContext

    // visibility
    private val _visibilityType = MutableStateFlow(RoomVisibilityType.VISIBLE)

    private val _roomAndUserSortType = MutableStateFlow(RoomAndUserSortType.USER_EMAIL)
    private val _roomAndUsers = _roomAndUserSortType
        .flatMapLatest { sortType ->
            when(sortType){
                RoomAndUserSortType.USER_EMAIL -> roomAndUserDao.getRoomsOrderedByUserEmail()
                RoomAndUserSortType.IS_PRESENT -> roomAndUserDao.getRoomsOrderedByAttendance()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//    val stateRoomAndUsers = combine(_state, _sortType, _rooms){ state, sortType, rooms ->
//        state.copy(
//            roomAndUsers = rooms,
//            sortType = sortType
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RoomState())

    private val _rooms = _sortType
        .flatMapLatest { sortType ->
            when(sortType){
                RoomSortType.ID -> dao.getRoomsOrderedById()
                RoomSortType.ROOM_NAME -> dao.getRoomsOrderedByRoomName()
                // RoomSortType.PASSWORD -> dao.getRoomsOrderedByPassword()
                RoomSortType.EMAIL_ADMIN -> dao.getRoomsOrderedByEmailAdmin()
                RoomSortType.FAVORITES -> dao.getRoomsOrderedByFavorites()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val _state = MutableStateFlow(RoomState())
    val state = combine(_state, _sortType, _rooms, _roomAndUserSortType, _roomAndUsers){ state, sortType, rooms, roomAndUserSortType, roomAndUsers->
        state.copy(
            rooms = rooms,
            sortType = sortType,
            roomanduserSortType = roomAndUserSortType,
            roomAndUsers = roomAndUsers,

            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RoomState())

    fun onEvent(event: RoomEvent){
        //validateDateWithRules()
        //validateLoginUIDataWithRules()
        when(event){
            is RoomEvent.DeleteRoom -> {
                viewModelScope.launch {
                    dao.deleteRoom(event.room)
                }
            }
            is RoomEvent.DeleteUserFromRoom ->{
                viewModelScope.launch {
                    roomAndUserDao.deleteRoomAndUser(event.roomAndUser)
                }
            }
            is RoomEvent.HideAddRoomDialog -> {
                _state.update{it.copy(
                    isAddingRoom = false
                ) }
            }

            is RoomEvent.InitFavoriteRoom -> {
                val emailOfUser = event.userEmail
                val id = state.value.currentRoom?.id
                val isFavorite = false

                val roomAndFavorite = id?.let {
                    RoomAndFavorites(
                        userEmail = emailOfUser!!,
                        roomId = it,
                        isFavorite = isFavorite,
                    )
                }
                viewModelScope.launch {
                    Log.d("Init email", "$emailOfUser")
                    Log.d("Init id", "$id")

                    roomAndFavoritesDao.upsertFavoriteRoom(roomAndFavorite!!)
//                    val id = dao.insertRoom(room)
                    Log.d(TAG, "id: $id")


                    _state.update {
                        it.copy(
                            id = id!!,
                            isFavorite = false,
                            //isVisible = false,
                            //passwordNeeded = false
                            //currentRoom = roomName
                        )
                    }

                    Log.d(TAG, "id: ${state.value.id}")
                }
            }

            is RoomEvent.SaveRoom -> {
                val roomName = state.value.roomName
                val password = state.value.password
                val emailAdmin = state.value.emailId
                var id: Long
                val isVisible = state.value.isVisible
                val passwordNeeded = state.value.passwordNeeded

                val emailOfUser = event.userEmail
                val isFavorite = false

                if (emailAdmin != null) {
                    if(roomName.isBlank() || password.isBlank() || emailAdmin.isBlank()){
                        return
                    }
                }

                val room = emailAdmin?.let {
                    Room(
                        roomName = roomName,
                        password = password,
                        emailAdmin = it,
                        isVisible = isVisible,
                        passwordNeeded = passwordNeeded
                    )
                }
                viewModelScope.launch{
                    id = room?.let { dao.upsertRoom(it) }!!
//                    val id = dao.insertRoom(room)
                    Log.d(TAG, "id: $id")

                    val roomAndFavorite = RoomAndFavorites(
                        userEmail = emailOfUser!!,
                        roomId = id,
                        isFavorite = isFavorite,
                    )

                    roomAndFavoritesDao.upsertFavoriteRoom(roomAndFavorite!!)

                    Log.d("Init email", "$emailOfUser")
                    Log.d("Init id", "$id")

                    _state.update { it.copy(
                        id = id,
                        isAddingRoom = false,
                        roomName = "",
                        password = "",
                        emailAdmin = "",
                        isFavorite = false,
                        //isVisible = false,
                        //passwordNeeded = false
                        currentRoom = room
                    ) }

                    Log.d(TAG, "id111: ${state.value.id}")
                }


            }

            is RoomEvent.UnfavoriteRoom -> {
                _state.update { it.copy(
                    currentRoom = event.room
                )}
                val userEmail = event.userEmail
                val currentRoom = state.value.currentRoom?.id
                Log.d(TAG, "currentRoom: ${currentRoom}, userEmail: $userEmail")

                viewModelScope.launch {
                    val roomAndFavorite = RoomAndFavorites(
                        roomId = currentRoom!!, // Return if currentId is null
                        userEmail = userEmail!!,
                        isFavorite = false,
                    )
                    // Proceed with saving roomAndUser
                    roomAndFavoritesDao.upsertFavoriteRoom(roomAndFavorite)

                    // Update state on the main thread
                    withContext(Dispatchers.Main) {
                        _state.update {
                            it.copy(
                                emailOfUser = "",
                                isFavorite = false,
                            )
                        }
                    }
                }
//                val updatedFavoriteRoomIds = favoriteRoomIds.value.toMutableList().apply {
//                    remove(event.room.id)
//                }
//                saveFavoriteRoomIds(context, updatedFavoriteRoomIds)
//                _favoriteRoomIds.value = updatedFavoriteRoomIds
            }

            is RoomEvent.FavoriteRoom -> {
                _state.update {
                    it.copy(
                        currentRoom = event.room
                    )
                }
                val userEmail = event.userEmail
                val currentRoom = state.value.currentRoom?.id
                Log.d(TAG, "currentRoom: ${currentRoom}, userEmail: $userEmail")

                viewModelScope.launch {
                    val roomAndFavorite = RoomAndFavorites(
                        roomId = currentRoom!!, // Return if currentId is null
                        userEmail = userEmail!!,
                        isFavorite = true,
                    )
                    // Proceed with saving roomAndUser
                    roomAndFavoritesDao.upsertFavoriteRoom(roomAndFavorite)

                    // Update state on the main thread
                    withContext(Dispatchers.Main) {
                        _state.update {
                            it.copy(
                                emailOfUser = "",
                                isFavorite = false,
                            )
                        }
                    }
                }


                _state.update {
                    it.copy(
                        emailOfUser = "",
                        currentRoom = event.room,
                        isFavorite = false,
                    )
                }
//                val updatedFavoriteRoomIds = favoriteRoomIds.value.toMutableList().apply {
//                    add(event.room.id)
//                }
//                saveFavoriteRoomIds(context, updatedFavoriteRoomIds)
//                _favoriteRoomIds.value = updatedFavoriteRoomIds
            }

            is RoomEvent.SaveEdits -> {
                val newRoomName = state.value.roomName
                val newPassword = state.value.password
                val currentId = state.value.currentRoom?.id
                val newPasswordNeeded = state.value.passwordNeeded
                val newIsVisible = state.value.isVisible
                Log.d(TAG, "new room: ${newRoomName}, new pass: ${newPassword}, id: ${currentId}, vis: $newIsVisible, passNeeded: $newPasswordNeeded")

                if(newRoomName.isBlank() || newPassword.isBlank()){
                    return
                }

                viewModelScope.launch {
                    val originalRoom = currentId?.let { dao.getRoomFromId(it) }
                    Log.d("ID IS", "$currentId")

                    if (originalRoom != null) { // Check if originalRoom is not null
                        val updatedRoom = originalRoom.copy(
                            roomName = newRoomName,
                            password = newPassword,
                            isVisible = newIsVisible,
                            passwordNeeded = newPasswordNeeded,
                        )

                        dao.updateRoom(updatedRoom)
                    } else {
                        // Handle the case where the room with the specified ID does not exist
                        Log.e(TAG, "Original room not found for ID: $currentId")
                    }
                }

                _state.update { it.copy(
                    isEditingRoom = false,
                    roomName = "",
                    password = "",
                    emailAdmin = "",
                    //currentRoom = newRoomName
                    validPassword = false,
                    passwordToEnter = "",
                    //passwordNeeded = false
                ) }
            }


//            is RoomEvent.SaveUserInRoom -> {
//                val emailOfUser = state.value.emailOfUser
//                val currentId = state.value.currentRoom?.id
//                val isPresent = state.value.isPresent
//
//                if(emailOfUser.isBlank()){
//                    return
//                }
//
//                val roomAndUser = currentId?.let {
//                    RoomAndUser(
//                        roomId = it,
//                        userEmail = emailOfUser,
//                        isPresent = isPresent
//                    )
//                }
//                viewModelScope.launch{
//                    if (roomAndUser != null) {
//                        roomAndUserDao.upsertRoomAndUser(roomAndUser)
//                    }
////                    val id = dao.insertRoom(room)
//                   // Log.d(TAG, "id: $id")
//
//
//                    _state.update { it.copy(
//                        isAddingUserInRoom = false,
//                        emailOfUser = "",
//                        isPresent = false
//
//                    ) }
//
//                    //Log.d(TAG, "id: ${state.value.id}")
//                }
//            }

//            is RoomEvent.FavoriteRoom -> {
//                val currentId = state.value.currentRoom?.id
//
//                viewModelScope.launch {
//                    // Perform the database operation in a background thread
//                    withContext(Dispatchers.IO) {
//                        // Check if the email exists in the user table
//
//
//                        if (currentId?.let { roomAndFavoritesDao.isRoomFavorite(it) } == null) {
//                            // Email exists, proceed with saving roomAndUser
//                            val roomAndFavorites = RoomAndFavorites(
//                                roomId = currentId ?: return@withContext, // Return if currentId is null
//                                isFavorite = when (roomAndFavorites)
//                            )
//                            // Proceed with saving roomAndUser
//                            roomAndUserDao.upsertRoomAndUser(roomAndUser)
//
//                            // Update state on the main thread
//                            withContext(Dispatchers.Main) {
//                                _state.update { it.copy(
//                                    isAddingUserInRoom = false,
//                                    emailOfUser = "",
//                                    isPresent = false,
//                                    presentDate = null
//                                ) }
//                            }
//                        } else {
//
//                        }
//                    }
//                }
//            }

            is RoomEvent.SaveUserInRoom -> {
                val emailOfUser = state.value.emailOfUser
                val currentId = state.value.currentRoom?.id
                val isPresent = state.value.isPresent
                val presentDate = null

                if (emailOfUser.isBlank()) {
                    // Handle empty email case, maybe by throwing an exception or setting an error state
                    return
                }

                viewModelScope.launch {
                    // Perform the database operation in a background thread
                    withContext(Dispatchers.IO) {
                        // Check if the email exists in the user table
                        val email = userDao.getEmail(emailOfUser)

                        if (email == null) {
                            // Handle case where email doesn't exist in the user table
                            // Maybe by throwing an exception or setting an error state
                            Log.d(TAG, "Not valid email")
                            return@withContext
                        } else {
                            // Email exists, proceed with saving roomAndUser
                            val roomAndUser = RoomAndUser(
                                roomId = currentId ?: return@withContext, // Return if currentId is null
                                userEmail = emailOfUser,
                                isPresent = isPresent,
                                presentDate = null
                            )
                            // Proceed with saving roomAndUser
                            roomAndUserDao.upsertRoomAndUser(roomAndUser)

                            val roomAndFavorite = RoomAndFavorites(
                                userEmail = emailOfUser,
                                roomId = currentId,
                                isFavorite = false,
                            )

                            roomAndFavoritesDao.upsertFavoriteRoom(roomAndFavorite!!)

                            // Update state on the main thread
                            withContext(Dispatchers.Main) {
                                _state.update { it.copy(
                                    isAddingUserInRoom = false,
                                    emailOfUser = "",
                                    isPresent = false,
                                    presentDate = null,
                                    isFavorite = false
                                ) }
                            }
                        }
                    }
                }
            }

            is RoomEvent.SetRoomName ->{
                _state.update { it.copy(
                    roomName = event.roomName
                )}
            }
            is RoomEvent.SetPassword ->{
                _state.update { it.copy(
                    password = event.password
                )}
            }
            // visibility
            is RoomEvent.SetIsVisible ->{
                _state.update { it.copy(
                    isVisible = event.isVisible
                )}
            }
            is RoomEvent.SetPasswordNeeded ->{
                _state.update { it.copy(
                    passwordNeeded = event.passwordNeeded
                )}
            }
            is RoomEvent.SetVisibilityType ->{
                _state.update { it.copy(
                    visibilityType = event.visibilityType
                )}
            }
            is RoomEvent.SetPasswordToEnter ->{
                _state.update { it.copy(
                    passwordToEnter = event.passwordToEnter
                )}
            }

            is RoomEvent.CheckPassword ->{
                val currentRoomPassword = state.value.currentRoom?.password
                if (state.value.passwordToEnter == currentRoomPassword){
                    Log.d(TAG, "in checkPassword")


                    val userEmail = event.emailId
                    val currentRoom = state.value.currentRoom?.id
                    val isPresent = state.value.isPresent
                    Log.d(TAG, "currentRoom: ${currentRoom}, userEmail: $userEmail")


                    viewModelScope.launch {
                        val originalRoomAndUser =
                            currentRoom?.let {
                                userEmail?.let { it1 ->
                                    roomAndUserDao.getRoomAndUserFromId(
                                        it,
                                        it1
                                    )
                                }
                            }

                        if (originalRoomAndUser != null) { // Check if originalRoom is not null
                            val updatedRoomAndUser = userEmail?.let {
                                originalRoomAndUser.copy(
                                    roomId = currentRoom,
                                    userEmail = it,
                                    isPresent = true,
                                    presentDate = Date()
                                )
                            }

                            if (updatedRoomAndUser != null) {
                                roomAndUserDao.upsertRoomAndUser(updatedRoomAndUser)
                            }
                        } else {
                            // Handle the case where the room with the specified ID does not exist
                            Log.e(TAG, "Original room not found for ID: $currentRoom")
                        }

                    }

                    _state.update { it.copy(
                        //validPassword = true,
                        isPasswordNeeded = false,
                        emailOfUser = "",
                        isPresent = false,
                        isDone = true,
                        validPassword = false,
                        passwordToEnter = "",
                        passwordNeeded = false,
                        presentDate = null
                    )}
                }else{
                    _state.update { it.copy(
                        validPassword = false
                    )}
                }

            }

            is RoomEvent.SetEmailAdmin ->{
                _state.update { it.copy(
                    emailAdmin = event.emailAdmin
                )}
            }
            is RoomEvent.SetEmailOfUser ->{
                _state.update { it.copy(
                    emailOfUser = event.emailOfUser
                )}
            }

            is RoomEvent.GetFirstName -> {
                val emailOfUser = event.userEmail
                Log.d(TAG, "emailOfUser: $emailOfUser")

                if (emailOfUser.isBlank()) {
                    // Handle empty email case, maybe by throwing an exception or setting an error state
                    return
                }

                viewModelScope.launch {
                    // Perform the database operation in a background thread
                    withContext(Dispatchers.IO) {
                        // Check if the email exists in the user table
                        val fName = userDao.getFirstName(emailOfUser)
                        Log.d(TAG, "fName: $fName")

                        if (emailOfUser == null) {
                            // Handle case where email doesn't exist in the user table
                            // Maybe by throwing an exception or setting an error state
                            Log.d(TAG, "Not valid email")
                            return@withContext
                        } else {


                            // Update state on the main thread
                            withContext(Dispatchers.Main) {
                                _state.update { it.copy(
                                    userFirstName = fName
                                ) }
                            }
                        }
                    }
                }
            }

            is RoomEvent.GetLastName -> {
                val emailOfUser = event.userEmail

                if (emailOfUser.isBlank()) {
                    // Handle empty email case, maybe by throwing an exception or setting an error state
                    return
                }

                viewModelScope.launch {
                    // Perform the database operation in a background thread
                    withContext(Dispatchers.IO) {
                        // Check if the email exists in the user table
                        val lName = userDao.getLastName(emailOfUser)

                        if (emailOfUser == null) {
                            // Handle case where email doesn't exist in the user table
                            // Maybe by throwing an exception or setting an error state
                            Log.d(TAG, "Not valid email")
                            return@withContext
                        } else {


                            // Update state on the main thread
                            withContext(Dispatchers.Main) {
                                _state.update { it.copy(
                                    userLastName = lName
                                ) }
                            }
                        }
                    }
                }
            }

            is RoomEvent.ShowAddRoomDialog ->{
                _state.update { it.copy(
                    isAddingRoom = true,
                    emailId = event.emailId
                )}
            }

            // new!
            is RoomEvent.ShowEditRoomDialog ->{
                _state.update { it.copy(
                    isEditingRoom = true,
                    currentRoom = event.room
                )}
            }

            is RoomEvent.HideEditRoomDialog ->{
                _state.update { it.copy(
                    isEditingRoom = false,
                    currentRoom = null
                )}
            }

            is RoomEvent.ShowAddUserInRoomDialog ->{
                _state.update { it.copy(
                    isAddingUserInRoom = true,
                    currentRoom = event.room
                )}
            }

            is RoomEvent.HideAddUserInRoomDialog ->{
                _state.update { it.copy(
                    isAddingUserInRoom = false,
                    currentRoom = null
                )}
            }
            //passwordNeeded Dialog
            is RoomEvent.ShowPasswordNeededDialog ->{
                _state.update { it.copy(
                    isPasswordNeeded = true,
                    currentRoom = event.room
                )}
            }
            is RoomEvent.HidePasswordNeededDialog ->{
                _state.update { it.copy(
                    isPasswordNeeded = false,
                    currentRoom = null
                )}
            }

            is RoomEvent.GetRoomIdFromUserEmail -> {
                val userEmail = event.emailId
                val currentRooms = event.rooms
                viewModelScope.launch {
                    val roomIds = mutableListOf<Long>()
                    for (room in currentRooms) {
                        try {
                            val roomId = roomAndUserDao.getRoomIdFromUserEmail(userEmail, room.id)
                            if (roomId != null) { // Check if roomId is not null
                                roomIds.add(roomId)
                            } else {
                                // Handle the case where roomId is null
                                Log.e("RoomViewModel", "RoomId is null for room: $room")
                            }
                        } catch (e: Exception) {
                            // Log the error
                            Log.e("RoomViewModel", "Error getting roomId: ${e.message}")
                        }
                    }

                    _state.update { it.copy(
                        currentRoomIds = roomIds
                    )}
                }
            }

            is RoomEvent.GetFavoriteRoomIdFromUserEmail -> {
                val userEmail = event.emailId
                val currentRooms = event.rooms
                viewModelScope.launch {
                    val roomIds = mutableListOf<Long>()
                    for (room in currentRooms) {
                        try {
                            val roomId = roomAndFavoritesDao.getFavoriteRoomIdFromUserEmail(userEmail, room.id)
                            if (roomId != null) { // Check if roomId is not null
                                roomIds.add(roomId)
                            } else {
                                // Handle the case where roomId is null
                                Log.e("RoomViewModel", "RoomId is null for room: $room")
                            }
                        } catch (e: Exception) {
                            // Log the error
                            Log.e("RoomViewModel", "Error getting roomId: ${e.message}")
                        }
                    }

                    _state.update { it.copy(
                        currentFavoriteRoomIds = roomIds
                    )}
                }

//                val userEmail = event.emailId
//                val currentRooms = event.rooms
//                viewModelScope.launch {
//                    val favoriteRoomIds = mutableListOf<Long>()
//                    for (favoriteroom in currentRooms) {
//                        try {
//                            val isFavorite = roomAndFavoritesDao.getIsFavorite(userEmail!!, favoriteroom.id)
//                            Log.d("isFavorite", "$isFavorite")
//                            if (isFavorite) { // Check if roomId is not null
//                                favoriteRoomIds.add(favoriteroom.id)
//                            } else {
//                                // Handle the case where roomId is null
//                                favoriteRoomIds.remove(favoriteroom.id)
//                                Log.e("RoomViewModel", "RoomId is null for favorite room: $favoriteroom")
//                            }
//                        } catch (e: Exception) {
//                            // Log the error
//                            Log.e("RoomViewModel", "Error getting roomId: ${e.message}")
//                        }
//                    }
//
//                    _state.update { it.copy(
//                        currentFavoriteRoomIds = favoriteRoomIds
//                    )}
//                }
            }

            is RoomEvent.GetEmailFromRoom-> {
                val userEmail = event.emailId
                val currentRooms = event.rooms
                viewModelScope.launch {
                    val presentRoom = mutableListOf<Long>()
                    for (room in currentRooms) {
                        try {
                            val userPresent =
                                userEmail?.let { roomAndUserDao.getUserEmailFromRoomId(it, room.id) }
                            Log.d("userPresent", "${userPresent}")

                            if (userPresent == true) { // Check if roomId is not null
                                presentRoom.add(room.id)
                            } else {
                                // Handle the case where roomId is null
                                Log.e("RoomViewModel", "userPresent is false for room: $room")
                            }
                        } catch (e: Exception) {
                            // Log the error
                            Log.e("RoomViewModel", "Error getting roomId: ${e.message}")
                        }
                    }

                    _state.update { it.copy(
                        currentPresentRoomIds = presentRoom
                    )}
                }
            }
            is RoomEvent.GetPasswordNeededFromRoom ->{
                _state.update { it.copy(
                    currentRoom = event.room
                )}
                val currentRoomId = state.value.currentRoom?.id
                Log.d(TAG, "View currentRoomId = ${currentRoomId}")
                viewModelScope.launch {
                    val passwordNeeded = currentRoomId?.let { dao.getPasswordNeededFromRoomId(it) }
                    Log.d(TAG, "View passwordNeeded = ${passwordNeeded}")

                    _state.update { passwordNeeded?.let { it1 -> it.copy(passwordNeeded = it1) }!! }
                }
            }



            is RoomEvent.isPresent ->{
                _state.update { it.copy(
                    currentRoom = event.room
                )}
                val userEmail = event.emailId
                val currentRoom = state.value.currentRoom?.id
                val isPresent = state.value.isPresent
                Log.d(TAG, "currentRoom: ${currentRoom}, userEmail: $userEmail")

//                if(userEmail.isBlank()){
//                    return
//                }

                viewModelScope.launch {
                    val originalRoomAndUser =
                        currentRoom?.let { userEmail?.let { it1 ->
                            roomAndUserDao.getRoomAndUserFromId(it,
                                it1
                            )
                        } }

                    if (originalRoomAndUser != null) { // Check if originalRoom is not null
                        val updatedRoomAndUser = userEmail?.let {
                            originalRoomAndUser.copy(
                                roomId= currentRoom,
                                userEmail= it,
                                isPresent= true,
                                presentDate = Date()
                            )
                        }

                        if (updatedRoomAndUser != null) {
                            roomAndUserDao.upsertRoomAndUser(updatedRoomAndUser)
                        }
                    } else {
                        // Handle the case where the room with the specified ID does not exist
                        Log.e(TAG, "Original room not found for ID: $currentRoom")
                    }
                }

                _state.update { it.copy(
                    emailOfUser = "",
                    isPresent = false,
                    isDone = true,
                    currentRoom = event.room,
                    validPassword = false,
                    passwordToEnter = "",
                    passwordNeeded = false,
                    presentDate = null

                ) }
            }

            is RoomEvent.SortRooms -> {
                _sortType.value = event.sortType
            }

            is RoomEvent.SortRoomsAndUsers -> {
                _roomAndUserSortType.value = event.sortTypeRoomAndUser
            }

            is RoomEvent.GetRoomName -> {
                val key = state.value.roomName

                viewModelScope.launch {
                    val roomName = dao.getRoomName(key)
                    // Dispatch the event with the retrieved first name
                    _state.update { it.copy(roomName = roomName) }
                }
            }
            is RoomEvent.GetPassword -> {
                val key = state.value.roomName

                viewModelScope.launch {
                    val password = dao.getPassword(key)
                    // Dispatch the event with the retrieved first name
                    _state.update { it.copy(password = password) }
                }
            }
            is RoomEvent.GetEmailAdmin -> {
                val key = state.value.roomName

                viewModelScope.launch {
                    val emailAdmin = dao.getEmailAdmin(key)
                    // Dispatch the event with the retrieved first name
                    _state.update { it.copy(emailAdmin = emailAdmin) }
                }
            }

            is RoomEvent.RoomNameChanged -> {
                _state.value = state.value.copy(
                    roomName = event.roomName
                )
//                _state.value = _state.value.copy(
//                    currentRoom = event.roomName
//                )
                //printState()
            }
            is RoomEvent.PasswordChanged -> {
                _state.value = state.value.copy(
                    password = event.password
                )
                //printState()
            }
            is RoomEvent.EmailAdminChanged -> {
                _state.value = state.value.copy(
                    emailAdmin = event.emailAdmin
                )
                //printState()
            }

            // unused
//            is RoomEvent.AddRoomButtonClicked ->{
//                addButtonRoomDatabase()
//                //signUpDatabase()
//            }

//            is RoomEvent.PrivacyPolicyCheckBoxClicked ->{
//                _state.value = state.value.copy(
//                    privacyPolicyAccepted = event.status
//                )
//                //printState()
//            }

            else -> {}

        }
    }

//    private fun createRoomInDatabase(roomName: String, password: String, emailAdmin: String) {
//
//        InProgress.value = true
//
//        viewModelScope.launch {
//            onEvent(RoomEvent.SetRoomName(roomName))
//            onEvent(RoomEvent.SetPassword(password))
//            onEvent(RoomEvent.SetEmailAdmin(emailAdmin))
//            onEvent(RoomEvent.SaveRoom)
//
//            InProgress.value = false
//
//            AppRouter.navigateTo(Screen.HomeNewScreen)
//        }
//    }

    // unused
//    private fun addButtonRoomDatabase(){
//        Log.d(TAG, "Inside_AddButtonRoom")
//        //printState()
//
//        Log.d(TAG, "roomroomName = ${state.value.roomName}")
//        Log.d(TAG, "roompassword = ${state.value.password}")
//        Log.d(TAG, "roomemailAdmin = ${state.value.emailAdmin}")
//        createRoomInDatabase(
//            roomName = state.value.roomName,
//            password = state.value.password,
//            emailAdmin = state.value.emailAdmin
//        )
//
//        //validateDateWithRules()
//    }

    //    private suspend fun getEmailAndPasswordFromDatabase(email: String): Pair<String?, String?> {
//        return withContext(Dispatchers.IO) {
//            val emailFromDatabase = dao.getEmail(email)
//            val passwordFromDatabase = dao.getPassword(email)
//            Pair(emailFromDatabase, passwordFromDatabase)
//        }
//    }
//    private fun loginDatabase() {
//
//        //InProgress.value = true
//
//        viewModelScope.launch {
//            val email = state.value.email
//            val password = state.value.password
//
//            val (emailFromDatabase, passwordFromDatabase) = getEmailAndPasswordFromDatabase(email)
//            Log.d(TAG, "email = $email")
//            Log.d(TAG, "password = $password")
//            Log.d(TAG, "emailFromDatabase = $emailFromDatabase")
//            Log.d(TAG, "passwordFromDatabase = $passwordFromDatabase")
//            if (emailFromDatabase == email && passwordFromDatabase == password) {
//                // Navigate to the home screen if email and password match
//                AppRouter.navigateTo(Screen.HomeNewScreen)
//            } else {
//                // Handle incorrect email or password
//                // For example, update UI state to show an error message
//            }
//        }
//    }
//
//    var allValidationsPassed = mutableStateOf(false)
//
//
//    private fun validateLoginUIDataWithRules(){
//        val emailResult = Validator.validateEmail(
//            email = state.value.email
//        )
//        val passwordResult = Validator.validatePassword(
//            password = state.value.password
//        )
//
//        _state.value = state.value.copy(
//            emailError = emailResult.status,
//            passwordError = passwordResult.status
//        )
//
//        allValidationsPassed.value = emailResult.status && passwordResult.status
//
//    }
//
//    private fun validateDateWithRules(){
//        val firstNameResult = Validator.validateFirstName(
//            userFirstName = state.value.userFirstName
//        )
//        val lastNameResult = Validator.validateLastName(
//            userLastName = state.value.userLastName
//        )
//        val emailResult = Validator.validateEmail(
//            email = state.value.email
//        )
//        val passwordResult = Validator.validatePassword(
//            password = state.value.password
//        )
//        val privacyPolicyResult = Validator.validatePolicyAcceptance(
//            statusValue = state.value.privacyPolicyAccepted
//        )
//
//        Log.d(TAG, "Inside_validateDataWithRules")
//        Log.d(TAG, "firstNameResult= $firstNameResult")
//        Log.d(TAG, "lastNameResult= $lastNameResult")
//        Log.d(TAG, "emailResult= $emailResult")
//        Log.d(TAG, "passwordResult= $passwordResult")
//        Log.d(TAG, "privacyPolicyResult= $privacyPolicyResult")
//
//
//        _state.value = state.value.copy(
//            firstNameError = firstNameResult.status,
//            lastNameError = lastNameResult.status,
//            emailError = emailResult.status,
//            passwordError = passwordResult.status,
//            privacyPolicyError = privacyPolicyResult.status
//        )
//
//        if(firstNameResult.status && lastNameResult.status &&
//            emailResult.status && passwordResult.status && privacyPolicyResult.status){
//            allValidationsPassed.value = true
//        }else{
//            allValidationsPassed.value = false
//        }
//    }
//
//    val navigationItemsList = listOf<NavigationItem>(
//        NavigationItem(
//            title = "Home",
//            icon = Icons.Default.Home,
//            description = "Home Screen",
//            itemId = "homeScreen"
//        ),
//        NavigationItem(
//            title = "Settings",
//            icon = Icons.Default.Settings,
//            description = "Settings Screen",
//            itemId = "settingsScreen"
//        ),
//        NavigationItem(
//            title = "Favorite",
//            icon = Icons.Default.Favorite,
//            description = "Favorite Screen",
//            itemId = "favoriteScreen"
//        )
//    )
//
//    //var emailId = mutableStateOf(UserState())
//    //val currentUser = userState.value.currentUser
//    //Log.d(TAG, "Inside sign outsuccess")
//
//    //var emailId: MutableLiveData<String> = MutableLiveData()
//    var emailId: String? = null
//    fun getUserData(){
//        viewModelScope.launch(Dispatchers.IO) {
//            val emailKey = _state.value.currentUser
//            Log.d(TAG, "emailKey= $emailKey")
//            emailId = dao.getEmail(emailKey)
//            //onEvent(UserEvent.GetEmail(emailKey))
//
//            //val emailId = _state.value.email
//            Log.d(TAG, "emailId= $emailId")
//        }
//    }
//
//    fun logoutDatabase(){
//        Log.d(TAG, "Inside sign outsuccess")
//
//        AppRouter.navigateTo(Screen.LoginNewScreen)
//
//    }
    suspend fun getRoomIdFromUserEmail(email: String?, roomId: Long): Long {

        return withContext(Dispatchers.IO) {
            roomAndUserDao.getRoomIdFromUserEmail(email, roomId)
        }
    }



    suspend fun getPasswordNeededFromRoom(room: Room): Boolean? {
        _state.update { it.copy(
            currentRoom = room
        )}
        val currentRoomId = state.value.currentRoom?.id
        Log.d(TAG, "View currentRoomId = ${currentRoomId}")

        val passwordNeeded = currentRoomId?.let { dao.getPasswordNeededFromRoomId(it) }
        Log.d(TAG, "View passwordNeeded = ${passwordNeeded}")

        return passwordNeeded

    }

    suspend fun getPresentNeededFromRoom(room: Room): Boolean? {
        _state.update { it.copy(
            currentRoom = room
        )}
        val currentRoomId = state.value.currentRoom?.id
        Log.d(TAG, "View currentRoomId = ${currentRoomId}")

        val presentNeeded = currentRoomId?.let { roomAndUserDao.getPresentNeededFromRoomId(it) }
        Log.d(TAG, "View passwordNeeded = ${presentNeeded}")

        return presentNeeded

    }

    suspend fun getFirstName(userEmail: String): String {
        return withContext(Dispatchers.IO) {
            userDao.getFirstName(userEmail)
        }
    }

    suspend fun getLastName(userEmail: String): String {
        return withContext(Dispatchers.IO) {
            userDao.getLastName(userEmail)
        }
    }

    suspend fun unfavoriteRoom(userEmail: String, room: Room) {
        _state.update { it.copy(
            currentRoom = room
        )}
        val currentRoom = state.value.currentRoom?.id
        Log.d(TAG, "currentRoom: ${currentRoom}, userEmail: $userEmail")


        val roomAndFavorite = RoomAndFavorites(
            roomId = currentRoom!!, // Return if currentId is null
            userEmail = userEmail!!,
            isFavorite = false,
        )
        // Proceed with saving roomAndUser
        roomAndFavoritesDao.upsertFavoriteRoom(roomAndFavorite)

        // Update state on the main thread
        withContext(Dispatchers.Main) {
            _state.update {
                it.copy(
                    emailOfUser = "",
                    currentRoom = room,
                    isFavorite = false,
                )
            }
        }

//                val updatedFavoriteRoomIds = favoriteRoomIds.value.toMutableList().apply {
//                    remove(event.room.id)
//                }
//                saveFavoriteRoomIds(context, updatedFavoriteRoomIds)
//                _favoriteRoomIds.value = updatedFavoriteRoomIds
    }

    suspend fun favoriteRoom(userEmail: String, room: Room) {
        _state.update {
            it.copy(
                currentRoom = room
            )
        }
        val currentRoom = state.value.currentRoom?.id
        Log.d(TAG, "currentRoom: ${currentRoom}, userEmail: $userEmail")


        val roomAndFavorite = RoomAndFavorites(
            roomId = currentRoom!!, // Return if currentId is null
            userEmail = userEmail!!,
            isFavorite = true,
        )
        // Proceed with saving roomAndUser
        roomAndFavoritesDao.upsertFavoriteRoom(roomAndFavorite)

        // Update state on the main thread

//            _state.update {
//                it.copy(
//                    emailOfUser = "",
//                    isFavorite = false,
//                )
//            }




        _state.update {
            it.copy(
                emailOfUser = "",
                currentRoom = room,
                isFavorite = false,
            )
        }
//                val updatedFavoriteRoomIds = favoriteRoomIds.value.toMutableList().apply {
//                    add(event.room.id)
//                }
//                saveFavoriteRoomIds(context, updatedFavoriteRoomIds)
//                _favoriteRoomIds.value = updatedFavoriteRoomIds
    }

    suspend fun getIsFavorite(userEmail: String, id: Long): Boolean {
        return withContext(Dispatchers.IO) {
            roomAndFavoritesDao.getIsFavorite(userEmail, id)
        }
    }

    suspend fun getRoomAndFavorite(userEmail: String, id: Long): RoomAndFavorites {
        return withContext(Dispatchers.IO) {
            roomAndFavoritesDao.getRoomAndFavoriteFromId(id, userEmail)
        }
    }

    suspend fun checkFavoriteRoom(id: Long, userEmail: String): Boolean {

        val isFavorite = roomAndFavoritesDao.getIsFavorite(userEmail, id)
        return isFavorite
    }

    // Add a method to save favorite room IDs to SharedPreferences
    fun saveFavoriteRoomIds(context: Context, favoriteRoomIds: List<Long>) {
        val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("favoriteRoomIds", favoriteRoomIds.joinToString(","))
        editor.apply()
    }

    // Add a method to load favorite room IDs from SharedPreferences
    fun loadFavoriteRoomIds(context: Context): List<Long> {
        val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favoriteRoomIdsString = sharedPreferences.getString("favoriteRoomIds", "")
        return favoriteRoomIdsString?.split(",")?.mapNotNull { it.toLongOrNull() } ?: emptyList()
    }

    suspend fun CheckPassword(): Boolean {
        val currentRoomPassword = state.value.currentRoom?.password
        if (state.value.passwordToEnter == currentRoomPassword) {
            _state.update { it.copy(
                validPassword = true,
                isPasswordNeeded = false,
            )}
            return true
        }else{
            _state.update { it.copy(
                validPassword = false
            )}
            return false
        }

    }
}