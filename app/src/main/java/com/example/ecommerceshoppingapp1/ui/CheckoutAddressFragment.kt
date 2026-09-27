package com.example.ecommerceshoppingapp1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceshoppingapp1.R
import com.example.ecommerceshoppingapp1.databinding.FragmentCheckoutAddressBinding

class CheckoutAddressFragment : Fragment() {

    private var _binding: FragmentCheckoutAddressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnNext.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val houseNo = binding.etHouseNo.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || houseNo.isEmpty() || address.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // In a real app we'd pass these values via SafeArgs or ViewModel. 
            // For now, simply navigate to the summary page.
            findNavController().navigate(R.id.action_checkoutAddressFragment_to_checkoutSummaryFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
