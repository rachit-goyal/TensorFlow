package com.learn.tensorflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.learn.tensorflow.ui.CameraScreen
import com.plcoding.landmarkrecognitiontensorflow.ui.theme.LandmarkRecognitionTensorflowTheme

class Main : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            LandmarkRecognitionTensorflowTheme {
                var isClicked by remember {
                    mutableStateOf(false)
                }
                if (isClicked) {
                    CameraScreen {
                        isClicked = false
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { isClicked = true }, modifier = Modifier) {
                            Text(text = "Open Camera")
                        }
                    }

                }

            }
        }
    }
}