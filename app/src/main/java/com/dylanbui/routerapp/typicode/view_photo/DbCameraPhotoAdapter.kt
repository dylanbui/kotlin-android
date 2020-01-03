package com.dylanbui.routerapp.typicode.view_photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dylanbui.android_library.photo_gallery.DbPhoto
import com.dylanbui.routerapp.R


class DbCameraPhotoAdapter : RecyclerView.Adapter<DbCameraPhotoAdapter.ImageAlleyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAlleyHolder {
        return ImageAlleyHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_camera_add_photo_adapter,
                parent,
                false
            )
        )

    }

    fun addImage(image: DbPhoto) {
        surveyImages?.add(image)
        notifyItemInserted(surveyImages.size - 1)
    }

    override fun getItemCount() = if (surveyImages != null) surveyImages.size else 0

    override fun onBindViewHolder(holder: ImageAlleyHolder, position: Int) {
        holder.renderView(surveyImages.get(position), position)
    }

    var surveyImages = mutableListOf<DbPhoto>()

    inner class ImageAlleyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgPhoto = itemView.findViewById<ImageView>(R.id.imgPhoto)
        private val imgDelete = itemView.findViewById<ImageView>(R.id.imgDelete)
        private val imgError = itemView.findViewById<ImageView>(R.id.imgError)
        fun renderView(surveyImage: DbPhoto, position: Int) {

//            errorView.visibility = if (surveyImage.uploadError) View.VISIBLE else View.GONE
//            if (surveyImage.link != null && surveyImage.link!!.length > 5) {
//                ImageLoader.loadImageFromUrl(
//                    surveyImage.link,
//                    image.context,
//                    R.drawable.ic_no_image,
//                    image
//                )
//            } else if (surveyImage.path != null && surveyImage.path!!.length > 5) {
//                Glide.with(image.context).load(File(surveyImage.path))
//                    .placeholder(R.mipmap.ic_no_image)
//                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .centerCrop()
//                    .into(image)
//            }
//            btnDelete.setOnClickListener {
//                if (position < surveyImages.size) {
//                    surveyImages!!.removeAt(position)
//                    notifyItemRemoved(position)
//                }
//            }
        }
    }

}