package com.example.artphotoframe.core.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.artphotoframe.core.domain.wallpaper.WallpaperTarget

@Composable
fun FavoriteItemMenu(
    onAction: (WallpaperTarget) -> Unit
) {
    var menuOpening by remember { mutableStateOf(false) }

    //Кнопка открытия меню
    IconButton(onClick = { menuOpening = true }) {
        //стандартная иконка "три вертикальные точки" MoreVert
        Icon(Icons.Default.MoreVert, contentDescription = "Меню")
    }
    // Выпадающее меню
    DropdownMenu(expanded = menuOpening,
        onDismissRequest = { menuOpening = false }) {

        //Элементы меню (пункты)
        DropdownMenuItem(
            text = { Text("На главный экран") },
            onClick = { menuOpening = false; onAction(WallpaperTarget.HOME) }
        )
        DropdownMenuItem(
            text = { Text("На экран блокировки") },
            onClick = { menuOpening = false; onAction(WallpaperTarget.LOCK) }
        )
        DropdownMenuItem(
            text = { Text("На оба экрана") },
            onClick = { menuOpening = false; onAction(WallpaperTarget.BOTH) }
        )
    }
}
