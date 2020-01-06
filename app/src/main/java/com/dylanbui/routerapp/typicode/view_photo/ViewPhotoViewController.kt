package com.dylanbui.routerapp.typicode.view_photo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dylanbui.android_library.photo_gallery.DbPhoto
import com.dylanbui.android_library.photo_gallery.DbPhotoPickerActivity
import com.dylanbui.android_library.photo_gallery.DbPhotoPickerAdapter
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.android_library.utils.getPathString
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.networking.UploadViewController
import com.dylanbui.routerapp.typicode.view_photo.ViewPhotoPresenter
import com.dylanbui.routerapp.typicode.view_photo.ViewPhotoViewAction
import com.google.gson.Gson
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.tbruyelle.rxpermissions2.RxPermissions

class ViewPhotoViewController : BaseMvpController<ViewPhotoViewAction, ViewPhotoPresenter>(),
    ViewPhotoViewAction {

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        //Permission code
        private const val PERMISSION_CODE = 1001
        // Camera code
        private const val CAMERA_CODE = 1002
    }

    lateinit var btnPermission: Button
    lateinit var btnUpload: Button
    lateinit var btnPickImage: Button
    lateinit var btnCamera: Button
    lateinit var imgView: ImageView

    var mediaPath: String? = null
    var chooseFile: Uri? = null


    override fun setTitle(): String? = "ViewPhoto"

    override fun createPresenter(): ViewPhotoPresenter = ViewPhotoPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_view_photo, container, false)
    }

    override fun onViewBound(view: View) {
        imgView = view.findViewById(R.id.preview) as ImageView

        btnUpload = view.findViewById(R.id.btnUpload)
        btnUpload.setOnClickListener { _ ->

        }

        btnCamera = view.findViewById(R.id.btnCamera)
        btnCamera.setOnClickListener { _ ->
            var i = Intent(activity, DbCameraActivity::class.java)
            i.putExtra("minTotalItem", 2)
            i.putExtra("maxTotalItem",6)
            // i.putExtra("pathName", "ducbui") // Duong dan
            i.putExtra("folderName", "listing222") // Ten folder
            i.putExtra("filePrefix", "dylan___") // Ten folder
            startActivityForResult(i, CAMERA_CODE)
        }

        btnPermission = view.findViewById(R.id.btnPermission)
        btnPermission.setOnClickListener { _ ->
            this.mainActivity?.rxPermissions?.request(
                Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
            )?.subscribe({ grand ->
                if (grand) {
                    activity?.toast("Da chap nha quyen")
                } else {
                    activity?.toast("Tu choi quyen TRUY CAP")
                }
            })

        }

        btnPickImage = view.findViewById(R.id.btnChoosePhoto)
        btnPickImage.setOnClickListener { _ ->
            // Toast.makeText(applicationContext, "show ra loi", Toast.LENGTH_LONG).show()
            // Toast.makeText(activity, "show ra loi", Toast.LENGTH_LONG).show()

            this.mainActivity?.rxPermissions?.request(
                Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
            )?.subscribe({ grand ->
                if (grand) {
                    this.choosePhotoPicker()
                } else {
                    activity?.toast("Tu choi quyen TRUY CAP")
                }
            })


        }

//        RxPermissions(activity as FragmentActivity)
//            .request(
//                Manifest.permission.CAMERA
//                , Manifest.permission.WRITE_EXTERNAL_STORAGE
//                , Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//            .subscribe({ grand ->
//                if (grand) {
//                    getPresenter().queryImage(this)
//                } else getPresenter().finishChooseImage()
//            })
    }

    override fun onPreAttach() {
        // -- At here presenter is existed --
        // -- Get action in row => presenter --
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
    }

    private fun choosePhotoPicker() {
        // Default PhotoPicker
//        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        galleryIntent.type = "image/*"
//        startActivityForResult(galleryIntent, ViewPhotoViewController.IMAGE_PICK_CODE)

//        val intent = Intent(activity, DbPhotoPickerActivity::class.java)
//        startActivityForResult(intent, ViewPhotoViewController.IMAGE_PICK_CODE)

        val intent = Intent(activity, DbPhotoPickerActivity::class.java)
        intent.putExtra("numImageCanChoose", 10)
        startActivityForResult(intent, ViewPhotoViewController.IMAGE_PICK_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) run {
            if (data != null) {
                var changeAvatarPath: String = data.getStringExtra("result")

                // imgView.setImageURI(changeAvatarPath)
//                Glide.with(this).load(getPresenter().changeAvatarPath).skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(image)
                activity?.toast(changeAvatarPath)

                var arrPhoto: ArrayList<DbPhoto> = data.getParcelableArrayListExtra("results")
                dLog("arrPhoto.count() = ${arrPhoto.count()}")
                for (i in 0 until arrPhoto.count()) {
                    dLog("link = ${arrPhoto[i].path}")
                }

                var photo: DbPhoto = arrPhoto[0]
                photo.loadToImageView(imgView, activity)


            }
        }

        if (resultCode == MvpActivity.RESULT_OK && requestCode == CAMERA_CODE) run {
            if (data != null) {
                try {

                    var arrPhoto: ArrayList<DbPhoto> = data.getParcelableArrayListExtra("results")
                    dLog("arrPhoto.count() = ${arrPhoto.count()}")
                    for (i in 0 until arrPhoto.size) {
                        dLog("link = ${arrPhoto[i].path}")
                    }

                    var photo: DbPhoto = arrPhoto[0]
                    activity?.toast(photo.path ?: "")
                    photo.loadToImageView(imgView, activity)

//                    val json = data.getStringExtra("listImage")
//                    dLog(json)
//                    val listImage: MutableList<SurveyImage> =
//                        Gson().fromJson(
//                            data?.getStringExtra("listImage"),
//                            Array<SurveyImage>::class.java
//                        )
//                            .toMutableList()
//                    imageAlleys.addAll(listImage)
//
//                    updateLocationAllImage()
//
//                    ifViewAttached { v -> v.addDataAdapterAlleyImage(listImage) }

                } catch (e: Exception) {

                }

            }
        }


//        if (resultCode == Activity.RESULT_OK && requestCode == ViewPhotoViewController.IMAGE_PICK_CODE){
//            chooseFile = data?.data
//            mediaPath = chooseFile?.getPathString(activity!!.baseContext)
//            imgView.setImageURI(chooseFile)
//        }

    }
}

