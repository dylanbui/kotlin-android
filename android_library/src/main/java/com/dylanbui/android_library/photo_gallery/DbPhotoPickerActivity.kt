package com.dylanbui.android_library.photo_gallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dylanbui.android_library.R
import com.hannesdorfmann.mosby3.mvp.MvpActivity

class DbPhotoPickerActivity: MvpActivity<DbPhotoPickerViewAction, DbPhotoPickerPresenter>(), DbPhotoPickerViewAction,
    DbPhotoPickerAdapter.DbChoosePhotoListener {

    private lateinit var adapter: DbPhotoPickerAdapter

    private lateinit var tvNumberImageChoosed: TextView
    private lateinit var btnDone: Button
    private lateinit var rvImage: RecyclerView

    override fun createPresenter(): DbPhotoPickerPresenter = DbPhotoPickerPresenter()

    open override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_picker)

        tvNumberImageChoosed = findViewById(R.id.tvNumberImageChoosed)
        btnDone = findViewById(R.id.btnDone)
        rvImage = findViewById(R.id.rvImage)

        // Chay se co loi ve adapter chua duoc tao
    }



    override fun showButtonDone(isShow: Boolean) {
        btnDone.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun scanImage() {

    }

    override fun finishChooseImage(result: String) {

    }

    override fun setDataAdapter(myImages: MutableList<DbPhoto>, numImageCanChoose: Int) {
        adapter = DbPhotoPickerAdapter(myImages)
        adapter.setChooseImageListener(this)
        adapter.setNumImageCanChoose(numImageCanChoose)
        rvImage.layoutManager = GridLayoutManager(this@DbPhotoPickerActivity, 3)
        rvImage.adapter = adapter
    }

    override fun addImageToFirstList(myImage: DbPhoto) {
        adapter?.addItemInfirst(myImage)
        adapter?.notifyDataSetChanged()
    }

    override fun updateNumberOfPositionCheckedItem(position: Int, newIndex: Int) {

    }

    // update lại số ảnh đã chọn trên thanh header
    override fun updateNumberImageChoosedInHeader(numImageChoosed: Int) {
        tvNumberImageChoosed.text = if (numImageChoosed > 0) "($numImageChoosed)" else ""
        btnDone.visibility = if (numImageChoosed > 0) View.VISIBLE else View.GONE
    }

    override fun openCameraIntent(outputFileUri: Uri, resultCode: Int) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
        startActivityForResult(cameraIntent, resultCode)
    }

    override fun onOpenCamera() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChecked(
        isChecked: Boolean,
        position: Int,
        myImage: DbPhoto,
        imageViewHolder: DbPhotoPickerAdapter.ImageViewHolder
    ) {
        presenter.onItemImageChecked(isChecked, position, myImage)
    }

}
