package com.example.helpers.ui.components.posts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.helpers.models.domain.model.PostEntry

@Composable
fun ContentPost(modifier: Modifier=Modifier, postEntry: PostEntry) {
    Column(modifier = modifier) {
        Column(modifier = Modifier.padding(8.dp)) {
            TitlePost(postEntry.title)
            TextContent(postEntry.content)
        }
        Spacer(modifier = Modifier.height(4.dp))
    }

}

@Composable
fun ImageContent(url: String) {

    val painter = rememberImagePainter(
        data = url,
        builder = {
            crossfade(true)
            size(Int.MAX_VALUE)
        })
    Image(
        painter = painter,
        contentDescription = null,
        Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun TextContent(text: String) {
    Text(
        text = text,
        color = Color.DarkGray,
        fontSize = 14.sp,
        maxLines = 3
    )
}

@Composable
fun TitlePost(text: String) {
    Text(
        text = text,
        maxLines = 3,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = Color.Black,
    )
}