package com.example.ecommerceshoppingapp1.ui

import com.example.ecommerceshoppingapp1.database.DatabaseHelper
import com.example.ecommerceshoppingapp1.ui.HomepageActivity



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.ecommerceshoppingapp1.R
import com.example.ecommerceshoppingapp1.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSavedCredentials()

        binding.tvRegisterLink.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.tilEmail.editText?.text.toString()
            val password = binding.tilPassword.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                val isValid = dbHelper.checkUser(email, password)
                if (isValid) {
                    Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                    
                    // Save session
                    val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("session_email", email).apply()

                    val intent = Intent(requireContext(), HomepageActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadSavedCredentials() {
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("saved_email", "")
        val password = sharedPref.getString("saved_password", "")
        val rememberMe = sharedPref.getBoolean("remember_me", false)

        if (rememberMe) {
            binding.tilEmail.editText?.setText(email)
            binding.tilPassword.editText?.setText(password)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



