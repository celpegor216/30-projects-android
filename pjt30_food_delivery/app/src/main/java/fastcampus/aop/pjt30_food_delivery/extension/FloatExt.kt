package fastcampus.aop.pjt30_food_delivery.extension

import android.content.res.Resources

fun Float.fromDpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()