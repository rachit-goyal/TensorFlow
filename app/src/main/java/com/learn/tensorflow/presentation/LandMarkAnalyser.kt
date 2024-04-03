package com.plcoding.landmarkrecognitiontensorflow.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.plcoding.landmarkrecognitiontensorflow.domain.Classification
import com.plcoding.landmarkrecognitiontensorflow.domain.LandMarkClassifier

/**
created by Rachit on 4/2/2024.
 */
class LandMarkAnalyser(
    private val classifier: LandMarkClassifier,
    private val onResult: (List<Classification>) -> Unit,
) : ImageAnalysis.Analyzer {
    private var frameSkipConter=0
    override fun analyze(image: ImageProxy) {
        if(frameSkipConter%60==0) {
            val rotation = image.imageInfo.rotationDegrees
            val bitmap = image.toBitmap().centerCrop(321, 321)
            val result = classifier.classify(bitmap, rotation)
            onResult(result)
        }
        frameSkipConter++
        image.close()
    }
}