package com.dylanbui.android_library.photo_gallery

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dylanbui.android_library.R
import kotlinx.android.synthetic.main.adapter_photo_picker_image_item.view.*


class DbPhotoPickerAdapter(private val myImages: MutableList<DbPhoto>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_CAMERA = 0
    private val TYPE_IMAGE = 1
    private var binding = true
    private var width = 0
    private var chooseImageListener: DbChoosePhotoListener? = null

    private var numImageCanChoose = 1
    private var numImageChoosed = 0

    fun setNumImageCanChoose(numImageCanChoose: Int) {
        this.numImageCanChoose = numImageCanChoose
    }

    fun setChooseImageListener(chooseImageListener: DbChoosePhotoListener) {
        this.chooseImageListener = chooseImageListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        if (viewType == TYPE_CAMERA)
            view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_photo_picker_camera_item, parent, false)
        else
            view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_photo_picker_image_item, parent, false)
        //fix heigh item
        width = parent.measuredWidth / 3
        val params = view!!.layoutParams as RecyclerView.LayoutParams
        params.height = width
        params.width = width
        view.layoutParams = params
        return if (viewType == TYPE_CAMERA)
            CameraViewHolder(view)
        else
            ImageViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            TYPE_CAMERA
        else
            TYPE_IMAGE
    }

    fun addItemInfirst(myImage: DbPhoto) {
        myImages!!.add(0, myImage)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (position == 0) {
                var cameraViewHolder = holder as CameraViewHolder
                cameraViewHolder.image!!.setOnClickListener { chooseImageListener?.onOpenCamera() }
                cameraViewHolder.rootView!!.setOnClickListener { chooseImageListener?.onOpenCamera() }
            } else {
                var imageViewHolder = holder as ImageViewHolder
                binding = true
                imageViewHolder.checkBox!!.isChecked = myImages!![position - 1].isChoosed
                binding = false
                Glide.with(imageViewHolder.image!!.context).load(myImages[position - 1].link)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop().into(imageViewHolder.image)
                if (myImages[position - 1].indexChoosed > 0) {
                    imageViewHolder.tvNumber!!.setVisibility(View.VISIBLE)
                    imageViewHolder.tvNumber!!.setText("${myImages[position - 1].indexChoosed}")
                } else {
                    imageViewHolder.tvNumber!!.setVisibility(View.GONE)
                }
                imageViewHolder.checkBox!!.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (!binding) {
                        try {
                            if (isChecked && numImageChoosed >= numImageCanChoose) {
                                imageViewHolder.checkBox!!.isChecked = false
                                showMessageExceedImage(imageViewHolder.checkBox!!.context, numImageCanChoose)
                                return@setOnCheckedChangeListener
                            }

                            if (isChecked) {
                                numImageChoosed++
                                myImages[position - 1].indexChoosed = numImageChoosed
                                imageViewHolder.tvNumber!!.setVisibility(View.VISIBLE)
                                imageViewHolder.tvNumber!!.setText(numImageChoosed.toString() + "")
                                myImages[position - 1].isChoosed = true
                            } else {
                                numImageChoosed--
                                imageViewHolder.tvNumber!!.setVisibility(View.GONE)

                                //notifyDataSetChanged();
                            }

                            if (chooseImageListener != null)
                                chooseImageListener!!.onChecked(isChecked, position, myImages[position - 1], imageViewHolder)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                }
            }
        } catch (e: Exception) {

        }

    }

    private fun showMessageExceedImage(context: Context, numImageCanChoose: Int) {
        // DialogUtil.createDialogNotiOneButton(context, SweetAlertDialog.WARNING_TYPE, context.resources.getString(R.string.max_choose) + " " + numImageCanChoose + " " + context.resources.getString(R.string.image), context.resources.getString(R.string.close), false, null)
    }

    override fun getItemCount(): Int {
        return if (myImages == null) 1 else myImages.size + 1
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rootView = itemView.findViewById<ViewGroup>(R.id.root_view_item) // .root_view_item
        var image = itemView.findViewById<ImageView>(R.id.image_item) // image_item
        var checkBox = itemView.findViewById<CheckBox>(R.id.ckb) //ckb
        var tvNumber = itemView.findViewById<TextView>(R.id.tvNumber)// tvNumber
    }

    inner class CameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rootView = itemView.findViewById<ViewGroup>(R.id.root_view) //.root_view
        var image = itemView.findViewById<ImageView>(R.id.image) //.image
    }

    interface DbChoosePhotoListener {
        fun onOpenCamera()
        fun onChecked(isChecked: Boolean, position: Int, myImage: DbPhoto, imageViewHolder: ImageViewHolder)
    }
}