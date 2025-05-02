package com.example.a546final

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BinderScreen(navController: NavController, homeScreenViewModel: HomeScreenViewModel, photoViewModel: PhotoViewModel) {

    val photos by photoViewModel.photos.observeAsState(emptyList())
    var selectedPhoto by remember { mutableStateOf<Photo?>(null) }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(title = {  Text("View Photos") })
        }
    ){
        paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Text("Total Photos: ${photos.size}",
                modifier = Modifier
                    .padding(8.dp)
                    .align(alignment = Alignment.CenterHorizontally))

            Button( onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()){
                Text("Back")
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(photos) { photo ->
                    Card(
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPhoto = photo }
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
            }
            selectedPhoto?.let { photo ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f))
                        .clickable { selectedPhoto = null }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(photo.uri),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}



//
//@Composable
//fun CardItem(photo: Photo, onClick: () -> Unit) {
//    Card (
//        elevation = CardDefaults.cardElevation(4.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() },
//    ) {
//        Image(
//            painter = rememberAsyncImagePainter(photo.uri),
//            contentDescription = "",
//            modifier = Modifier
//                .background(Color.White)
//                .fillMaxWidth()
//        )
//    }
//
//}
