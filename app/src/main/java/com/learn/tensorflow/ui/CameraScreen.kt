package com.learn.tensorflow.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.plcoding.landmarkrecognitiontensorflow.data.TFLiteLandmarkClassifier
import com.plcoding.landmarkrecognitiontensorflow.domain.Classification
import com.plcoding.landmarkrecognitiontensorflow.presentation.CameraPreview
import com.plcoding.landmarkrecognitiontensorflow.presentation.LandMarkAnalyser
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

/**
created by Rachit on 4/2/2024.
 */
@Composable
fun CameraScreen(onFinish:()->Unit){
    val context = LocalContext.current

    if (!hasCameraPermission()) {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CAMERA), 0)
    }
    var ticks by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            if (ticks == 5) {
                onFinish()
            }
            delay(1.seconds)
            ticks++
        }
    }
    var classification by remember {
        mutableStateOf(emptyList<Classification>())
    }

    val analyser = remember {
        LandMarkAnalyser(
            classifier = TFLiteLandmarkClassifier(context = context),
            onResult = {
                classification = it
            }
        )
    }
    val controller = remember {
        LifecycleCameraController(context).apply {

            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                analyser
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())
        Text(
            text = "$ticks",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
            , fontSize = 30.sp, textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {

            classification.forEach {
                Text(
                    text = it.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }

}

@Composable
fun hasCameraPermission() = ContextCompat.checkSelfPermission(
    LocalContext.current, Manifest.permission.CAMERA
) == PackageManager.PERMISSION_GRANTED