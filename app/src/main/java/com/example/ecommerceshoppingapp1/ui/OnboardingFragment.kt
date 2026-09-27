package com.example.ecommerceshoppingapp1.ui

import com.example.ecommerceshoppingapp1.model.OnboardingItem
import com.example.ecommerceshoppingapp1.adapter.OnboardingAdapter



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerceshoppingapp1.R

class OnboardingFragment : Fragment() {

    private lateinit var onboardingAdapter: OnboardingAdapter
    private lateinit var indicatorContainer: LinearLayout
    private lateinit var btnAction: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        indicatorContainer = view.findViewById(R.id.indicatorContainer)
        btnAction = view.findViewById(R.id.btnAction)

        setupOnboardingItems()
        setupIndicators()
        setCurrentIndicator(0)

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = onboardingAdapter
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                if (position == onboardingAdapter.itemCount - 1) {
                    btnAction.text = getString(R.string.btn_get_started)
                } else {
                    btnAction.text = getString(R.string.btn_next)
                }
            }
        })

        btnAction.setOnClickListener {
            if (viewPager.currentItem + 1 < onboardingAdapter.itemCount) {
                viewPager.currentItem += 1
            } else {
                findNavController().navigate(R.id.action_OnboardingFragment_to_LoginFragment)
            }
        }
    }

    private fun setupOnboardingItems() {
        val bgColor = ContextCompat.getColor(requireContext(), R.color.bg_onboarding)
        val items = listOf(
            OnboardingItem(
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_desc_1),
                R.drawable.screen1,
                bgColor
            ),
            OnboardingItem(
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_desc_2),
                R.drawable.screen2,
                bgColor
            ),
            OnboardingItem(
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_desc_3),
                R.drawable.screen3,
                bgColor
            )
        )
        onboardingAdapter = OnboardingAdapter(items)
    }

    private fun setupIndicators() {
        indicatorContainer.removeAllViews()
        val indicators = arrayOfNulls<ImageView>(onboardingAdapter.itemCount)
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

    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        private val MIN_SCALE = 0.85f
        private val MIN_ALPHA = 0.5f

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { alpha = 0f }
                    position <= 1 -> {
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            -horzMargin + vertMargin / 2
                        }
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                        alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { alpha = 0f }
                }
            }
        }
    }
}




