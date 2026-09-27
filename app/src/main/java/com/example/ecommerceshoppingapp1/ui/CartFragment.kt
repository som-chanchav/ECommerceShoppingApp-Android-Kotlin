package com.example.ecommerceshoppingapp1.ui

import com.example.ecommerceshoppingapp1.database.DatabaseHelper
import com.example.ecommerceshoppingapp1.model.CartItem
import com.example.ecommerceshoppingapp1.adapter.CartAdapter


import com.example.ecommerceshoppingapp1.R

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceshoppingapp1.databinding.FragmentCartBinding
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: CartAdapter
`
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
        setupCartList()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnProceedCheckout.setOnClickListener {
            val count = dbHelper.getCartCount()
            if (count > 0) {
                findNavController().navigate(R.id.action_cartFragment_to_checkoutAddressFragment)
            } else {
                android.widget.Toast.makeText(requireContext(), "Your cart is empty", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCartList() {
        val cartItems = dbHelper.getCartItems().toMutableList()
        
        adapter = CartAdapter(cartItems, 
            onQuantityChanged = { item, newQty ->
                if (dbHelper.updateCartQuantity(item.cartId, newQty)) {
                    refreshCart()
                }
            },
            onItemDeleted = { item ->
                if (dbHelper.deleteCartItem(item.cartId)) {
                    refreshCart()
                }
                }
            }
        )
        
        binding.rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartItems.adapter = adapter

        updateTotals(cartItems)
    }
    
    private fun refreshCart() {
        val items = dbHelper.getCartItems()
        adapter.updateData(items)
        updateTotals(items)
        
        // Update top cart badge
        val count = dbHelper.getCartCount()
        binding.tvCartBadge.text = count.toString()
        if (count == 0) {
            binding.tvCartBadge.visibility = View.GONE
        } else {
            binding.tvCartBadge.visibility = View.VISIBLE
        }
        (activity as? HomepageActivity)?.updateCartBadge()
    }

    private fun updateTotals(cartItems: List<CartItem>) {
        var subtotal = 0.0
        var totalItems = 0
        for (item in cartItems) {
            subtotal += item.product.price * item.quantity
            totalItems += item.quantity
        }
        
        binding.tvSubtotalLabel.text = "Subtotal ($totalItems items)"
        binding.tvSubtotalVal.text = String.format(Locale.US, "$%.2f", subtotal)

        // Free delivery rule: Standard shipping is $2.00, Buy 3 items or more -> Free Delivery ($0.00)
        val standardShipping = if (totalItems > 0) 2.0 else 0.0
        val isFreeShippingUnlocked = totalItems >= 3
        val shippingFee = if (isFreeShippingUnlocked) 0.0 else standardShipping
        val total = subtotal + shippingFee

        if (isFreeShippingUnlocked) {
            binding.tvFreeShippingLabel.text = "Buy 3 get Free Delivery"
            binding.tvFreeShippingLabel.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.primary_green))
            binding.tvFreeShippingVal.text = "$0.00 (FREE)"
            binding.tvFreeShippingVal.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.primary_green))
        } else {
            binding.tvFreeShippingLabel.text = "Buy 3 get Free Delivery"
            binding.tvFreeShippingLabel.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.dark_grey))
            binding.tvFreeShippingVal.text = String.format(Locale.US, "$%.2f", standardShipping)
            binding.tvFreeShippingVal.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.black))
        }

        // Update Free Delivery Progress Bar (Max 3 items)
        binding.pbFreeShipping.progress = totalItems.coerceAtMost(3)

        binding.tvTotalVal.text = String.format(Locale.US, "$%.2f", total)
        binding.tvCheckoutPrice.text = String.format(Locale.US, "$%.2f", total)
    }

    override fun onResume() {
        super.onResume()
        refreshCart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





