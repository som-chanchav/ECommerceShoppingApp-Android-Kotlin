package com.example.ecommerceshoppingapp1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceshoppingapp1.R
import com.example.ecommerceshoppingapp1.adapter.ProductAdapter
import com.example.ecommerceshoppingapp1.database.DatabaseHelper
import com.example.ecommerceshoppingapp1.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFavorites()
    }
    
    override fun onResume() {
        super.onResume()
        setupFavorites()
    }

    private fun setupFavorites() {
        val favorites = dbHelper.getFavoriteProducts()
        
        if (favorites.isEmpty()) {
            binding.rvFavorites.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvFavorites.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
            
            val adapter = ProductAdapter(favorites, isGrid = true) { product ->
                val bundle = Bundle().apply {
                    putInt("product_id", product.id)
                }
                findNavController().navigate(R.id.action_favoriteFragment_to_detailFragment, bundle)
            }
            binding.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvFavorites.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
