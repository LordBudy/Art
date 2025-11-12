package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.artphotoframe.core.data.models.Picture

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    picture: Picture,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onAddFavorite: (Picture) -> Unit,
    onRemoveFavorite: (Picture) -> Unit,
    onUpdateFavorite: (Picture) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Кнопка "Назад"
        BackButton(onClick = onBack)

        // Небольшой отступ между стрелкой и сердцем
        androidx.compose.foundation.layout.Spacer(Modifier.padding(horizontal = 8.dp))

        // Кнопка "Избранное"
        BtnFavorite(
            picture = picture,
            isFavorite = isFavorite,
            onAddToFavorites = onAddFavorite,
            onRemoveFromFavorites = onRemoveFavorite,
            onUpdateFavorites = onUpdateFavorite
        )
    }
}