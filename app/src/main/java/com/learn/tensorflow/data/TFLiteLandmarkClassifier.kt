package com.plcoding.landmarkrecognitiontensorflow.data

import android.content.Context
import android.graphics.Bitmap
import android.view.Surface
import com.plcoding.landmarkrecognitiontensorflow.domain.Classification
import com.plcoding.landmarkrecognitiontensorflow.domain.LandMarkClassifier
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

/**
created by Rachit on 4/2/2024.
 */
class TFLiteLandmarkClassifier(
    private val context: Context,
    private val threshold: Float = .5f,
    private val maxResult: Int = 1,
) : LandMarkClassifier {

    private var classifier: ImageClassifier? = null

    private fun setUpClassifier() {

        val baseOption = BaseOptions.builder().setNumThreads(2).build()
        val options = ImageClassifier.ImageClassifierOptions.builder().setBaseOptions(baseOption)
            .setMaxResults(maxResult).setScoreThreshold(threshold).build()

        try {
            classifier = ImageClassifier.createFromFileAndOptions(context, "1.tflite", options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun classify(bitmap: Bitmap, rotation: Int): List<Classification> {
        if (classifier == null) {
            setUpClassifier()
        }

        val imageProcessor = ImageProcessor.Builder().build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
        val imageProcessing =
            ImageProcessingOptions.builder().setOrientation(getImageOrientation(rotation)).build()

        val result = classifier?.classify(tensorImage, imageProcessing)

        return result?.flatMap { classifications ->
            classifications.categories.map {
                Classification(name = it.displayName, score = it.score)
            }
        }?.distinctBy { it.name } ?: emptyList()
    }

    private fun getImageOrientation(rotation: Int): ImageProcessingOptions.Orientation? {
        return when (rotation) {
            Surface.ROTATION_0 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }
}
