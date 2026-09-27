package com.example.ecommerceshoppingapp1.model

data class Order(
    val id: Int,
    val userEmail: String,
    val productTitle: String,
    val price: Double,
    val imagePath: String,
    val quantity: Int,
    val orderDate: String,
    val size: String,
    val color: String
)
