package com.example.ecommerceshoppingapp1.ui

import com.example.ecommerceshoppingapp1.R

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerceshoppingapp1.database.DatabaseHelper
import com.example.ecommerceshoppingapp1.databinding.ActivityHomepageBinding

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        dbHelper = DatabaseHelper(this)
        
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg_onboarding)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_homepage) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        
        updateCartBadge()
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
    }

    fun updateCartBadge() {
        val count = dbHelper.getCartCount()
        val badge = binding.bottomNav.getOrCreateBadge(R.id.cartFragment)
        if (count > 0) {
            badge.isVisible = true
            badge.number = count
        } else {
            badge.isVisible = false
        }
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(androidx.core.view.GravityCompat.START)
    }
}
