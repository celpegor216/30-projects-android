package fastcampus.aop.pjt26_sns.home

data class ArticleModel(
    val userId: String,
    val title: String,
    val createdAt: Long,
    val content: String,
    val imageUrlList: List<String>
) {
    // Firebase DB에서 ArticleModel 인스턴스 자체를 받아오기 위해
    constructor(): this("", "", 0, "", listOf())
}
