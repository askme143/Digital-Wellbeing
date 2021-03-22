package com.yeongil.focusaid.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter

object ImageViewBindingAdapter {
    @BindingAdapter("image_drawable")
    @JvmStatic
    fun imageViewItemBind(imageView: ImageView, data: Drawable?) {
        imageView.setImageDrawable(data)
    }

    @BindingAdapter("image_drawable")
    @JvmStatic
    fun imageViewItemBind(imageView: ImageView, data: Int) {
        imageView.setImageResource(data)
    }
}