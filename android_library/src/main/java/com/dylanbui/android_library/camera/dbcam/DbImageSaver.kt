package com.dylanbui.android_library.camera.dbcam

import android.media.Image
import android.util.Log
import android.util.Size

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Long.signum
import java.util.Comparator

/**
 * Compares two `Size`s based on their areas.
 */
internal class DbCompareSizesByArea_sdd : Comparator<Size> {

    // We cast here to ensure the multiplications won't overflow
    override fun compare(lhs: Size, rhs: Size) =
        signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)

}

/**
 * Saves a JPEG [Image] into the specified [File].
 */
internal class DbImageSaver(
    /**
     * The JPEG image
     */
    private val image: Image,

    /**
     * The file we save the image into.
     */
    private val file: File
) : Runnable {

    override fun run() {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        var output: FileOutputStream? = null
        try {
            output = FileOutputStream(file).apply {
                write(bytes)
            }
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        } finally {
            image.close()
            output?.let {
                try {
                    it.close()
                } catch (e: IOException) {
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

    companion object {
        /**
         * Tag for the [Log].
         */
        private val TAG = "DbImageSaver"
    }
}