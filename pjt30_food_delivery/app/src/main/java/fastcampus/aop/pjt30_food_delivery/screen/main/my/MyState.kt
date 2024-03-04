package fastcampus.aop.pjt30_food_delivery.screen.main.my

import android.net.Uri
import androidx.annotation.StringRes
import fastcampus.aop.pjt30_food_delivery.data.entity.OrderEntity

sealed class MyState {

    object Uninitialized: MyState()

    object Loading: MyState()

    data class Login(
        val idToken: String
    ): MyState()

    sealed class Success: MyState() {
        data class Registered(
            val username: String,
            val profileImageUri: Uri?,
            val orderList: List<OrderEntity>
        ): Success()

        object NotRegistered: Success()
    }

    data class Error(
        @StringRes val messageId: Int
    ): MyState()

}