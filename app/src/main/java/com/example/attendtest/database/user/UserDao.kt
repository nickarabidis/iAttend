package com.example.attendtest.database.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.attendtest.database.userSettings.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUser(user: User)

    @Upsert
    suspend fun upsertUserSettings(userSettings: UserSettings)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT firstName FROM user WHERE email = :email")
    fun getFirstName(email: String): String

    @Query("SELECT lastName FROM user WHERE email = :email")
    fun getLastName(email: String): String

    @Query("SELECT email FROM user WHERE email = :email")
    fun getEmail(email: String): String

    @Query("SELECT password FROM user WHERE email = :email")
    fun getPassword(email: String): String

    @Query("SELECT * FROM user ORDER BY firstName ASC")
    fun getUsersOrderedByFirstName(): Flow<List<User>>

    @Query("SELECT * FROM user ORDER BY lastName ASC")
    fun getUsersOrderedByLastName(): Flow<List<User>>

    @Query("SELECT * FROM user ORDER BY email ASC")
    fun getUsersOrderedByEmail(): Flow<List<User>>

    @Query("SELECT * FROM user ORDER BY password ASC")
    fun getUsersOrderedByPassword(): Flow<List<User>>

    @Query("UPDATE UserSettings SET language = :lang WHERE userEmail = :email")
    suspend fun updateLanguage(email: String, lang: String)

    @Query("UPDATE UserSettings SET theme = :theme WHERE userEmail = :email")
    suspend fun updateTheme(email: String, theme: String)

    @Query("SELECT language FROM UserSettings WHERE userEmail = :email")
    fun getLanguage(email: String): String

    @Query("SELECT theme FROM UserSettings WHERE userEmail = :email")
    fun getTheme(email: String): String
}