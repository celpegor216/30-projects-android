package fastcampus.aop.pjt29_movie_review.extension

import java.text.DecimalFormat

fun Float.toDecimalFormatString(format: String): String = DecimalFormat(format).format(this)