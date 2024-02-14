package fastcampus.aop.pjt24_online_store.data.response


import com.google.gson.annotations.SerializedName
import fastcampus.aop.pjt24_online_store.data.entity.product.ProductEntity
import java.util.Date

data class ProductResponse(
    @SerializedName("createdAt")
    val createdAt: Long,
    @SerializedName("id")
    val id: String,
    @SerializedName("product_image")
    val productImage: String,
    @SerializedName("product_introduction_image")
    val productIntroductionImage: String,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_price")
    val productPrice: String,
    @SerializedName("product_type")
    val productType: String
) {
    fun toEntity(): ProductEntity = ProductEntity(
        id = id.toLong(),
        createdAt = Date(createdAt),
        productName = productName,
        productPrice = productPrice.toDouble().toInt(),
        productImage = productImage,
        productType = productType,
        productIntroductionImage = productIntroductionImage
    )
}