package fastcampus.aop.pjt14_c2c_marketplace.chatdetail

data class ChatItem(
    val senderId: String,
    val message: String
) {
    constructor(): this("", "")
}
