package com.example.artphotoframe.core.presentation.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.artphotoframe.R
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme

@Composable
fun FastSearch(
    text: String,
    onValueChange: (String) -> Unit,
    @DrawableRes image: Int
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(start = 10.dp, top = 30.dp, end = 10.dp)
            .height(100.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {

        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
//                .background(
//                    color = MaterialTheme.colorScheme.surface
//                )
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 10.dp, end = 10.dp)
                .size(50.dp)
                .clickable { /* Действие при нажатии */ }
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape

                )
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "searchImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)

            )
        }
    }
}

@PreviewLightDark
@Composable
fun Prev() {
    val textState = remember { mutableStateOf("Введите название") }
    ArtPhotoFrameTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.onSurface)
        ) {
            FastSearch(
                text = textState.value,
                onValueChange = { newText ->
                    textState.value = newText
                },
                image = R.drawable.search
            )
        }
    }
}