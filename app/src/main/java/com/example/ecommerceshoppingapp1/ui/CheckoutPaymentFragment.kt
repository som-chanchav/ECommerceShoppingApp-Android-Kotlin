package com.example.ecommerceshoppingapp1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceshoppingapp1.R
import com.example.ecommerceshoppingapp1.database.DatabaseHelper
import com.example.ecommerceshoppingapp1.databinding.FragmentCheckoutPaymentBinding

class CheckoutPaymentFragment : Fragment() {

    private var _binding: FragmentCheckoutPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutPaymentBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.cardCod.setOnClickListener {
            selectPaymentMethod(true)
        }

        binding.cardQr.setOnClickListener {
            selectPaymentMethod(false)
        }

        binding.btnPlaceOrder.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)
            val sessionEmail = sharedPref.getString("session_email", "") ?: "guest@gmail.com"
            val emailToUse = if (sessionEmail.isEmpty()) "guest@gmail.com" else sessionEmail

            val cartItems = dbHelper.getCartItems()
            for (item in cartItems) {
                dbHelper.saveOrder(
                    userEmail = emailToUse,
                    productTitle = item.product.title,
                    price = item.product.price,
                    imagePath = item.product.imagePath,
                    quantity = item.quantity,
                    size = item.size,
                    color = item.color
                )
            }

            dbHelper.clearCart()
            (activity as? HomepageActivity)?.updateCartBadge()
            
            Toast.makeText(requireContext(), "Order Placed Successfully!", Toast.LENGTH_LONG).show()
            
            // Navigate back to Home
            findNavController().navigate(R.id.action_checkoutPaymentFragment_to_homeFragment)
        }
    }

    private fun selectPaymentMethod(isCod: Boolean) {
        if (isCod) {
            binding.cardCod.setBackgroundResource(R.drawable.bg_payment_card_selected)
            binding.ivCodRadio.setImageResource(R.drawable.ic_radio_checked_green)
            
            binding.cardQr.setBackgroundResource(R.drawable.bg_payment_card_unselected)
            binding.ivQrRadio.setImageResource(R.drawable.ic_radio_unchecked_grey)
            
            binding.layoutQrImage.visibility = View.GONE
        } else {
            binding.cardQr.setBackgroundResource(R.drawable.bg_payment_card_selected)
            binding.ivQrRadio.setImageResource(R.drawable.ic_radio_checked_green)
            
            binding.cardCod.setBackgroundResource(R.drawable.bg_payment_card_unselected)
            binding.ivCodRadio.setImageResource(R.drawable.ic_radio_unchecked_grey)
            
            binding.layoutQrImage.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
