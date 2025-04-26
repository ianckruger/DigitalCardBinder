package com.example.a546final

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.compose.rememberAsyncImagePainter

@Composable
fun PhotoList(
    photos: List<Photo>,
    // onClickPhoto
    modifier: Modifier = Modifier

) {
    Box {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(2)
        ) {
            items(photos) { photo ->
                CardItem(photo) {

                }

            }
        }

        // Enlarge the card if selected

    }
}

@Composable
fun CardItem(photo: Photo, onClick: () -> Unit) {
    Card(

    ) {
        Image(
            painter = rememberAsyncImagePainter(photo.uri),
            contentDescription = "",
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
        )
    }

}