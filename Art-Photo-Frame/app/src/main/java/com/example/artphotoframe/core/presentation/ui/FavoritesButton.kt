package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme


@Composable
fun FavoritesButton(
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier
) {
    Box(
        contentAlignment = BottomEnd ,
        modifier = Modifier.fillMaxSize()
            .padding(15.dp)
    ) {
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .width(140.dp).height(40.dp)
                .border(width = 2.dp, color = Color.Green,
                    shape = RoundedCornerShape(20.dp))

        ){
            Text(
                text = "⭐ Избранное",
                color = color,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .clickable(onClick = onClick)

            )
        }

    }
}
@PreviewLightDark
@Composable
fun PreviewFavoritesButton() {

    ArtPhotoFrameTheme {
        Box(

            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            FavoritesButton(onClick = {},
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
            )
        }
    }
}