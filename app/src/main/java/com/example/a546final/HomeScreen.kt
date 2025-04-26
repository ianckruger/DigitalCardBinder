package com.example.a546final

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeScreenViewModel) {

    // get photos from photo repo
    // val photos by photoViewModel.photos.collectAsState()
    // might change to reflect room usage

    Column(
        modifier = Modifier.padding(16.dp)
    ) {


        Row() {

            Button(onClick = {

            }) {
                Text("Upload New Card")
            }

            Button(onClick = {

            }) {
                Text("View Collection")
            }
        }
    }
}