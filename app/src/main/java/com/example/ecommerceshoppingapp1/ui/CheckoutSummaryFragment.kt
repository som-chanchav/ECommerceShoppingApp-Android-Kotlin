package com.example.ecommerceshoppingapp1.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceshoppingapp1.R
import com.example.ecommerceshoppingapp1.adapter.CheckoutSummaryAdapter
import com.example.ecommerceshoppingapp1.database.DatabaseHelper
import com.example.ecommerceshoppingapp1.databinding.FragmentCheckoutSummaryBinding
import java.util.Locale

class CheckoutSummaryFragment : Fragment() {

    private var _binding: FragmentCheckoutSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutSummaryBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutSummaryFragment_to_checkoutPaymentFragment)
        }

        setupSummary()
    }

    private fun setupSummary() {
        val cartItems = dbHelper.getCartItems()
        val adapter = CheckoutSummaryAdapter(cartItems)
        binding.rvSummaryItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSummaryItems.adapter = adapter

        var subtotal = 0.0
        var totalItems = 0
        for (item in cartItems) {
            subtotal += item.product.price * item.quantity
            totalItems += item.quantity
        }
        val standardShipping = if (totalItems > 0) 2.0 else 0.0
        val isFreeShippingUnlocked = totalItems >= 3
        val shippingFee = if (isFreeShippingUnlocked) 0.0 else standardShipping
        val total = subtotal + shippingFee

        binding.tvSummarySubtotal.text = String.format(Locale.US, "$%.2f", subtotal)

        if (isFreeShippingUnlocked) {
            binding.tvSummaryShippingLabel.text = "Buy 3 get Free Delivery"
            binding.tvSummaryShippingLabel.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.primary_green))
            binding.tvSummaryShipping.text = "$0.00 (FREE)"
            binding.tvSummaryShipping.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.primary_green))
        } else {
            binding.tvSummaryShippingLabel.text = "Buy 3 get Free Delivery"
            binding.tvSummaryShippingLabel.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.dark_grey))
            binding.tvSummaryShipping.text = String.format(Locale.US, "$%.2f", standardShipping)
            binding.tvSummaryShipping.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.black))
        }

        binding.tvSummaryTotal.text = String.format(Locale.US, "$%.2f", total)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
