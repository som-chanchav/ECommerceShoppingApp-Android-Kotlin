package com.example.ecommerceshoppingapp1.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView

/**
 * ផ្ទុក និងបង្ហាញរូបភាពចេញពី assets folder ទៅកាន់ ImageView
 * ឧទាហរណ៍នៃការប្រើប្រាស់៖ imageView.loadFromAsset("products/shoes.jpg")
 */
fun ImageView.loadFromAsset(fileName: String) {
    try {
        val stream = context.assets.open(fileName)
        val drawable = Drawable.createFromStream(stream, null)
        this.setImageDrawable(drawable)
        stream.close()
    } catch (e: Exception) {
        e.printStackTrace()
        // អាចដាក់រូបភាពដើម (Placeholder) នៅទីនេះក្នុងករណីរកមិនឃើញរូប
    }
}
