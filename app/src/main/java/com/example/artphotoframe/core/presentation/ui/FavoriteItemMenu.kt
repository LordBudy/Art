package com.example.artphotoframe.core.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.artphotoframe.core.domain.wallpaper.WallpaperTarget
import com.example.artphotoframe.core.presentation.ui.theme.Orange

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
            contentDescription = "Меню",
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
                text = { Text("Установить как обои") },
                onClick = { showSubmenu = true }
            )
        } else {
            // --- Подменю ---
            DropdownMenuItem(
                text = { Text("← Назад") },
                onClick = { showSubmenu = false }
            )
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            DropdownMenuItem(
                text = { Text("На главный экран") },
                onClick = {
                    menuOpening = false; showSubmenu = false
                    onAction(WallpaperTarget.HOME)
                }
            )
            DropdownMenuItem(
                text = { Text("На экран блокировки") },
                onClick = {
                    menuOpening = false; showSubmenu = false
                    onAction(WallpaperTarget.LOCK)
                }
            )
            DropdownMenuItem(
                text = { Text("На оба экрана") },
                onClick = {
                    menuOpening = false; showSubmenu = false
                    onAction(WallpaperTarget.BOTH)
                }
            )
        }
    }
}
