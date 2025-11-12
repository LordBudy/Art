package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.artphotoframe.R

@Composable
fun BottomSheetInfo(
    title: String?,
    description: String?
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = title ?: stringResource(R.string.no_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        Text(
            text = description ?: stringResource(R.string.no_description),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}