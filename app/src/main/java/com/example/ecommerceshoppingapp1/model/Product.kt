package com.example.ecommerceshoppingapp1.model

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val category: String,
    val imagePath: String,
    val rating: Float,
    val description: String,
    val discount: Int = 0,
    val isPopular: Boolean = false,
    var isFavorite: Boolean = false
)
