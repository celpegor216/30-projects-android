package fastcampus.aop.pjt24_online_store.data.response

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("items")
    val items: List<ProductResponse>,
    @SerializedName("count")
    val count: Int
)
