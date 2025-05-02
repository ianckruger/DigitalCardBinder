package com.example.a546final

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import java.io.File

@Composable
fun CameraScreen(navController: NavController, homeScreenViewModel: HomeScreenViewModel, photoViewModel: PhotoViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember { ImageCapture.Builder().build() }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var isCameraInitialized by remember { mutableStateOf(false) }
    var isCameraBound by remember { mutableStateOf(false) }

    // Permission handling
    var hasCameraPermission by remember { mutableStateOf(checkCameraPermission(context)) }

    if (!hasCameraPermission) {
        // Request permission
        LaunchedEffect(Unit) {
            requestCameraPermission(context) { granted ->
                hasCameraPermission = granted
            }
        }
        Text("Camera permission required.")
        return
    }

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
                        isCameraBound = true
                    } catch (e: Exception) {
                        Log.e("CameraScreen", "Use case binding failed", e)
                        isCameraBound = false
                    }

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Button(
            onClick = {
                if (isCameraBound) {
                    takePhoto(imageCapture, context) { photo ->
                        homeScreenViewModel.addPhotoToDatabase(photo)
                        navController.popBackStack()
                    }
                } else {
                    Log.e("CameraScreen", "Camera is not bound. Cannot take photo.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
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

private fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

private fun requestCameraPermission(context: Context, onPermissionResult: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as android.app.Activity,
            arrayOf(Manifest.permission.CAMERA),
            0
        )
    } else {
        onPermissionResult(true)
    }
}