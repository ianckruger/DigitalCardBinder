package com.example.a546final

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.camera.view.PreviewView
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File

@Composable
fun CameraScreen(navController: NavController, homeScreenViewModel: HomeScreenViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember { ImageCapture.Builder().build() }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var isCameraInitialized by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isCameraInitialized) {
            Text("Initializing Camera...")
        } else {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = androidx.camera.core.Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    try {
                        val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        Log.e("CameraScreen", "Use case binding failed", e)
                    }

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Button(
            onClick = {
                takePhoto(imageCapture, context) { photo ->
                    homeScreenViewModel.addPhotoToDatabase(photo)
                    navController.popBackStack() // Navigate back to HomeScreen after saving
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Capture Photo")
        }
    }

    LaunchedEffect(cameraProviderFuture) {
        isCameraInitialized = cameraProviderFuture.isDone
    }
}

private fun takePhoto(imageCapture: ImageCapture, context: Context, onPhotoSaved: (Photo) -> Unit) {
    val photoFile = File(context.cacheDir, "photo-${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val photo = Photo(name = "New Card", uri = photoFile.toURI().toString())
                onPhotoSaved(photo)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraScreen", "Photo capture failed: ${exception.message}")
            }
        }
    )
}
