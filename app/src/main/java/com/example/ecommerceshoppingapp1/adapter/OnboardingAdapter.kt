package com.example.ecommerceshoppingapp1.adapter

import com.example.ecommerceshoppingapp1.model.OnboardingItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceshoppingapp1.R

class OnboardingAdapter(private val onboardingItems: List<OnboardingItem>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_onboarding_page,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(onboardingItems[position])
    }

    override fun getItemCount(): Int = onboardingItems.size

    fun getOnboardingItem(position: Int): OnboardingItem = onboardingItems[position]

    class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val layoutOnboarding: View = view.findViewById(R.id.layoutOnboarding)
        private val ivIllustration: ImageView = view.findViewById(R.id.ivIllustration)
        private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = view.findViewById(R.id.tvDescription)

        fun bind(item: OnboardingItem) {
            layoutOnboarding.setBackgroundColor(item.backgroundColor)
            ivIllustration.setImageResource(item.illustration)
            tvTitle.text = item.title
            tvDescription.text = item.description
        }
    }
}



