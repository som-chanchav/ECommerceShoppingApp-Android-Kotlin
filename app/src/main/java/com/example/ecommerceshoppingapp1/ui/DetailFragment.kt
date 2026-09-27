package com.example.ecommerceshoppingapp1.ui

import com.example.ecommerceshoppingapp1.database.DatabaseHelper
import com.example.ecommerceshoppingapp1.model.Product
import com.example.ecommerceshoppingapp1.model.Category



import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceshoppingapp1.R
import com.example.ecommerceshoppingapp1.databinding.FragmentDetailBinding
import com.example.ecommerceshoppingapp1.utils.loadFromAsset
import java.util.Locale

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private var productId: Int = -1
    private var quantity: Int = 1
    private var currentPrice: Double = 0.0
    private var isFavorite: Boolean = false

    // Color & Size state
    private val colorNames = mapOf(
        R.id.colorWhite to "White / Black",
        R.id.colorBlack to "Black",
        R.id.colorOlive to "Olive Green",
        R.id.colorBlue to "Blue"
    )
    private var selectedColorId: Int = R.id.colorWhite
    private var selectedSizeView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        productId = arguments?.getInt("product_id") ?: -1
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadProductData()
        setupClickListeners()
        setupColorSelection()
        setupSizeSelection()
    }

    private fun loadProductData() {
        val product = dbHelper.getProductById(productId)
        product?.let {
            binding.ivProductLarge.loadFromAsset(it.imagePath)
            binding.tvDetailTitle.text = it.title
            binding.tvDetailCategory.text = it.category
            currentPrice = it.price
            binding.tvDetailPrice.text = String.format(Locale.US, "$%.2f", it.price)
            binding.tvDetailRating.text = String.format(Locale.US, "%.1f", it.rating)
            binding.tvReviewCount.text = "2,356 Reviews"
            binding.tvSoldCount.text = "5.2k Sold"
            binding.tvDetailDesc.text = it.description

            if (it.discount > 0) {
                binding.tvDiscount.visibility = View.VISIBLE
                binding.tvOriginalPrice.visibility = View.VISIBLE
                
                binding.tvDiscount.text = "-${it.discount}%"
                
                val originalPrice = it.price / (1.0 - (it.discount / 100.0))
                binding.tvOriginalPrice.text = String.format(Locale.US, "$%.2f", originalPrice)
                binding.tvOriginalPrice.paintFlags = binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.tvDiscount.visibility = View.GONE
                binding.tvOriginalPrice.visibility = View.GONE
            }
            
            isFavorite = it.isFavorite
            updateFavoriteIcon()
            
            updateTotalPrice()
        }
    }

    private fun updateFavoriteIcon() {
        if (isFavorite) {
            binding.btnDetailFavorite.setImageResource(R.drawable.favorite_24)
            binding.btnDetailFavorite.setColorFilter(android.graphics.Color.parseColor("#E53935"))
        } else {
            binding.btnDetailFavorite.setImageResource(R.drawable.favorite_border_24)
            binding.btnDetailFavorite.setColorFilter(android.graphics.Color.parseColor("#757575"))
        }
    }

    private fun updateTotalPrice() {
        val total = currentPrice * quantity
        binding.tvDetailTotal.text = String.format(Locale.US, "Total: $%.2f", total)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnDetailFavorite.setOnClickListener {
            if (productId != -1) {
                isFavorite = !isFavorite
                dbHelper.toggleFavorite(productId, isFavorite)
                updateFavoriteIcon()
            }
        }

        binding.btnAddToCart.setOnClickListener {
            if (productId != -1) {
                val size = selectedSizeView?.text?.toString() ?: ""
                val color = colorNames[selectedColorId] ?: ""
                val success = dbHelper.addToCart(productId, quantity, size, color)
                if (success) {
                    (activity as? HomepageActivity)?.updateCartBadge()
                    Toast.makeText(requireContext(), "Added $quantity item(s) to Cart!", Toast.LENGTH_SHORT).show()
                    val bottomNav = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
                    if (bottomNav != null) {
                        bottomNav.selectedItemId = R.id.cartFragment
                    } else {
                        findNavController().navigate(R.id.cartFragment)
                    }
                }
            }
        }

        // Quantity controls
        binding.btnPlus.setOnClickListener {
            quantity++
            binding.tvQuantity.text = quantity.toString()
            updateTotalPrice()
        }

        binding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
                updateTotalPrice()
            }
        }

        // Telegram button
        binding.btnChat.setOnClickListener {
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse("https://t.me/som_chanchav")
            startActivity(intent)
        }

        // Read More toggle
        binding.tvReadMore.setOnClickListener {
            if (binding.tvDetailDesc.maxLines == 3) {
                binding.tvDetailDesc.maxLines = Integer.MAX_VALUE
                binding.tvReadMore.text = "Read Less ▴"
            } else {
                binding.tvDetailDesc.maxLines = 3
                binding.tvReadMore.text = "Read More ▾"
            }
        }
    }

    // ===== COLOR SELECTION =====
    private fun setupColorSelection() {
        val colorViews = listOf(
            binding.colorWhite,
            binding.colorBlack,
            binding.colorOlive,
            binding.colorBlue
        )

        colorViews.forEach { colorView ->
            colorView.setOnClickListener {
                selectColor(colorView, colorViews)
            }
        }

        // Set default selection
        selectColor(binding.colorWhite, colorViews)
    }

    private fun selectColor(selected: FrameLayout, allColors: List<FrameLayout>) {
        selectedColorId = selected.id

        // Reset all to no border
        allColors.forEach { colorView ->
            colorView.setBackgroundResource(0)
        }

        // Set selected border
        selected.setBackgroundResource(R.drawable.bg_color_circle)

        // Update color name text
        binding.tvColorName.text = colorNames[selected.id] ?: ""
    }

    // ===== SIZE SELECTION =====
    private fun setupSizeSelection() {
        val sizeViews = listOf(
            binding.size38,
            binding.size39,
            binding.size40,
            binding.size41,
            binding.size42,
            binding.size43
        )

        sizeViews.forEach { sizeView ->
            sizeView.setOnClickListener {
                selectSize(sizeView, sizeViews)
            }
        }

        // Set default selection (size 40)
        selectSize(binding.size40, sizeViews)
    }

    private fun selectSize(selected: TextView, allSizes: List<TextView>) {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.primary_green)
        val greyColor = ContextCompat.getColor(requireContext(), R.color.text_grey)

        // Reset all sizes to normal
        allSizes.forEach { sizeView ->
            sizeView.setBackgroundResource(R.drawable.bg_size_normal)
            sizeView.setTextColor(greyColor)
            sizeView.setTypeface(null, Typeface.NORMAL)
        }

        // Highlight selected
        selected.setBackgroundResource(R.drawable.bg_size_selected)
        selected.setTextColor(greenColor)
        selected.setTypeface(null, Typeface.BOLD)

        selectedSizeView = selected
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


