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
            .load(item.src)
            .resize(226, 170)
            .centerCrop()
            .into(ivImagen)
    }
}