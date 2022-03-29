package com.example.helpers.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation


@Composable
fun ImageBuilder(size: Dp, url: String) {
    Image(
        painter = rememberImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(data = url)
                .size(Int.MAX_VALUE)
                .build()
        ),
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .padding(4.dp),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ImageBuilderCircle(size: Dp, url: String) {
    Image(
        painter = rememberImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(data = url)
                .size(Int.MAX_VALUE)
                .transformations(CircleCropTransformation())
                .build()
        ),
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .padding(4.dp),
        contentScale = ContentScale.Crop,
    )
}