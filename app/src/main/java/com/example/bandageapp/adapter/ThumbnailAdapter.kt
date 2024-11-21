package com.example.bandageapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandageapp.databinding.ItemThumbnailBinding

class ThumbnailAdapter(
    private val imageUrls: List<String>,
    private val onThumbnailClick: (Int) -> Unit
) : RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailViewHolder {
        val binding = ItemThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThumbnailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThumbnailViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.binding.imgThumbnail)

        holder.binding.root.setOnClickListener {
            onThumbnailClick(position)
        }
    }

    override fun getItemCount(): Int = imageUrls.size

    inner class ThumbnailViewHolder(val binding: ItemThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root)
}
