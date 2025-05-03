package com.example.a546final

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BinderScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel,
    photoViewModel: PhotoViewModel
) {

    val photos by photoViewModel.photos.observeAsState(emptyList())
    var selectedPhoto by remember { mutableStateOf<Photo?>(null) }
    var editingName by remember { mutableStateOf<String?>(null)}
    var search by remember { mutableStateOf("") }

    val filteredPhotos = photos.filter {
        it.name.contains(search, ignoreCase = true)
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("View Photos") })
        }
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()) {
                // Display the number of photos
                Text(
                    text = "Number of Photos: ${photos.size}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(8.dp)
                ){
                    Button(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Text("Back")
                    }

                    TextField(
                        value = search,
                        onValueChange = { search = it },
                        label = {Text("Search")},
                        trailingIcon = {
                            if(search.isNotEmpty()) {
                                IconButton(onClick = { search = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        }
                    )

                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(filteredPhotos) { photo ->
                        val isEditing = editingName == photo.name
                        var name by remember { mutableStateOf(photo.name)}
                        // Debug statement: log the photo's description
                        Log.d("BinderScreen", "Photo Description: ${photo.name}")
                        Card(
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPhoto = photo }
                                .padding(8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Use the byte array to decode into a Bitmap, then to an ImageBitmap
                                Image(
                                    bitmap = BitmapFactory.decodeByteArray(
                                        photo.image,
                                        0,
                                        photo.image.size
                                    ).asImageBitmap(),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .background(Color.White)
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                )

                                if (isEditing){
                                    TextField(
                                        value = name,
                                        onValueChange = {name = it},
                                        singleLine = true,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            imeAction = ImeAction.Done),
                                        keyboardActions = KeyboardActions(
                                            onDone= { editingName = null
                                            photo.name = name})
                                    )
                                } else {
                                    Text(
                                        text = photo.name,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                editingName = photo.name
                                            }
                                    )
                                }

                            }
                        }
                    }
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
                    bitmap = BitmapFactory.decodeByteArray(photo.image, 0, photo.image.size).asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}