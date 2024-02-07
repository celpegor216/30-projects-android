package fastcampus.aop.pjt22_free_image_finder.data.models


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("download")
    val download: String?,
    @SerializedName("download_location")
    val downloadLocation: String?,
    @SerializedName("html")
    val html: String?,
    @SerializedName("self")
    val self: String?
)