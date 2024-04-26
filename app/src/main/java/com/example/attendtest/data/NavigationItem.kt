package com.example.attendtest.data

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem (
    @StringRes val titleResId: Int,
    val description: String,
    val itemId: String,
    val icon: ImageVector
)