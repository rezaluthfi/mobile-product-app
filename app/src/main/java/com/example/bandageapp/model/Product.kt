package com.example.bandageapp.model

import java.io.Serializable

data class Product(
    val images: List<String>? = null,
    val category: String? = null,
    val old_price: Double? = null,
    val new_price: Double? = null,
    val details: ProductDetails? = null
) : Serializable

data class ProductDetails(
    val name: String? = null,
    val reviews: Int? = null,
    val price: Double? = null,
    val description: String? = null,
    val colors: List<String>? = null
) : Serializable
