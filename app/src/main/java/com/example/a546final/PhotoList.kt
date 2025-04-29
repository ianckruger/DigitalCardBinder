package com.example.a546final

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PhotoList(
    photos: List<Photo>,
    onClickPhoto: (Photo) -> Unit,
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(2)
        ) {
            items(photos) { photo ->
                CardItem(photo) {
                    onClickPhoto(photo)
                }

            }
        }

        // Enlarge the card if selected

}
