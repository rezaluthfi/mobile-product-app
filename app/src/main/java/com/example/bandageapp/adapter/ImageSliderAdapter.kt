package com.example.bandageapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandageapp.R
import com.example.bandageapp.databinding.ItemSliderBinding

class ImageSliderAdapter(
    private val imageUrls: List<String>,
    private val onThumbnailClick: (Int) -> Unit
) : RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageSliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.binding.imgSlider)

        holder.binding.root.setOnClickListener {
            onThumbnailClick(position)
        }
    }

    override fun getItemCount(): Int = imageUrls.size

    inner class ImageSliderViewHolder(val binding: ItemSliderBinding) :
        RecyclerView.ViewHolder(binding.root)
}
