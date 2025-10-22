package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight


@Composable
fun FavoritesButton(
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier
) {
    Text(
        text = "⭐ Избранное",
        color = color,
        fontWeight = FontWeight.Bold,
        modifier = modifier.clickable(onClick = onClick)

    )
}