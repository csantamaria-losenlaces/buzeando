package com.carlossantamaria.buzeando.imagegallery

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.R
import com.carlossantamaria.buzeando.objects.Image
import com.squareup.picasso.Picasso

class ImageGalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val ivImagen: ImageView = view.findViewById(R.id.ivImagen)

    fun drawViews(item: Image) {
        Picasso.get()
            .load("http://77.90.13.129/android/${item.src}")
            .resize(226, 170)
            .centerCrop()
            .into(ivImagen)
    }
}