package fastcampus.aop.pjt22_free_image_finder.data.models


import com.google.gson.annotations.SerializedName

data class Social(
    @SerializedName("instagram_username")
    val instagramUsername: Any?,
    @SerializedName("paypal_email")
    val paypalEmail: Any?,
    @SerializedName("portfolio_url")
    val portfolioUrl: Any?,
    @SerializedName("twitter_username")
    val twitterUsername: Any?
)