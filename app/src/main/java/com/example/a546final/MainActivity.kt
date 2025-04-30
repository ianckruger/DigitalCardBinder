package com.example.a546final

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraSelector: CameraSelector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Camera Executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Setup camera
        askPermissions()

        setContent {
            CardApp() // Your existing UI code
        }
    }

    // Camera functionality

    private fun startCamera() {
        Log.d("CameraDebug", "startCamera() called")
        // Check and retrieve camera IDs
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIds = cameraManager.cameraIdList
        Log.d("CameraDebug", "Available camera IDs: ${cameraIds.joinToString()}")

        // If there are no cameras available, log and return
        if (cameraIds.isEmpty()) {
            Log.e("CameraDebug", "No cameras available on the device.")
            throw Exception("No cameras found")
        }

        // We will create a camera selector that will select any camera
        // We know there is at least 1 camera, because we just checked
        cameraSelector = CameraSelector.Builder().build()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            try {
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector
                )

            } catch (exc: Exception) {
                Log.e("CameraDebug", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    // Permission functionality

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your app.
            startCamera()
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    }

    private fun askPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                startCamera()
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's not granted. In this UI, include a
                // "OK" or "Got it" button that allows the user to continue.
            }

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}