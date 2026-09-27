import os

path = r"E:\learn android\ECommerceShoppingApp1\app\src\main\java\com\example\ecommerceshoppingapp1\ui\HomeFragment.kt"
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

imports = """
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
"""
if "ViewPager2" not in content:
    content = content.replace("class HomeFragment", imports + "\nclass HomeFragment")

adapter_code = """
    inner class BannerAdapter(private val images: List<Int>) : androidx.recyclerview.widget.RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
        inner class BannerViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
            val imgBanner = view.findViewById<ImageView>(R.id.imgBannerItem)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
            return BannerViewHolder(view)
        }
        override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
            holder.imgBanner.setImageResource(images[position])
        }
        override fun getItemCount() = images.size
    }
"""

banner_logic = """
    private lateinit var indicatorContainer: LinearLayout
    private val bannerImages = listOf(R.drawable.screen1, R.drawable.screen2, R.drawable.screen3)
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
"""

content = content.replace("    override fun onDestroyView() {", banner_logic + "\n    override fun onDestroyView() {")
content = content.replace("setupCategories()", "setupBanner()\n        setupCategories()")
content = content.replace("_binding = null", "_binding = null\n        autoSlideHandler.removeCallbacks(autoSlideRunnable)")

content = content.rstrip()
if content.endswith("}"):
    content = content[:-1] + adapter_code + "\n}"

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

print("Done")
