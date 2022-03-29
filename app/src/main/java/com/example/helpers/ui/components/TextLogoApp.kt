package com.example.helpers.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextLogoApp() {
    Box(modifier = Modifier
        .fillMaxSize()){
        Text(
            text = "Helpers",
            style = TextStyle(
                color = MaterialTheme.colors.primary,
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}