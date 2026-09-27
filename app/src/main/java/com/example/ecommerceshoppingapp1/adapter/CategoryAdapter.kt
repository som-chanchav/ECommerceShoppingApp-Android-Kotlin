package com.example.ecommerceshoppingapp1.adapter

import com.example.ecommerceshoppingapp1.model.Category


import com.example.ecommerceshoppingapp1.R

import android.view.LayoutInflater
import android.view.ViewGroup
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceshoppingapp1.databinding.ItemCategoryBinding
import com.example.ecommerceshoppingapp1.utils.loadFromAsset

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import android.graphics.Typeface

class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var selectedCategoryName: String? = null

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategoryName.text = category.name
            if (category.image.startsWith("res:")) {
                val resId = binding.root.context.resources.getIdentifier(
                    category.image.substringAfter("res:"),
                    "drawable",
                    binding.root.context.packageName
                )
                if (resId != 0) {
                    binding.imgCategory.setImageResource(resId)
                } else {
                    binding.imgCategory.setImageResource(R.drawable.ic_launcher_foreground)
                }
            } else {
                binding.imgCategory.loadFromAsset(category.image)
            }
            binding.imgCategory.setBackgroundColor(Color.parseColor(category.bgColor))

            val isSelected = category.name.equals(selectedCategoryName, ignoreCase = true)
            if (isSelected) {
                binding.imgCategory.strokeWidth = 6f
                binding.imgCategory.strokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.primary_green)
                )
                binding.tvCategoryName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.primary_green)
                )
                binding.tvCategoryName.setTypeface(null, Typeface.BOLD)
            } else {
                binding.imgCategory.strokeWidth = 0f
                binding.tvCategoryName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.black)
                )
                binding.tvCategoryName.setTypeface(null, Typeface.NORMAL)
            }

            binding.root.setOnClickListener {
                onItemClick(category)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {

        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int
    ) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}




