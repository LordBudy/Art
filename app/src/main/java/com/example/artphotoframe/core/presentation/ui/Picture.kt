package com.example.artphotoframe.core.presentation.ui

import android.widget.ImageButton
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.artphotoframe.R

@Composable
fun Picture(
    text: String,
    onValueChange: (String) -> Unit,
    @DrawableRes image: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .height(80.dp)
            .padding(start = 10.dp, top = 30.dp, end = 10.dp)
            .fillMaxWidth()
            .background(color = Color.White)
    ) {

        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .clickable { /* Действие при нажатии */ }
                .background(
                    color = Color.Gray,
                    shape = CircleShape
                )
        ) {
            AsyncImage(
                model = image,
                contentDescription = "searchImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(45.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Prev() {
    val textState = remember { mutableStateOf("Введите название") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White )
    ) {
        Picture(
            text = textState.value,
            onValueChange = { newText ->
                textState.value = newText
            },
            image = R.drawable.search
        )
    }
}