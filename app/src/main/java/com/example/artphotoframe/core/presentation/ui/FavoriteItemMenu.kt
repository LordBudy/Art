package com.example.artphotoframe.core.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.artphotoframe.core.domain.wallpaper.WallpaperTarget
import com.example.artphotoframe.core.presentation.ui.theme.Orange
import com.example.artphotoframe.R
@Composable
fun FavoriteItemMenu(
    onAction: (WallpaperTarget) -> Unit
) {
    var menuOpening by remember { mutableStateOf(false) }
    var showSubmenu by remember { mutableStateOf(false) }
    //Кнопка открытия меню
    IconButton(onClick = {
        menuOpening = true
        showSubmenu = false
    }
    ) {
        //стандартная иконка "три вертикальные точки" MoreVert
        Icon(Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.menu_title),
            tint = Orange
        )
    }
    // Выпадающее меню
    DropdownMenu(
        expanded = menuOpening,
        onDismissRequest = {
            menuOpening = false
            showSubmenu = false
        }) {

        if (!showSubmenu) {
            // --- Первый уровень ---
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_set_wallpaper)) },
                onClick = { showSubmenu = true }
            )
        } else {
            // --- Подменю ---
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_back)) },
                onClick = { showSubmenu = false }
            )
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_wallpaper_home)) },
                onClick = {
                    menuOpening = false; showSubmenu = false
                    onAction(WallpaperTarget.HOME)
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_wallpaper_lock)) },
                onClick = {
                    menuOpening = false; showSubmenu = false
                    onAction(WallpaperTarget.LOCK)
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_wallpaper_both)) },
                onClick = {
                    menuOpening = false; showSubmenu = false
                    onAction(WallpaperTarget.BOTH)
                }
            )
        }
    }
}
