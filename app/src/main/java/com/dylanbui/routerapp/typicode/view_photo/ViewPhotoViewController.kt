package com.dylanbui.routerapp.typicode.view_photo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.dylanbui.android_library.DbMessageEvent

import com.dylanbui.android_library.doPostNotification
import com.dylanbui.android_library.photo_gallery.DbPhoto
import com.dylanbui.android_library.photo_gallery.DbPhotoPickerActivity
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.typicode.ApplicationRoute
import com.dylanbui.routerapp.typicode.post.PostDetailViewController
import com.dylanbui.routerapp.utils.defaultPushController
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode



// Define enum nhu la 1 data class
sealed class DbPhotoEvent: DbMessageEvent {
    class capturePhotoComplete(val path: String) : DbPhotoEvent()
    class capturePhotoError(val errorCode: Int) : DbPhotoEvent()
}

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
    lateinit var txtPreview: TextView

    var mediaPath: String? = null
    var chooseFile: Uri? = null


    override fun setTitle(): String? = "ViewPhoto"

    override fun createPresenter(): ViewPhotoPresenter = ViewPhotoPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_view_photo, container, false)
    }

    override fun onViewBound(view: View) {
        txtPreview = view.findViewById(R.id.txtPreview) as TextView
        txtPreview.text = "None"

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
            this.mainActivity?.checkPermissions(permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),
                onPermissionResult = { permissionResult ->
                    // handle permission result
                    dLog("getGranted: " + permissionResult.getGranted().toString())
                    dLog("getDenied: " + permissionResult.getDenied().toString())
                    if(permissionResult.areAllGranted()){
                        // Do the task now
                        activity?.toast("Permissions granted.")
                    }else{
                        activity?.toast("Permissions denied.")
                    }
                }, requestCode = 222)

//            this.mainActivity?.rxPermissions?.request(
//                Manifest.permission.CAMERA
//                , Manifest.permission.WRITE_EXTERNAL_STORAGE
//                , Manifest.permission.READ_EXTERNAL_STORAGE
//            )?.subscribe({ grand ->
//                if (grand) {
//                    activity?.toast("Da chap nha quyen")
//                } else {
//                    activity?.toast("Tu choi quyen TRUY CAP")
//                }
//            })
        }

        btnPickImage = view.findViewById(R.id.btnChoosePhoto)
        btnPickImage.setOnClickListener { _ ->
            // Toast.makeText(applicationContext, "show ra loi", Toast.LENGTH_LONG).show()
            // Toast.makeText(activity, "show ra loi", Toast.LENGTH_LONG).show()
            this.mainActivity?.checkPermissions(permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),
                onPermissionResult = { permissionResult ->
                    // handle permission result
                    dLog("getGranted: " + permissionResult.getGranted().toString())
                    dLog("getDenied: " + permissionResult.getDenied().toString())
                    if(permissionResult.areAllGranted()){
                        // Do the task now
                        activity?.toast("Permissions granted.")
                    }else{
                        activity?.toast("Permissions denied.")
                    }
                }, requestCode = 333)

//            this.mainActivity?.rxPermissions?.request(
//                Manifest.permission.CAMERA
//                , Manifest.permission.WRITE_EXTERNAL_STORAGE
//                , Manifest.permission.READ_EXTERNAL_STORAGE
//            )?.subscribe({ grand ->
//                if (grand) {
//                    this.choosePhotoPicker()
//                } else {
//                    activity?.toast("Tu choi quyen TRUY CAP")
//                }
//            })


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

    override fun onDetach(view: View) {
        super.onDetach(view)

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

                EventBus.getDefault().post(DbPhotoEvent.capturePhotoComplete(photo.path!!))
                // doPostNotification(DbPhotoEvent.capturePhotoError(arrPhoto.size))
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
                    // activity?.toast(photo.path ?: "")
                    photo.loadToImageView(imgView, activity)

                    doPostNotification(DbPhotoEvent.capturePhotoComplete(photo.path!!))
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    //fun onMessageEvent(mes: DbMessageEvent) {
    fun onMessageEvent(mes: DbPhotoEvent) {
        when (mes) {
            is DbPhotoEvent.capturePhotoComplete -> {
                txtPreview.text = "capturePhotoComplete: " + mes.path
                activity?.toast("capturePhotoComplete: " + mes.path)
            }
            is DbPhotoEvent.capturePhotoError -> {
                txtPreview.text = "capturePhotoError: " + mes.errorCode
                activity?.toast("capturePhotoError: " + mes.errorCode)
            }
        }
    }

}

