package com.dylanbui.routerapp.typicode.view_photo

import android.Manifest
import android.app.Activity
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.OrientationEventListener
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.dylanbui.android_library.camera.ezcam.EZCam
import com.dylanbui.android_library.camera.ezcam.EZCamCallback
import com.dylanbui.android_library.photo_gallery.DbPhoto
import com.dylanbui.android_library.utils.ManagePermissions
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.typicode.AppCoordinator
import com.tbruyelle.rxpermissions2.RxPermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DbCameraActivity : Activity(), EZCamCallback {


    private var dateFormat: SimpleDateFormat? = null
    // internal var mSpinnerTakePicture = PublishSubject.create<Int>()
    private val TAG = "CAM"
    var minTotalItem = 2
    var maxTotalItem = 6

    private var mOrientation: Int = 0
    private var imageOrientation = 1

    var folder = ""
    var typeImage = ""

    private lateinit var ezCam: EZCam
    private lateinit var listImage: RecyclerView
    private lateinit var textureView: TextureView
    private lateinit var mAdapter: DbCameraPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2)

        minTotalItem = intent.getIntExtra("minTotalItem", 2)
        maxTotalItem = intent.getIntExtra("maxTotalItem", 6)
        folder = intent.getStringExtra("folder")
        typeImage = intent.getStringExtra("typeImages")


        textureView = findViewById<TextureView>(R.id.textureView)
        dateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        ezCam = EZCam(this)
        ezCam.setCameraCallback(this)

        val id = ezCam.camerasList.get(CameraCharacteristics.LENS_FACING_BACK)
        ezCam.selectCamera(id)

        listImage = findViewById<RecyclerView>(R.id.listImage)
        listImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mAdapter = DbCameraPhotoAdapter()
        listImage.adapter = mAdapter

        val takePictureButton = findViewById<View>(R.id.takePictureButton)
        takePictureButton.setOnClickListener { //mSpinnerTakePicture?.onNext(200)
        }

        val deletePictureButton = findViewById<View>(R.id.deletePictureButton)
        deletePictureButton.setOnClickListener { onBackPressed() }

        val savePictureButton = findViewById<View>(R.id.savePictureButton)
        savePictureButton.setOnClickListener {
            if (checkFileError()) {
                // var message = getString(R.string.str_image_error)
                var message = "Co loi xay ra"
                showToast(message)
            } else if (mAdapter!!.surveyImages!!.size >= minTotalItem) {
//                val returnIntent = Intent()
//                returnIntent.putExtra("listImage", Gson().toJson(mAdapter!!.surveyImages!!))
//                setResult(RESULT_OK, returnIntent)
                onBackPressed()
            } else {
                // var message = getString(R.string.str_min_limit_image, minTotalItem.toString())
                var message = "Dat gioi han photo"
                showToast(message)
            }
        }
    }

    private fun checkFileError(): Boolean {
//        for (image in mAdapter!!.surveyImages) {
//            if (image.uploadError) return true
//        }
        return false
    }

    override fun onCameraReady() {
        ezCam.setCaptureSetting(
            CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE,
            CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY
        )
        ezCam.startPreview()

//        mSpinnerTakePicture.debounce(200, TimeUnit.MILLISECONDS).subscribe { data: Int ->
//            if (mAdapter != null && mAdapter!!.surveyImages!!.size < maxTotalItem) {
//                imageOrientation = mOrientation
//                cam!!.takePicture(getOrientation(imageOrientation))
//            } else {
//                var message = getString(R.string.str_max_limit_image, maxTotalItem.toString())
//                showToast(message)
//            }
//        }


    }

    private fun getOrientation(orientation: Int): Int {
        if (orientation == 1) {
            return 0
        } else if (orientation == 6) {
            return 90
        } else if (orientation == 3) {
            return 180
        } else if (orientation == 8) {
            return 270
        }
        return 0
    }

    private fun showToast(text: String) {
        runOnUiThread { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }
    }

    override fun onPicture(image: Image) {
        ezCam.restartPreview()
        try {
            // var wallpaperDirectory = File("/sdcard/PROPZY/${SurveyApp.imageFolder}/$typeImage/");
            var wallpaperDirectory = File("/sdcard/PROPZY/SurveyApp/$typeImage/");
            wallpaperDirectory.mkdirs()
            val filename =
                "survey_" + typeImage + "_" + System.currentTimeMillis() + ".jpg"
            var file = File(wallpaperDirectory, filename)
            EZCam.saveImage(image, file)

//            var orientation = Util.changeExifData(file.path, imageOrientation)
//            var image = SurveyImage(file.path)
//            image.order = System.currentTimeMillis()
//            image.orientation = orientation
//            image.uploadError = !(file.exists() && file.length() > 0)
            var image = DbPhoto(file.path)
            addImageToList(image)

        } catch (e: IOException) {
            Log.e(TAG, e.message)
        }

    }

    override fun onCameraDisconnected() {
        Log.e(TAG, "Camera disconnected")
    }

    override fun onError(message: String) {
        Log.e(TAG, message)
    }

    override fun onDestroy() {
        ezCam.close()
        super.onDestroy()
    }
    override fun onResume() {
        super.onResume()
        var mOrientationListener = object : OrientationEventListener(applicationContext) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation <= 45) {
                    mOrientation = ExifInterface.ORIENTATION_ROTATE_90;
                } else if (orientation <= 135) {
                    mOrientation = ExifInterface.ORIENTATION_ROTATE_180;
                } else if (orientation <= 225) {
                    mOrientation = ExifInterface.ORIENTATION_ROTATE_270;
                } else if (orientation <= 315) {
                    mOrientation = ExifInterface.ORIENTATION_NORMAL;
                } else {
                    mOrientation = ExifInterface.ORIENTATION_ROTATE_90;
                }
            }

        }

        if (mOrientationListener.canDetectOrientation()) {
            mOrientationListener.enable()
        }
    }

    // private var mHandler = Handler(Looper.getMainLooper())
    private fun addImageToList(image: DbPhoto) {
        mAdapter?.addImage(image)
//        mHandler.post {
//            if (mAdapter?.surveyImages!!.size == 0) {
//                image.latitude = LocationService.mLastLocation?.latitude
//                image.longitude = LocationService.mLastLocation?.longitude
//            } else {
//                image.latitude = mAdapter?.surveyImages!![0].latitude
//                image.longitude = mAdapter?.surveyImages!![0].longitude
//            }
//            mAdapter?.addImage(image)
//        }
    }
}

