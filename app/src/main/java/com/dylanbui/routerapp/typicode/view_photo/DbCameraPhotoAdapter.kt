package com.dylanbui.routerapp.typicode.view_photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dylanbui.android_library.photo_gallery.DbPhoto
import com.dylanbui.routerapp.R
import java.io.File
import java.net.URL


class DbCameraPhotoAdapter : RecyclerView.Adapter<DbCameraPhotoAdapter.ImageAlleyHolder>() {

    var listPhotos = mutableListOf<DbPhoto>()

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
        listPhotos.add(image)
        notifyItemInserted(listPhotos.size - 1)
    }

    // override fun getItemCount() = if (listPhotos != null) listPhotos.size else 0
    override fun getItemCount() = listPhotos.size

    override fun onBindViewHolder(holder: ImageAlleyHolder, position: Int) {
        holder.renderView(listPhotos.get(position), position)
    }

    inner class ImageAlleyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgPhoto = itemView.findViewById<ImageView>(R.id.imgPhoto)
        private val imgDelete = itemView.findViewById<ImageView>(R.id.imgDelete)
        private val imgError = itemView.findViewById<ImageView>(R.id.imgError)
        fun renderView(photo: DbPhoto, position: Int) {

            imgError.visibility = View.GONE // if (surveyImage.uploadError) View.VISIBLE else View.GONE

            imgDelete.setOnClickListener {
                if (position < listPhotos.size) {
                    listPhotos.removeAt(position)
                    notifyItemRemoved(position)
                }
            }

            // -- Uu tien load tu duong dan Path --
            if (photo.path != null) {
                Glide.with(imgPhoto.context).load(File(photo.path))
                    .placeholder(R.mipmap.ic_no_image)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(imgPhoto)
            } else {
                if (photo.link != null) {
                    Glide.with(imgPhoto.context).load(photo.link)
                        .placeholder(R.mipmap.ic_no_image)
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(imgPhoto)
                }
            }
        }
    }

}