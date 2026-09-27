package com.example.ecommerceshoppingapp1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceshoppingapp1.databinding.ItemOrderHistoryBinding
import com.example.ecommerceshoppingapp1.model.Order
import com.example.ecommerceshoppingapp1.utils.loadFromAsset
import java.util.Locale

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(private val binding: ItemOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.tvTitle.text = order.productTitle
            binding.tvDetails.text = "Size: ${order.size} | Color: ${order.color} | Qty: ${order.quantity}"
            binding.tvDate.text = order.orderDate
            binding.tvPrice.text = String.format(Locale.US, "$%.2f", order.price * order.quantity)
            binding.imgProduct.loadFromAsset(order.imagePath)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size
}
