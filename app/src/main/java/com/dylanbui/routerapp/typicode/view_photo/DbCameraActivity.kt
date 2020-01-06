package com.dylanbui.routerapp.typicode.view_photo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.media.Image
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dylanbui.android_library.camera.ezcam.EZCam
import com.dylanbui.android_library.camera.ezcam.EZCamCallback
import com.dylanbui.android_library.photo_gallery.DbAutoFitTextureView
import com.dylanbui.android_library.photo_gallery.DbPhoto
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.routerapp.R
import com.tbruyelle.rxpermissions2.RxPermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DbCameraActivity : AppCompatActivity(), EZCamCallback {

    private var dateFormat: SimpleDateFormat? = null
    private val TAG = "CAM"
    
    // -- Intent Params --
    var minTotalItem = 1
    var maxTotalItem = 1
    var folderName: String? = null
    var pathName: String? = null
    var filePrefix: String = "my_camera_"

    private lateinit var ezCam: EZCam
    private lateinit var listImage: RecyclerView
    private lateinit var textureView: DbAutoFitTextureView
    private lateinit var mAdapter: DbCameraPhotoAdapter

    private lateinit var takePictureButton: View
    private lateinit var deletePictureButton: View
    private lateinit var savePictureButton: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2)

        minTotalItem = intent.getIntExtra("minTotalItem", 2)
        maxTotalItem = intent.getIntExtra("maxTotalItem", 6)
        pathName = intent.getStringExtra("pathName")
        folderName = intent.getStringExtra("folderName")
        filePrefix = intent.getStringExtra("filePrefix")

        textureView = findViewById<DbAutoFitTextureView>(R.id.textureView)
        dateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        ezCam = EZCam(this)
        ezCam?.let {
            it.setCameraCallback(this)
            val id = it.camerasList.get(CameraCharacteristics.LENS_FACING_BACK)
            it.selectCamera(id)
        }

        listImage = findViewById<RecyclerView>(R.id.listImage)
        listImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mAdapter = DbCameraPhotoAdapter()
        listImage.adapter = mAdapter

        takePictureButton = findViewById<View>(R.id.takePictureButton)
        takePictureButton.setOnClickListener { //mSpinnerTakePicture?.onNext(200)
            if (mAdapter.listPhotos.size < maxTotalItem) {
                this.allowCameraControl(false)
                ezCam?.takePicture()
            } else {
                //var message = getString(R.string.str_max_limit_image, maxTotalItem.toString())
                showToast("Loi chup qua nhieu hinh !!")
            }
        }

        deletePictureButton = findViewById<View>(R.id.deletePictureButton)
        deletePictureButton.setOnClickListener { onBackPressed() }

        savePictureButton = findViewById<View>(R.id.savePictureButton)
        savePictureButton.setOnClickListener {
            if (mAdapter.listPhotos.size >= minTotalItem) {
                this.returnIntentResult()
            } else {
                // var message = getString(R.string.str_min_limit_image, minTotalItem.toString())
                var message = "Chua dat gioi han photo"
                showToast(message)
            }
        }

        if (maxTotalItem == 1) {
            // -- Hide List Image --
            listImage.visibility = ViewGroup.GONE
            var viewControl = findViewById<RelativeLayout>(R.id.viewControl)
            viewControl.layoutParams.height -= listImage.layoutParams.height //Utils.dpToPx(110)
            viewControl.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        this.allowCameraControl(false)

        RxPermissions(this)
            .request(
                Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .subscribe({ grand ->
                if (grand) {
                    ezCam?.open(CameraDevice.TEMPLATE_PREVIEW, textureView)
                }
            })

    }

    override fun onCameraReady() {
        ezCam?.setCaptureSetting(
            CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE,
            CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY
        )
        ezCam?.startPreview()

        this.allowCameraControl(true)
    }

    private fun returnIntentResult() {
        val returnIntent = Intent()
        // returnIntent.putExtra("result", result)
        returnIntent.putParcelableArrayListExtra("results", ArrayList(mAdapter.listPhotos))
        setResult(Activity.RESULT_OK, returnIntent)
        onBackPressed()
    }

    private fun showToast(text: String) {
        runOnUiThread { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }
    }

    private fun makeFile(): File {
        // -- Code demo thu muc --
        // [Bo nho trong]/Pictures
        var folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (pathName != null) {
            folder = File(pathName)
        }
        val storageDir = File(folder, folderName)

        if (!storageDir.exists()){
            if (!storageDir.mkdirs()) {
                // Khong tao duoc thu muc
            }
        }

        // -- Tao file tam de luu --
        val filename = filePrefix + System.currentTimeMillis() + ".jpg"
        return File(storageDir, filename)
    }

    override fun onPicture(image: Image) {

        try {
            var file = makeFile()
            EZCam.saveImage(image, file)

            // -- Save to Library --
            // -- Sau khi luu vao Pictures, thi duong dan anh tren khong con ton tai --
//            val galleryIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//            val picUri = Uri.fromFile(file)
//            galleryIntent.data = picUri
//            this.sendBroadcast(galleryIntent)

            var image = DbPhoto(file.path)
            addImageToList(image)
            dLog(file.path)

        } catch (e: IOException) {
            Log.e(TAG, e.message)
        }

        this.allowCameraControl(true)

        // -- Truong hop chi tra ve 1 tam anh --
        if (maxTotalItem == 1) {
            ezCam?.stopPreview()
            this.returnIntentResult()
            return
        }
        // -- Restart review camera --
        // ezCam?.restartPreview()
    }

    override fun onCameraDisconnected() {
        Log.e(TAG, "Camera disconnected")
    }

    override fun onError(message: String) {
        Log.e(TAG, message)
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        ezCam?.close()
        super.onDestroy()
    }

    private fun allowCameraControl(allow: Boolean) {
        // deletePictureButton.isClickable = allow
        takePictureButton.isClickable = allow
        savePictureButton.isClickable = allow
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

