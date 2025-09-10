package com.example.artphotoframe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.artphotoframe.ui.theme.ArtPhotoFrameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()// для включения других функций Edge-to-Edge при необходимости
        setContent {
            MyComposeApp()
        }
    }
}
@Composable
fun MyComposeApp() {
    // Apply systemBarsPadding to a container that wraps your content
    Box(
        modifier = Modifier
            .fillMaxSize()
            //добавляет отступы для системных панелей (строка состояния, панель навигации).
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Text("Hello Compose World!")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyComposeApp()
}