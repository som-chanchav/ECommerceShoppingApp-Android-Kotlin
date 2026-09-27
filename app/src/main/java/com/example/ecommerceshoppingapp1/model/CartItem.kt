package com.example.ecommerceshoppingapp1.model




data class CartItem(
    val cartId: Int,
    val product: Product,
    var quantity: Int,
    val size: String,
    val color: String
)



