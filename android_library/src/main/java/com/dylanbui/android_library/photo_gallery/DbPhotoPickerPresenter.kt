package com.dylanbui.android_library.photo_gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class DbPhoto(var link: String? = null, var isChoosed: Boolean = false, var indexChoosed: Int = 0)

interface DbPhotoPickerViewAction : MvpView {
    fun showButtonDone(isShow: Boolean)
    fun scanImage()
    fun finishChooseImage(result: String)
    fun setDataAdapter(myImages: MutableList<DbPhoto>, numImageCanChoose: Int)
    fun addImageToFirstList(myImage: DbPhoto)
    fun updateNumberOfPositionCheckedItem(position: Int, newIndex: Int)
    fun updateNumberImageChoosedInHeader(numImageChoosed:Int)
    fun openCameraIntent(outputFileUri: Uri, resultCode: Int)
}

class DbPhotoPickerPresenter: MvpBasePresenter<DbPhotoPickerViewAction>() {

    var count: Int = 0
    var arrPath = ArrayList<String>()
    var numImageCanChoose = 1
    var numImageChoosed = 0
    var imagePathsResult = ArrayList<String>()
    var myImages = ArrayList<DbPhoto>()
    var avataFile: File? = null
    val TAKE_PHOTO_CODE = 0

    fun getDataIntent(b: Bundle?) {
        b?.let {
            if (it.containsKey("numImageCanChoose"))
                numImageCanChoose = it.getInt("numImageCanChoose")
        }
        ifViewAttached {
            if (numImageCanChoose > 1)
                it.showButtonDone(true)
            else
                it.showButtonDone(false)
        }
    }

    fun finishChoosePhoto() {
        ifViewAttached {
            var result = ""
            if (imagePathsResult.size >= 1)
                result = imagePathsResult[0]
            if (imagePathsResult.size > 1)
                for (i in 1 until imagePathsResult.size)
                    result = result + "|" + imagePathsResult[i]
            it.finishChooseImage(result)
        }
    }

    fun queryImage(context: Context) {
        val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
        val orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        val imageCursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
            null, null, orderBy)
        val imageColumnIndex = imageCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
        this.count = imageCursor!!.getCount()
        //this.thumbnails = new Bitmap[this.count];

        for (i in 0 until this.count) {
            imageCursor.moveToPosition(i)
            val id = imageCursor.getInt(imageColumnIndex)
            val dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)
            arrPath.add(imageCursor.getString(dataColumnIndex))
        }
        for (i in arrPath.indices)
            myImages.add(DbPhoto(arrPath[i]))

        ifViewAttached { v -> v.setDataAdapter(myImages, numImageCanChoose) }
    }

    fun updateNumberPhotoChoosedInHeader() {
        ifViewAttached { it.updateNumberImageChoosedInHeader(numImageChoosed) }
    }

    fun onItemImageChecked(isChecked: Boolean, position: Int, photo: DbPhoto) {
        ifViewAttached {
            if (isChecked) {
                imagePathsResult.add(photo.link!!)
                numImageChoosed++
            } else {
                imagePathsResult.remove(photo.link!!)
                numImageChoosed--
            }
            it.updateNumberImageChoosedInHeader(numImageChoosed)
            // nếu chỉ chọn 1 tấm thì sau khi chọn trả về kết quả luôn
            if (numImageCanChoose == 1)
                finishChoosePhoto()
            // nếu bỏ check thì bỏ check và số thứ tự của ảnh hiện tại và update lại thứ tự các ảnh đã check khác
            if (!isChecked) {
                val indexUnchecked = myImages[position - 1].indexChoosed
                myImages[position - 1].indexChoosed = 0
                myImages[position - 1].isChoosed = false
                // tất cả các ảnh đã chọn có thứ tự nhỏ hơn thứ tự của ảnh bỏ chọn sẽ giữ nguyên
                // còn các ảnh đã chọn có thứ tự lớn hơn ảnh bỏ chọn thì sẽ cập nhật lại giảm bớt thứ tự đi 1 đơn vị
                for (i in myImages.indices) {
                    try {
                        if (myImages[i].indexChoosed > indexUnchecked) {
                            myImages[i].indexChoosed = myImages[i].indexChoosed - 1
                            it.updateNumberOfPositionCheckedItem(i + 1, myImages[i].indexChoosed)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }

    fun onActivityResult(context: Context, requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PHOTO_CODE && resultCode == MvpActivity.RESULT_OK) {
            ImageCompression(context, 1024f).execute(avataFile?.getAbsolutePath())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCompressImageCompleted(event: CompressImageCompletedEvent) {
        arrPath.add(0, event.imagePath)
        ifViewAttached { v -> v.addImageToFirstList(DbPhoto(event.imagePath)) }
    }

    fun openCamera(activity: Activity) {
        try {
            var outputFileUri: Uri? = null
            avataFile = createImageFile()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                outputFileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider",
                    createImageFile())
            } else
                outputFileUri = Uri.fromFile(avataFile)
            ifViewAttached { v -> v.openCameraIntent(outputFileUri, TAKE_PHOTO_CODE) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createImageFile(): File {
        var image: File? = null
        try {
            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = File(Environment.getExternalStorageDirectory().toString() + "/Pictures")
            if (!storageDir.exists())
                storageDir.mkdirs()
            image = File(storageDir.absolutePath + "/avata_diy.jpg")
            // Save a file: path for use with ACTION_VIEW intents
            return image
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return image!!
    }

}