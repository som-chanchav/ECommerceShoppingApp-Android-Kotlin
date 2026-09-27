package com.example.ecommerceshoppingapp1.adapter

import com.example.ecommerceshoppingapp1.model.CartItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceshoppingapp1.R
import com.example.ecommerceshoppingapp1.utils.loadFromAsset
import java.util.Locale

class CartAdapter(
    private var cartItems: MutableList<CartItem>,
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    private val onItemDeleted: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateData(newItems: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivProd: ImageView = view.findViewById(R.id.ivCartProduct)
        private val tvTitle: TextView = view.findViewById(R.id.tvCartProductTitle)
        private val tvAttributes: TextView = view.findViewById(R.id.tvAttributes)
        private val tvPrice: TextView = view.findViewById(R.id.tvCartProductPrice)
        private val tvQty: TextView = view.findViewById(R.id.tvCartQuantity)
        
        private val btnPlus: TextView = view.findViewById(R.id.btnPlus)
        private val btnMinus: TextView = view.findViewById(R.id.btnMinus)
        private val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)

        fun bind(item: CartItem) {
            ivProd.loadFromAsset(item.product.imagePath)
            tvTitle.text = item.product.title
            
            // Format attributes
            val attrParts = mutableListOf<String>()
            if (item.size.isNotEmpty()) attrParts.add("Size: ${item.size}")
            if (item.color.isNotEmpty()) attrParts.add("Color: ${item.color}")
            tvAttributes.text = attrParts.joinToString(" • ")
            if (attrParts.isEmpty()) tvAttributes.visibility = View.GONE
            else tvAttributes.visibility = View.VISIBLE

            tvPrice.text = String.format(Locale.US, "$%.2f", item.product.price)
            tvQty.text = item.quantity.toString()

            btnPlus.setOnClickListener {
                onQuantityChanged(item, item.quantity + 1)
            }

            btnMinus.setOnClickListener {
                if (item.quantity > 1) {
                    onQuantityChanged(item, item.quantity - 1)
                }
            }

            btnDelete.setOnClickListener {
                onItemDeleted(item)
            }
        }
    }
}



