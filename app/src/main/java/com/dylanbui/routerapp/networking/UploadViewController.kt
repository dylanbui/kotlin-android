package com.dylanbui.routerapp.networking

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bluelinelabs.conductor.Controller
import com.dylanbui.routerapp.MainActivity
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.toast
import com.dylanbui.routerapp.utils.Utils
import com.dylanbui.routerapp.utils.fromJson
import com.dylanbui.routerapp.utils.getPathString
import java.io.File


class UploadViewController : Controller()
{
    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        //Permission code
        private const val PERMISSION_CODE = 1001
    }

    lateinit var btnUpload: Button
    lateinit var btnPickImage: Button
    lateinit var imgView: ImageView

    var progressDialog: AlertDialog? = null
    var mediaPath: String? = null
    var chooseFile: Uri? = null

    private fun setTitle(): String = "Upload View Controller"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        var view: View = inflater.inflate(R.layout.controller_upload, container, false)
        onViewBound(view)
        return view
    }

    private fun onViewBound(view: View)
    {
        AppNetwork.setBaseUrl("http://45.117.162.50:8080/file/api/")

        imgView = view.findViewById(R.id.preview) as ImageView

        btnUpload = view.findViewById(R.id.upload)
        btnUpload.setOnClickListener { _ ->
            doUpload()
        }

        btnPickImage = view.findViewById(R.id.pick_img)
        btnPickImage.setOnClickListener { _ ->
            // Toast.makeText(applicationContext, "show ra loi", Toast.LENGTH_LONG).show()
            // Toast.makeText(activity, "show ra loi", Toast.LENGTH_LONG).show()

//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, IMAGE_PICK_CODE)

            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, IMAGE_PICK_CODE)

        }
    }

    // Run code, lay du lieu tu Presenter
    override fun onAttach(view: View)
    {
        super.onAttach(view)

        var mainActivity = activity as? MainActivity

        mainActivity?.let {
            it.setToolBarTitle(setTitle())
            it.enableUpArrow(router.backstackSize > 1)
        }

        progressDialog = Utils.makeProgressDialog( activity!!, "Loading..")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            chooseFile = data?.data

            mediaPath = chooseFile?.getPathString(activity!!.baseContext)

            imgView.setImageURI(chooseFile)

        }

//        super.onActivityResult(requestCode, resultCode, data)
//
//        try { // When an Image is picked
//            if (requestCode === 0 && resultCode === RESULT_OK && null != data) { // Get the Image from data
//                val selectedImage: Uri = data.data
//                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//                val cursor: Cursor = activity?.contentResolver?.query(selectedImage, filePathColumn, null, null, null)!!
//                cursor.moveToFirst()
//                val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
//                mediaPath = cursor.getString(columnIndex)
//                // Set the Image in ImageView for Previewing the Media
//                imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
//                cursor.close()
//            } else {
//                Toast.makeText(activity, "You haven't picked Image/Video", Toast.LENGTH_LONG).show()
//            }
//        } catch (e: Exception) {
//            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
//        }

    }

//    fun getRealPathFromUri(contentUri: Uri?): String? {
//        var cursor: Cursor? = null
//        return try {
//            val proj = arrayOf(MediaStore.Images.Media.DATA)
//            cursor = activity?.contentResolver?.query(contentUri, proj, null, null, null)
//            assert(cursor != null)
//            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            cursor?.moveToFirst()
//            cursor?.getString(columnIndex!!)
//        } finally {
//            cursor?.close()
//        }
//    }

    private fun doUpload() {

        progressDialog?.show()

        val file = File(mediaPath)

        var params = DictionaryType()
        params["type"] = "avatar"

        AppNetwork.doUploadFile("upload", "file", file, params, onResponse = { responseBody: CloudResponse ->

            progressDialog?.hide()

            responseBody.data?.let {
                // Convert qua doi tuong can
                 val myList: DictionaryType = fromJson(it)
                activity?.toast("In ra: $myList")

                Log.d("TAG", myList["link"].toString())
            }

        }, onFailed = { error: AppNetworkServiceError ->

            Toast.makeText(activity, "Co loi xay ra: $error", Toast.LENGTH_LONG).show()

        }, onProgress = { process: Float ->
            Log.d("TAG", "----- Process: $process")
        })

    }


}