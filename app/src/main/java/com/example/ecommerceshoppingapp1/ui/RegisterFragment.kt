package com.example.ecommerceshoppingapp1.ui

import com.example.ecommerceshoppingapp1.database.DatabaseHelper


import com.example.ecommerceshoppingapp1.R

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceshoppingapp1.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLoginLink.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnRegister.setOnClickListener {
            val fullName = binding.tilFullName.editText?.text.toString()
            val email = binding.tilRegisterEmail.editText?.text.toString()
            val password = binding.tilRegisterPassword.editText?.text.toString()
            val rememberMe = binding.cbRememberMe.isChecked

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.registerUser(fullName, email, password)
                if (success) {
                    if (rememberMe) {
                        saveCredentials(email, password)
                    }
                    Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Email already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveCredentials(email: String, pass: String) {
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("saved_email", email)
            putString("saved_password", pass)
            putBoolean("remember_me", true)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



