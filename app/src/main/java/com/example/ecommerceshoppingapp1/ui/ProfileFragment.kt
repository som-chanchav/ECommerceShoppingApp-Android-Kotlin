package com.example.ecommerceshoppingapp1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerceshoppingapp1.databinding.FragmentSecondBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.ecommerceshoppingapp1.databinding.DialogOrdersBinding
import com.example.ecommerceshoppingapp1.adapter.OrderAdapter
import androidx.recyclerview.widget.LinearLayoutManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: com.example.ecommerceshoppingapp1.database.DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        dbHelper = com.example.ecommerceshoppingapp1.database.DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load session
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)
        val sessionEmail = sharedPref.getString("session_email", "") ?: ""

        if (sessionEmail.isNotEmpty()) {
            val user = dbHelper.getUserByEmail(sessionEmail)
            if (user != null) {
                binding.tvProfileName.text = user.name
                binding.tvProfileEmail.text = user.email
            }
        }

        binding.btnLogout.setOnClickListener {
            // Clear session
            sharedPref.edit().remove("session_email").apply()
            
            // Navigate back to Login (MainActivity)
            val intent = android.content.Intent(requireContext(), com.example.ecommerceshoppingapp1.ui.MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnMyOrders.setOnClickListener {
            showOrderHistoryDialog(if (sessionEmail.isEmpty()) "guest@gmail.com" else sessionEmail)
        }
    }

    private fun showOrderHistoryDialog(email: String) {
        val dialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogOrdersBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val orders = dbHelper.getUserOrders(email)
        if (orders.isEmpty()) {
            dialogBinding.layoutEmptyState.visibility = View.VISIBLE
            dialogBinding.rvOrders.visibility = View.GONE
        } else {
            dialogBinding.layoutEmptyState.visibility = View.GONE
            dialogBinding.rvOrders.visibility = View.VISIBLE
            
            val adapter = OrderAdapter(orders)
            dialogBinding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
            dialogBinding.rvOrders.adapter = adapter
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
