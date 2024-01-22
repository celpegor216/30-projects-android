package fastcampus.aop.pjt14_c2c_marketplace.home

data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val imageUrl: String
) {
    // Firebase DB에서 ArticleModel 인스턴스 자체를 받아오기 위해
    constructor(): this("", "", 0, "", "")
}
