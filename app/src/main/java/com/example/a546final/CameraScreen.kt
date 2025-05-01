package com.example.a546final

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import java.io.File
import java.util.concurrent.Executor

@Composable
fun CameraScreen(navController: NavController, photoViewModel: PhotoViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val imageCapture = remember { ImageCapture.Builder().build() }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var isCameraInitialized by remember { mutableStateOf(false) }
    var isCameraBound by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(checkCameraPermission(context)) }

    // Request permission if not granted
    if (!hasCameraPermission) {
        LaunchedEffect(Unit) {
            requestCameraPermission(context) { granted ->
                hasCameraPermission = granted
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera permission required.")
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isCameraInitialized) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Initializing Camera...")
            }
        } else {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraExecutor: Executor = ContextCompat.getMainExecutor(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
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
                    }, cameraExecutor)

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Top Back Button
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

        // Bottom Capture Button
        Button(
            onClick = {
                if (isCameraBound) {
                    takePhoto(imageCapture, context) { photo ->
                        photoViewModel.addPhotoToDatabase(photo)
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

    // Camera Initialization Check
    LaunchedEffect(cameraProviderFuture) {
        isCameraInitialized = cameraProviderFuture.isDone
    }
}

private fun takePhoto(imageCapture: ImageCapture, context: Context, onPhotoSaved: (Photo) -> Unit) {
    val photoFile = File(context.filesDir, "photo-${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val photoUri = Uri.fromFile(photoFile)
                val photo = Photo(name = "New Card", uri = photoUri.toString())
                onPhotoSaved(photo)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraScreen", "Photo capture failed: ${exception.message}")
            }
        }
    )
}

// Helper functions for camera permissions
fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

fun requestCameraPermission(context: Context, onPermissionResult: (Boolean) -> Unit) {
    if (!checkCameraPermission(context)) {
        //This is a very basic implementation, you should create an Activity class to handle permissions
        ActivityCompat.requestPermissions(
            context as android.app.Activity,
            arrayOf(Manifest.permission.CAMERA),
            100 // You can choose any unique integer for the request code
        )
        onPermissionResult(checkCameraPermission(context))
    } else {
        onPermissionResult(true)
    }
}