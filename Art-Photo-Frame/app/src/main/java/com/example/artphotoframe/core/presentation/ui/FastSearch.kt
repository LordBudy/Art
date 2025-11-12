package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme

@Composable
fun FastSearch(
    text: String,
    onValueChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {

        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                onValueChange(newText)},
            maxLines = 1,
            placeholder = { Text(text = "Введите название") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // ← кнопка «Поиск»
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick(text)         // ← запускаем поиск
                    keyboardController?.hide()  // ← прячем клавиатуру (по желанию)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        IconButton(
            onClick = { onSearchClick(text) }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.outlineVariant

            )
        }
    }
}

@PreviewLightDark
@Composable
fun FastSearchPreview() {
    val textState = remember { mutableStateOf("Введите название") }

    ArtPhotoFrameTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            FastSearch(
                text = textState.value,
                onValueChange = { newText ->
                    textState.value = newText
                },
                onSearchClick = {},
            )
        }
    }
}