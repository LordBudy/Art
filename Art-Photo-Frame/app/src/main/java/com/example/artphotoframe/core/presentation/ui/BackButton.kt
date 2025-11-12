package com.example.artphotoframe.core.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.artphotoframe.core.presentation.ui.theme.Orange

@Composable
fun BackButton(
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier = Modifier
){

    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIosNew,  // стрелка назад
            contentDescription = "назад",
            tint = Orange
        )
    }
}