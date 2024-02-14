package fastcampus.aop.pjt24_online_store.presentation.detail

import fastcampus.aop.pjt24_online_store.data.entity.product.ProductEntity

sealed class ProductDetailState {

    object Uninitialized: ProductDetailState()

    object Loading: ProductDetailState()

    data class Success(
        val productItem: ProductEntity
    ): ProductDetailState()

    object Order: ProductDetailState()

    object Error: ProductDetailState()
}