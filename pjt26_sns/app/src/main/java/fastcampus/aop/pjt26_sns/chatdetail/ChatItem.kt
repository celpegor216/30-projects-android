package fastcampus.aop.pjt26_sns.chatdetail

data class ChatItem(
    val senderId: String,
    val message: String
) {
    constructor(): this("", "")
}
