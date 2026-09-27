package com.example.ecommerceshoppingapp1.adapter

import com.example.ecommerceshoppingapp1.model.Product


import com.example.ecommerceshoppingapp1.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceshoppingapp1.utils.loadFromAsset
import java.util.Locale

class ProductAdapter(
    private val products: List<Product>,
    private val isGrid: Boolean = false,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        if (!isGrid) {
            val params = view.layoutParams
            params.width = (175 * parent.context.resources.displayMetrics.density).toInt()
            view.layoutParams = params
        }
        return ProductViewHolder(
            view,
            onProductClick
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(
        view: View,
        private val onClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
        private val tvTitle: TextView = view.findViewById(R.id.tvProductTitle)
        private val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
        private val ivStar1: ImageView = view.findViewById(R.id.ivStar1)
        private val ivStar2: ImageView = view.findViewById(R.id.ivStar2)
        private val ivStar3: ImageView = view.findViewById(R.id.ivStar3)
        private val tvDiscount: TextView = view.findViewById(R.id.tvDiscount)
        private val btnFavorite: android.widget.ImageButton = view.findViewById(R.id.btnFavorite)
        private val btnAddToCartQuick: android.widget.ImageButton = view.findViewById(R.id.btnAddToCartQuick)

        fun bind(product: Product) {
            ivProduct.loadFromAsset(product.imagePath)
            tvTitle.text = product.title
            tvPrice.text = String.format(Locale.US, "$%.2f", product.price)
            
            val activeColor = android.graphics.Color.parseColor("#FFC107")
            val inactiveColor = androidx.core.content.ContextCompat.getColor(itemView.context, R.color.indicator_inactive)
            
            // Map the current float rating data to 1, 2, or 3 for demonstration
            val idMod = product.id % 3
            val ratingInt = if (idMod == 0) 3 else if (idMod == 1) 2 else 1
            
            ivStar1.setColorFilter(if (ratingInt >= 1) activeColor else inactiveColor)
            ivStar2.setColorFilter(if (ratingInt >= 2) activeColor else inactiveColor)
            ivStar3.setColorFilter(if (ratingInt >= 3) activeColor else inactiveColor)
            
            if (product.discount > 0) {
                tvDiscount.visibility = View.VISIBLE
                tvDiscount.text = "-${product.discount}%"
            } else {
                tvDiscount.visibility = View.GONE
            }
            
            updateFavoriteIcon(product.isFavorite)
            
            btnFavorite.setOnClickListener {
                product.isFavorite = !product.isFavorite
                updateFavoriteIcon(product.isFavorite)
                
                val dbHelper = com.example.ecommerceshoppingapp1.database.DatabaseHelper(itemView.context)
                dbHelper.toggleFavorite(product.id, product.isFavorite)
            }
            
            btnAddToCartQuick.setOnClickListener {
                val dbHelper = com.example.ecommerceshoppingapp1.database.DatabaseHelper(itemView.context)
                val success = dbHelper.addToCart(product.id, 1, "40", "White / Black") // default size/color
                if (success) {
                    android.widget.Toast.makeText(itemView.context, "Added to Cart!", android.widget.Toast.LENGTH_SHORT).show()
                    try {
                        val activity = itemView.context as? androidx.appcompat.app.AppCompatActivity
                        val bottomNav = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
                        if (bottomNav != null) {
                            bottomNav.selectedItemId = R.id.cartFragment
                        } else {
                            androidx.navigation.Navigation.findNavController(itemView).navigate(R.id.cartFragment)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            
            itemView.setOnClickListener { onClick(product) }
        }
        
        private fun updateFavoriteIcon(isFav: Boolean) {
            if (isFav) {
                btnFavorite.setImageResource(R.drawable.favorite_24)
                btnFavorite.setColorFilter(androidx.core.content.ContextCompat.getColor(itemView.context, R.color.primary_orange))
            } else {
                btnFavorite.setImageResource(R.drawable.favorite_border_24)
                btnFavorite.setColorFilter(androidx.core.content.ContextCompat.getColor(itemView.context, R.color.text_grey))
            }
        }
    }
}




