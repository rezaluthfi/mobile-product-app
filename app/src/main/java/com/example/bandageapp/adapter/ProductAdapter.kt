package com.example.bandageapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandageapp.R
import com.example.bandageapp.model.Product

import com.example.bandageapp.databinding.ItemProductBinding

class ProductAdapter(private val productList: List<Product>, private val onClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductName.text = product.details?.name
            binding.tvProductCategory.text = product.category
            binding.tvProductPriceOld.text = "$${product.old_price}"
            binding.tvProductPriceNew.text = "$${product.new_price}"

            val imageUrl = product.images?.getOrNull(0)
            Log.d("ProductAdapter", "Image URL: $imageUrl")
            Glide.with(binding.ivProduct.context)
                .load(imageUrl)
                .into(binding.ivProduct)

            binding.root.setOnClickListener {
                onClick(product)
            }
        }
    }
}


