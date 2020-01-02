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
import com.dylanbui.android_library.photo_gallery.DbPhotoPickerActivity
import com.dylanbui.android_library.photo_gallery.DbPhotoPickerAdapter
import com.dylanbui.android_library.utils.getPathString
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.networking.UploadViewController
import com.dylanbui.routerapp.typicode.view_photo.ViewPhotoPresenter
import com.dylanbui.routerapp.typicode.view_photo.ViewPhotoViewAction
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.tbruyelle.rxpermissions2.RxPermissions

class ViewPhotoViewController : BaseMvpController<ViewPhotoViewAction, ViewPhotoPresenter>(),
    ViewPhotoViewAction {

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        //Permission code
        private const val PERMISSION_CODE = 1001
    }

    lateinit var btnPermission: Button
    lateinit var btnUpload: Button
    lateinit var btnPickImage: Button
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

        val intent = Intent(activity, DbPhotoPickerActivity::class.java)
        startActivityForResult(intent, ViewPhotoViewController.IMAGE_PICK_CODE)

//        val intent = Intent(this, ChooseImageActivity::class.java)
//        intent.putExtra("numImageCanChoose", getPresenter().LIMIT_IMAGES - getPresenter().myImages.size)
//        startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == ViewPhotoViewController.IMAGE_PICK_CODE) run {
            if (data != null) {
                var changeAvatarPath: String = data.getStringExtra("result")

                // imgView.setImageURI(changeAvatarPath)
//                Glide.with(this).load(getPresenter().changeAvatarPath).skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(image)

                activity?.toast(changeAvatarPath)
            }
        }

//        if (resultCode == Activity.RESULT_OK && requestCode == ViewPhotoViewController.IMAGE_PICK_CODE){
//            chooseFile = data?.data
//            mediaPath = chooseFile?.getPathString(activity!!.baseContext)
//            imgView.setImageURI(chooseFile)
//        }

    }
}

