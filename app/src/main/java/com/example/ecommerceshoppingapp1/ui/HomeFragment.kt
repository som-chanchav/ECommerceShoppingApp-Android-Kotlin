package com.example.ecommerceshoppingapp1.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerceshoppingapp1.R
import com.example.ecommerceshoppingapp1.adapter.CategoryAdapter
import com.example.ecommerceshoppingapp1.adapter.ProductAdapter
import com.example.ecommerceshoppingapp1.database.DatabaseHelper
import com.example.ecommerceshoppingapp1.databinding.FragmentHomeBinding
import com.example.ecommerceshoppingapp1.model.Category
import com.example.ecommerceshoppingapp1.model.Product

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var categoryAdapter: CategoryAdapter
    private var selectedCategory: String? = "All"
    private var searchQuery: String = ""

    private lateinit var indicatorContainer: LinearLayout
    private val bannerImages = listOf(
        R.drawable.banner_1,
        R.drawable.banner_2,
        R.drawable.banner_3,
        R.drawable.banner_4
    )
    private val autoSlideHandler = android.os.Handler(android.os.Looper.getMainLooper())
    private val autoSlideRunnable = object : Runnable {
        override fun run() {
            val viewPager = view?.findViewById<ViewPager2>(R.id.viewPagerBanner) ?: return
            if (viewPager.currentItem + 1 < bannerImages.size) {
                viewPager.currentItem += 1
            } else {
                viewPager.currentItem = 0
            }
            autoSlideHandler.postDelayed(this, 3000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupGreetingText()
        setupBanner()
        setupCategories()
        setupProducts()
        setupHeaderCart()
        updateCartBadge()
        setupSeeAllButtons()
        setupSearch()
        setupMenuButton()
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
    }

    private fun setupSeeAllButtons() {
        binding.tvSeeAllCats.setOnClickListener {
            resetCategoryFilter()
        }
        binding.tvSeeAllProducts.setOnClickListener {
            resetCategoryFilter()
        }
        binding.tvSeeAllPopular.setOnClickListener {
            resetCategoryFilter()
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s?.toString() ?: ""
                setupProducts()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun resetCategoryFilter() {
        selectedCategory = "All"
        binding.etSearch.setText("")
        categoryAdapter.selectedCategoryName = selectedCategory
        categoryAdapter.notifyDataSetChanged()
        setupProducts()
    }

    private fun setupHeaderCart() {
        binding.btnNotification.setOnClickListener {
            val bottomNav = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
            if (bottomNav != null) {
                bottomNav.selectedItemId = R.id.cartFragment
            } else {
                findNavController().navigate(R.id.cartFragment)
            }
        }
    }

    private fun updateCartBadge() {
        val count = dbHelper.getCartCount()
        binding.tvCartBadge.text = count.toString()
        if (count == 0) {
            binding.tvCartBadge.visibility = View.GONE
        } else {
            binding.tvCartBadge.visibility = View.VISIBLE
        }
        (activity as? HomepageActivity)?.updateCartBadge()
    }

    private fun setupGreetingText() {
        val fullText = "Find your\nbest products!"
        val spannable = SpannableString(fullText)
        val startIndex = fullText.indexOf("best products!")
        if (startIndex != -1) {
            val greenColor = ContextCompat.getColor(requireContext(), R.color.primary_green)
            spannable.setSpan(
                ForegroundColorSpan(greenColor),
                startIndex,
                startIndex + "best products!".length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvGreeting.text = spannable
    }

    private fun setupBanner() {
        val viewPager = binding.viewPagerBanner
        indicatorContainer = binding.layoutDots
        
        viewPager.adapter = BannerAdapter(bannerImages)
        setupIndicators()
        setCurrentIndicator(0)
        
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        
        autoSlideHandler.postDelayed(autoSlideRunnable, 3000)
    }

    private fun setupIndicators() {
        indicatorContainer.removeAllViews()
        val indicators = arrayOfNulls<ImageView>(bannerImages.size)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_indicator_inactive
                    )
                )
                this.layoutParams = layoutParams
            }
            indicatorContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_indicator_inactive
                    )
                )
            }
        }
    }

    private fun setupCategories() {
        val categories = listOf(
            Category(id = 0, name = "All", image = "res:ic_clickshop_logo", bgColor = "#F5F5F5"),
            Category(id = 1, name = "Shoes", image = "products/shoes.jpg", bgColor = "#EAF6E3"),
            Category(id = 2, name = "Shoulder bag", image = "products/Shoulder bag.jpg", bgColor = "#FFF0F0"),
            Category(id = 3, name = "Women's Fashion", image = "products/Women's Fashion.jpg", bgColor = "#F3EFFF"),
            Category(id = 4, name = "Jewelry", image = "products/1.jpg", bgColor = "#FFF7E3"),
            Category(id = 5, name = "Men's Fashion", image = "products/Men's Fashion.jpg", bgColor = "#EBF4FF")
        )

        categoryAdapter = CategoryAdapter(categories) { category ->
            if (selectedCategory == category.name) {
                selectedCategory = "All"
            } else {
                selectedCategory = category.name
            }
            categoryAdapter.selectedCategoryName = selectedCategory
            categoryAdapter.notifyDataSetChanged()
            setupProducts()
        }
        categoryAdapter.selectedCategoryName = selectedCategory

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategories.adapter = categoryAdapter
        binding.rvCategories.setHasFixedSize(true)
    }

    private fun setupProducts() {
        val allProducts = dbHelper.getAllProducts()
        
        var filteredProducts = if (selectedCategory == null || selectedCategory == "All") {
            allProducts
        } else {
            allProducts.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
        
        if (searchQuery.isNotEmpty()) {
            filteredProducts = filteredProducts.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
            }
        }
        
        val specialProducts = filteredProducts.filter { !it.isPopular }
        val popularProducts = filteredProducts.filter { it.isPopular }
        
        // Setup Special Products
        val adapter = ProductAdapter(specialProducts) { product ->
            val bundle = Bundle().apply {
                putInt("product_id", product.id)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvProducts.adapter = adapter
        
        // Setup Popular Products
        val popularAdapter = ProductAdapter(popularProducts, isGrid = true) { product ->
            val bundle = Bundle().apply {
                putInt("product_id", product.id)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
        binding.rvPopularProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPopularProducts.adapter = popularAdapter
    }

    private fun setupMenuButton() {
        binding.btnMenu.setOnClickListener {
            (activity as? HomepageActivity)?.openDrawer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        autoSlideHandler.removeCallbacks(autoSlideRunnable)
    }

    inner class BannerAdapter(private val images: List<Int>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
        inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imgBanner = view.findViewById<ImageView>(R.id.imgBannerItem)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
            return BannerViewHolder(view)
        }
        override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
            holder.imgBanner?.setImageResource(images[position])
        }
        override fun getItemCount() = images.size
    }
}
