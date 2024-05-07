package com.example.attendtest.data.user

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendtest.R
import com.example.attendtest.data.NavigationItem
import com.example.attendtest.data.rules.Validator
import com.example.attendtest.database.user.User
import com.example.attendtest.database.user.UserDao
import com.example.attendtest.database.user.userSortType
import com.example.attendtest.database.userSettings.Languages
import com.example.attendtest.database.userSettings.Themes
import com.example.attendtest.database.userSettings.UserSettings
import com.example.attendtest.navigation.AppRouter
import com.example.attendtest.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModel (
    private val dao: UserDao
) : ViewModel() {

    public val TAG = UserViewModel::class.simpleName

    var InProgress = mutableStateOf(false)
    //var loginUIState = mutableStateOf(LoginNewUIState())
    //var registrationUIState = mutableStateOf(RegistrationNewUIState())

    var userState = mutableStateOf(UserState())


    private val _sortType = MutableStateFlow(userSortType.FIRST_NAME)
    private val _users = _sortType
        .flatMapLatest { sortType ->
            when(sortType){
                userSortType.FIRST_NAME -> dao.getUsersOrderedByFirstName()
                userSortType.LAST_NAME -> dao.getUsersOrderedByLastName()
                userSortType.EMAIL -> dao.getUsersOrderedByEmail()
                userSortType.PASSWORD -> dao.getUsersOrderedByPassword()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val _state = MutableStateFlow(UserState())
    val state = combine(_state, _sortType, _users){ state, sortType, users ->
        state.copy(
            users = users,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserState())

    fun onEvent(event: UserEvent){
        validateDateWithRules()
        validateLoginUIDataWithRules()
        when(event){
            is UserEvent.DeleteUser -> {
                viewModelScope.launch {
                    dao.deleteUser(event.user)
                }
            }
//            is UserEvent.HideAddRoomDialog -> {
//                _state.update{it.copy(
//                    isAddingUser =  false
//                ) }
//            }

            is UserEvent.SaveUser -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val email = state.value.email
                val password = state.value.password

                if(firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()){
                    return
                }

                val user = User(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password
                )
                viewModelScope.launch{
                    dao.upsertUser(user)
                }
                _state.update { it.copy(
                    isAddingUser = false,
                    firstName = "",
                    lastName = "",
                    email = "",
                    password = "",
                    currentUser = email
                ) }

            }

            is UserEvent.SaveSettings -> {
                val email = state.value.email
                val language = state.value.languageChosen.name
                val theme = state.value.themeChosen.name

                val userSettings = UserSettings(
                    userEmail = email,
                    language = language,
                    theme = theme
                )
                viewModelScope.launch{
                    dao.upsertUserSettings(userSettings)
                }
                _state.update { it.copy(
                    email = "",
                    themeChosen = Themes.LIGHT,
                    languageChosen = Languages.EN,
                    currentUser = email
                ) }

            }

            is UserEvent.SetFirstName ->{
                _state.update { it.copy(
                    firstName = event.firstName
                )}
            }
            is UserEvent.SetLastName ->{
                _state.update { it.copy(
                    lastName = event.lastName
                )}
            }
            is UserEvent.SetEmail ->{
                _state.update { it.copy(
                    email = event.email
                )}
            }
            is UserEvent.SetPassword ->{
                _state.update { it.copy(
                    password = event.password
                )}
            }
            is UserEvent.SetLanguage ->{
                _state.update { it.copy(
                    languageChosen = event.language
                )}
            }
            is UserEvent.SetTheme ->{
                _state.update { it.copy(
                    themeChosen = event.theme
                )}

                Log.d("ChangedThemeTo", event.theme.name)
            }

//            UserEvent.ShowAddRoomDialog ->{
//                _state.update { it.copy(
//                    isAddingUser = true
//                )}
//            }
            is UserEvent.SortUsers ->{
                _sortType.value = event.sortType
            }

            is UserEvent.GetEmail -> {
                val key = state.value.email

                viewModelScope.launch {
                    val email = dao.getEmail(key)
                    // Dispatch the event with the retrieved first name
                    _state.update { it.copy(email = email) }
                }
            }
            is UserEvent.GetPassword -> {
                val key = state.value.email

                viewModelScope.launch {
                    val password = dao.getPassword(key)
                    // Dispatch the event with the retrieved first name
                    _state.update { it.copy(password = password) }
                }
            }
            is UserEvent.GetFirstName -> {
                val key = state.value.email

                viewModelScope.launch {
                    val firstName = dao.getFirstName(key)
                    // Dispatch the event with the retrieved first name
                    _state.update { it.copy(firstName = firstName) }
                }
            }
            is UserEvent.GetLastName -> {
                val key = state.value.email

                viewModelScope.launch {
                    val lastName = dao.getLastName(key)
                    // Dispatch the event with the retrieved first name
                    _state.update { it.copy(lastName = lastName) }
                }
            }

            is UserEvent.FirstNameChanged -> {
                _state.value = state.value.copy(
                    firstName = event.firstName
                )
                //printState()
            }
            is UserEvent.LastNameChanged -> {
                _state.value = state.value.copy(
                    lastName = event.lastName
                )
                //printState()
            }
            is UserEvent.EmailChanged -> {
                _state.value = state.value.copy(
                    email = event.email
                )
                _state.value = _state.value.copy(
                    currentUser = event.email
                )
                //printState()
            }
            is UserEvent.PasswordChanged -> {
                _state.value = state.value.copy(
                    password = event.password
                )
                //printState()
            }

            is UserEvent.RegisterButtonClicked ->{
                signUpDatabase()
            }
            is UserEvent.LoginButtonClicked ->{
                loginDatabase()
            }

            is UserEvent.PrivacyPolicyCheckBoxClicked ->{
                _state.value = state.value.copy(
                    privacyPolicyAccepted = event.status
                )
                //printState()
            }

            UserEvent.RegisterButtonClicked -> TODO()
            else -> {}

        }
    }

    private fun createUserInDatabase(firstName: String, lastName: String, email: String, password: String) {

        InProgress.value = true

        viewModelScope.launch {
            onEvent(UserEvent.SetFirstName(firstName))
            onEvent(UserEvent.SetLastName(lastName))
            onEvent(UserEvent.SetEmail(email))
            onEvent(UserEvent.SetPassword(password))
            onEvent(UserEvent.SaveUser)

            InProgress.value = false

            AppRouter.navigateTo(Screen.HomeNewScreen)
        }
    }

    private fun createUserSettings(email: String, language: Languages, theme: Themes) {
        viewModelScope.launch {
            onEvent(UserEvent.SetEmail(email))
            onEvent(UserEvent.SetLanguage(language))
            onEvent(UserEvent.SetTheme(theme))
            onEvent(UserEvent.SaveSettings)
        }
    }

    private fun signUpDatabase(){
        Log.d(TAG, "Inside_signUp")
        //printState()

        Log.d(TAG, "signupfirstName = ${state.value.firstName}")
        Log.d(TAG, "signuplastName = ${state.value.lastName}")
        Log.d(TAG, "signupemail = ${state.value.email}")
        Log.d(TAG, "signuppassword = ${state.value.password}")
        createUserInDatabase(
            firstName = state.value.firstName,
            lastName = state.value.lastName,
            email = state.value.email,
            password = state.value.password
        )

        createUserSettings(
            email = state.value.email,
            language = state.value.languageChosen,
            theme = state.value.themeChosen
        )

        //validateDateWithRules()
    }

    private suspend fun getEmailAndPasswordFromDatabase(email: String): Pair<String?, String?> {
        return withContext(Dispatchers.IO) {
            val emailFromDatabase = dao.getEmail(email)
            val passwordFromDatabase = dao.getPassword(email)
            Pair(emailFromDatabase, passwordFromDatabase)
        }
    }
    private fun loginDatabase() {

        //InProgress.value = true

        viewModelScope.launch {
            val email = state.value.email
            val password = state.value.password

            val (emailFromDatabase, passwordFromDatabase) = getEmailAndPasswordFromDatabase(email)
            Log.d(TAG, "email = $email")
            Log.d(TAG, "password = $password")
            Log.d(TAG, "emailFromDatabase = $emailFromDatabase")
            Log.d(TAG, "passwordFromDatabase = $passwordFromDatabase")
            if (emailFromDatabase == email && passwordFromDatabase == password) {
                // Navigate to the home screen if email and password match
                AppRouter.navigateTo(Screen.HomeNewScreen)
            } else {
                // Handle incorrect email or password
                // For example, update UI state to show an error message
            }
        }
    }

    var allValidationsPassed = mutableStateOf(false)


    private fun validateLoginUIDataWithRules(){
        val emailResult = Validator.validateEmail(
            email = state.value.email
        )
        val passwordResult = Validator.validatePassword(
            password = state.value.password
        )

        _state.value = state.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed.value = emailResult.status && passwordResult.status

    }

    private fun validateDateWithRules(){
        val firstNameResult = Validator.validateFirstName(
            firstName = state.value.firstName
        )
        val lastNameResult = Validator.validateLastName(
            lastName = state.value.lastName
        )
        val emailResult = Validator.validateEmail(
            email = state.value.email
        )
        val passwordResult = Validator.validatePassword(
            password = state.value.password
        )
        val privacyPolicyResult = Validator.validatePolicyAcceptance(
            statusValue = state.value.privacyPolicyAccepted
        )

        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "firstNameResult= $firstNameResult")
        Log.d(TAG, "lastNameResult= $lastNameResult")
        Log.d(TAG, "emailResult= $emailResult")
        Log.d(TAG, "passwordResult= $passwordResult")
        Log.d(TAG, "privacyPolicyResult= $privacyPolicyResult")


        _state.value = state.value.copy(
            firstNameError = firstNameResult.status,
            lastNameError = lastNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status,
            privacyPolicyError = privacyPolicyResult.status
        )

        if(firstNameResult.status && lastNameResult.status &&
            emailResult.status && passwordResult.status && privacyPolicyResult.status){
            allValidationsPassed.value = true
        }else{
            allValidationsPassed.value = false
        }
    }

    val navigationItemsList = listOf(
        NavigationItem(
            titleResId = R.string.home,
            icon = Icons.Default.Home,
            description = "Home Screen",
            itemId = "homeScreen"
        ),
        NavigationItem(
            titleResId = R.string.settings,
            icon = Icons.Default.Settings,
            description = "Settings Screen",
            itemId = "settingsScreen"
        ),
        NavigationItem(
            titleResId = R.string.favorites,
            icon = Icons.Default.Favorite,
            description = "Favorite Screen",
            itemId = "favoriteScreen"
        )
    )

    //var emailId = mutableStateOf(UserState())
    //val currentUser = userState.value.currentUser
    //Log.d(TAG, "Inside sign outsuccess")

    //var emailId: MutableLiveData<String> = MutableLiveData()
    var emailId: String? = null
    var language: String? = null
    var theme: String? = null
//    var firstName: String = ""
//    var lastName: String = ""
    fun getUserData(){
        viewModelScope.launch(Dispatchers.IO) {
            val emailKey = _state.value.currentUser
//            val firstNameKey = _state.value.currentUser
//            val lastNameKey = _state.value.currentUser
            Log.d(TAG, "emailKey= $emailKey")
            emailId = dao.getEmail(emailKey)

            language = dao.getLanguage(emailKey)
            theme = dao.getTheme(emailKey)
//            firstName = dao.getFirstName(firstNameKey)
//            lastName = dao.getLastName(lastNameKey)
            //onEvent(UserEvent.GetEmail(emailKey))

            //val emailId = _state.value.email
            Log.d(TAG, "emailId= $emailId")
//            Log.d(TAG, "emailId= $firstName")
//            Log.d(TAG, "emailId= $lastName")
        }
    }

    fun logoutDatabase(){
        Log.d(TAG, "Inside sign outsuccess")

        AppRouter.navigateTo(Screen.LoginNewScreen)

    }

}