package com.example.ecommerceshoppingapp1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceshoppingapp1.R
import com.example.ecommerceshoppingapp1.model.CartItem
import com.example.ecommerceshoppingapp1.utils.loadFromAsset
import java.util.Locale

class CheckoutSummaryAdapter(
    private val cartItems: List<CartItem>
) : RecyclerView.Adapter<CheckoutSummaryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_checkout_summary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cartItems[position]
        holder.tvItemQty.text = "Qty: ${item.quantity}"
        holder.tvItemTitle.text = item.product.title
        holder.tvItemPrice.text = String.format(Locale.US, "$%.2f", item.product.price * item.quantity)
        
        holder.ivItemImage.loadFromAsset(item.product.imagePath)
    }

    override fun getItemCount(): Int = cartItems.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItemImage: android.widget.ImageView = view.findViewById(R.id.ivItemImage)
        val tvItemQty: TextView = view.findViewById(R.id.tvItemQty)
        val tvItemTitle: TextView = view.findViewById(R.id.tvItemTitle)
        val tvItemPrice: TextView = view.findViewById(R.id.tvItemPrice)
    }
}
