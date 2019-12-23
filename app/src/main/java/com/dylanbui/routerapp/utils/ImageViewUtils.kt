package com.dylanbui.routerapp.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface

@SuppressLint("all")
object ImageViewUtils {

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

}