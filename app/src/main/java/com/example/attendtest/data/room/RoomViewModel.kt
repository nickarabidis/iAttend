package com.example.attendtest.data.room

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendtest.database.room.Room
import com.example.attendtest.database.room.RoomDao
import com.example.attendtest.database.room.roomSortType
import com.example.attendtest.database.roomAndUser.RoomAndUser
import com.example.attendtest.database.roomAndUser.RoomAndUserDao
import com.example.attendtest.database.roomAndUser.roomAndUserSortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class RoomViewModel (
    private val dao: RoomDao,
    private val roomAndUserDao: RoomAndUserDao
) : ViewModel() {

    val TAG = RoomViewModel::class.simpleName

    var InProgress = mutableStateOf(false)
    //var loginUIState = mutableStateOf(LoginNewUIState())
    //var registrationUIState = mutableStateOf(RegistrationNewUIState())

    var roomState = mutableStateOf(RoomState())

    //private val _sortTypeRoomsAndUsers = MutableStateFlow(roomAndUserSortType.USER_EMAIL)

    private val _sortType = MutableStateFlow(roomSortType.ROOM_NAME)

    private val _roomAndUserSortType = MutableStateFlow(roomAndUserSortType.USER_EMAIL)
    private val _roomAndUsers = _roomAndUserSortType
        .flatMapLatest { sortType ->
            when(sortType){
                roomAndUserSortType.USER_EMAIL -> roomAndUserDao.getRoomsOrderedByUserEmail()
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
                roomSortType.ID -> dao.getRoomsOrderedById()
                roomSortType.ROOM_NAME -> dao.getRoomsOrderedByRoomName()
                roomSortType.PASSWORD -> dao.getRoomsOrderedByPassword()
                roomSortType.EMAIL_ADMIN -> dao.getRoomsOrderedByEmailAdmin()
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

            is RoomEvent.SaveRoom -> {
                val roomName = state.value.roomName
                val password = state.value.password
                val emailAdmin = state.value.emailId
                var id: Long

                Log.d(TAG, "id: $emailAdmin")

                if (emailAdmin != null) {
                    if(roomName.isBlank() || password.isBlank() || emailAdmin.isBlank()){
                        return
                    }
                }

                val room = emailAdmin?.let {
                    Room(
                        roomName = roomName,
                        password = password,
                        emailAdmin = it
                    )
                }
                viewModelScope.launch{
                    id = room?.let { dao.upsertRoom(it) }!!
//                    val id = dao.insertRoom(room)
                    Log.d(TAG, "id: $id")


                    _state.update { it.copy(
                        id = id,
                        isAddingRoom = false,
                        roomName = "",
                        password = "",
                        emailAdmin = "",
                        //currentRoom = roomName
                    ) }

                    Log.d(TAG, "id: ${state.value.id}")
                }


            }


            is RoomEvent.SaveEdits -> {
                val newRoomName = state.value.roomName
                val newPassword = state.value.password
                val newEmailAdmin = state.value.emailAdmin
                val currentId = state.value.currentRoom?.id
                Log.d(TAG, "new room: ${newRoomName}, new pass: ${newPassword}, new email: ${newEmailAdmin}, id: ${currentId}")

                if(newRoomName.isBlank() || newPassword.isBlank() || newEmailAdmin.isBlank()){
                    return
                }

                viewModelScope.launch {
                    val originalRoom = currentId?.let { dao.getRoomFromId(it) }

                    if (originalRoom != null) { // Check if originalRoom is not null
                        val updatedRoom = originalRoom.copy(
                            roomName = newRoomName,
                            password = newPassword,
                            emailAdmin = newEmailAdmin
                        )

                        dao.updateRoom(updatedRoom)
                    } else {
                        // Handle the case where the room with the specified ID does not exist
                        Log.e(TAG, "Original room not found for ID: ${currentId}")
                    }
                }

                _state.update { it.copy(
                    isEditingRoom = false,
                    roomName = "",
                    password = "",
                    emailAdmin = "",
                    //currentRoom = newRoomName
                ) }
            }
            is RoomEvent.SaveUserInRoom -> {
                val emailOfUser = state.value.emailOfUser
                val currentId = state.value.currentRoom?.id
                val isPresent = state.value.isPresent

                if(emailOfUser.isBlank()){
                    return
                }

                val roomAndUser = currentId?.let {
                    RoomAndUser(
                        roomId = it,
                        userEmail = emailOfUser,
                        isPresent = isPresent
                    )
                }
                viewModelScope.launch{
                    if (roomAndUser != null) {
                        roomAndUserDao.upsertRoomAndUser(roomAndUser)
                    }
//                    val id = dao.insertRoom(room)
                   // Log.d(TAG, "id: $id")


                    _state.update { it.copy(
                        isAddingUserInRoom = false,
                        emailOfUser = "",
                        isPresent = false

                    ) }

                    //Log.d(TAG, "id: ${state.value.id}")
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

            is RoomEvent.isPresent ->{
                _state.update { it.copy(
                    currentRoom = event.room
                )}
                val userEmail = event.emailId
                val currentRoom = state.value.currentRoom?.id
                val isPresent = state.value.isPresent
                Log.d(TAG, "currentRoom: ${currentRoom}, userEmail: ${userEmail}")

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
                                isPresent= true
                            )
                        }

                        if (updatedRoomAndUser != null) {
                            roomAndUserDao.upsertRoomAndUser(updatedRoomAndUser)
                        }
                    } else {
                        // Handle the case where the room with the specified ID does not exist
                        Log.e(TAG, "Original room not found for ID: ${currentRoom}")
                    }
                }

                _state.update { it.copy(
                    emailOfUser = "",
                    isPresent = false,
                    isDone = true,
                    currentRoom = event.room

                ) }
            }

            is RoomEvent.SortRooms ->{
                _sortType.value = event.sortType
            }

            is RoomEvent.SortRoomsAndUsers ->{
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
//            firstName = state.value.firstName
//        )
//        val lastNameResult = Validator.validateLastName(
//            lastName = state.value.lastName
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

}