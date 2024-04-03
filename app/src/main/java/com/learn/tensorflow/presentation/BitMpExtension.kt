package com.plcoding.landmarkrecognitiontensorflow.presentation

import android.graphics.Bitmap

/**
created by Rachit on 4/2/2024.
 */
fun Bitmap.centerCrop(dWidth: Int, dHeight: Int): Bitmap {


    val xstart = (width - dWidth) / 2
    val ystart = (height - dHeight) / 2

    /*if (xstart > 0 || ystart < 0 || dWidth > width || dHeight > height) {
        throw IllegalArgumentException("Invalid Argument")
    }*/

    return Bitmap.createBitmap(this, xstart, ystart, dWidth, dHeight)
}