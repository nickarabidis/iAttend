package com.example.attendtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.attendtest.ui.theme.AttendTestTheme
import com.example.attendtest.app.App
import com.example.attendtest.data.user.UserViewModel
import com.example.attendtest.database.DatabaseApp
import com.example.iattend.ui.theme.IAttendTheme


class MainActivity : ComponentActivity() {

    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            DatabaseApp::class.java,
            "databaseUser.db"
        ).build()
    }
    private val viewModel by viewModels<UserViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T: ViewModel> create(modelClass: Class<T>): T{
                    return UserViewModel(db.userDao) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IAttendTheme {
                val state by viewModel.state.collectAsState()
                App()
            }
        }
    }
}