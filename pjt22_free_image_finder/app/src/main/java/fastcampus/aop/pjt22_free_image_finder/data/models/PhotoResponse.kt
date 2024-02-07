package fastcampus.aop.pjt22_free_image_finder.data.models


import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("alt_description")
    val altDescription: String?,
    @SerializedName("blur_hash")
    val blurHash: String?,
    @SerializedName("breadcrumbs")
    val breadcrumbs: List<Any>?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("current_user_collections")
    val currentUserCollections: List<Any>?,
    @SerializedName("description")
    val description: Any?,
    @SerializedName("downloads")
    val downloads: Int?,
    @SerializedName("exif")
    val exif: Exif?,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String?,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("links")
    val links: Links?,
    @SerializedName("location")
    val location: Location?,
    @SerializedName("promoted_at")
    val promotedAt: String?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("sponsorship")
    val sponsorship: Any?,
    @SerializedName("topic_submissions")
    val topicSubmissions: TopicSubmissions?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("urls")
    val urls: Urls?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("views")
    val views: Int?,
    @SerializedName("width")
    val width: Int
)