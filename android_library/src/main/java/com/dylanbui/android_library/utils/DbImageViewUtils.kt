package com.dylanbui.android_library.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface

@SuppressLint("all")
object DbImageViewUtils {

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        var matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    fun getExactllyImageRotate(photoPath: String, bitmap: Bitmap): Bitmap? {
        var ei = ExifInterface(photoPath)
        var orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        var rotatedBitmap: Bitmap? = null
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270f)
            else -> rotatedBitmap = bitmap
        }
        return rotatedBitmap
    }

    fun getImageOrientation(imagePath: String): Int {
        var rotate = 0
        try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rotate
    }

    fun changeExifData(imagePath: String, orientation: Int): Int {
        val exif = ExifInterface(imagePath)
        val exifOrientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION)
        if (exifOrientation != ExifInterface.ORIENTATION_UNDEFINED.toString()) {
            return orientation
        }
        return ExifInterface.ORIENTATION_NORMAL
    }

}