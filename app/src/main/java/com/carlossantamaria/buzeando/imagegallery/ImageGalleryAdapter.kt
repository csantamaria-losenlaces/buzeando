package com.carlossantamaria.buzeando.imagegallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.R
import com.carlossantamaria.buzeando.objects.Image

class ImageGalleryAdapter(private val listaImagenes: List<Image>) :
    RecyclerView.Adapter<ImageGalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageGalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ImageGalleryViewHolder(view)
    }

    override fun getItemCount(): Int = listaImagenes.size

    override fun onBindViewHolder(holder: ImageGalleryViewHolder, position: Int) {
        holder.drawViews(listaImagenes[position])
    }

}