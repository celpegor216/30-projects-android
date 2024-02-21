package fastcampus.aop.pjt28_delivery_info.extension

import android.view.View
import androidx.annotation.ColorRes

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}

fun View.color(@ColorRes colorResId: Int) = context.color(colorResId)