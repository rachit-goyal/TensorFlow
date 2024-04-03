package com.plcoding.landmarkrecognitiontensorflow.domain

import android.graphics.Bitmap

/**
created by Rachit on 4/2/2024.
 */
interface LandMarkClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}