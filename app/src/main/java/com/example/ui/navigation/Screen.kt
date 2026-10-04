package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val route: String,
    val titleAr: String,
    val titleEn: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector
) {
    HOME(
        route = "home",
        titleAr = "الرئيسية",
        titleEn = "Home",
        iconFilled = Icons.Filled.Home,
        iconOutlined = Icons.Outlined.Home
    ),
    YOUTUBE(
        route = "youtube",
        titleAr = "YouTube",
        titleEn = "YouTube",
        iconFilled = Icons.Filled.Movie,
        iconOutlined = Icons.Outlined.Movie
    ),
    TIKTOK(
        route = "tiktok",
        titleAr = "TikTok",
        titleEn = "TikTok",
        iconFilled = Icons.Filled.Smartphone,
        iconOutlined = Icons.Outlined.Smartphone
    ),
    INSTAGRAM(
        route = "instagram",
        titleAr = "Instagram",
        titleEn = "Instagram",
        iconFilled = Icons.Filled.CameraAlt,
        iconOutlined = Icons.Outlined.CameraAlt
    ),
    FACEBOOK(
        route = "facebook",
        titleAr = "Facebook",
        titleEn = "Facebook",
        iconFilled = Icons.Filled.Public,
        iconOutlined = Icons.Outlined.Public
    ),
    SAVED(
        route = "saved",
        titleAr = "المحفوظات",
        titleEn = "Saved",
        iconFilled = Icons.Filled.Bookmark,
        iconOutlined = Icons.Outlined.BookmarkBorder
    ),
    SETTINGS(
        route = "settings",
        titleAr = "الإعدادات",
        titleEn = "Settings",
        iconFilled = Icons.Filled.Settings,
        iconOutlined = Icons.Outlined.Settings
    );

    companion object {
        val bottomNavScreens = listOf(HOME, YOUTUBE, TIKTOK, INSTAGRAM, SAVED, SETTINGS)
    }
}
