package fastcampus.aop.pjt24_online_store.presentation.list

import fastcampus.aop.pjt24_online_store.data.entity.product.ProductEntity

sealed class ProductListState {

    object Uninitialized: ProductListState()

    object Loading: ProductListState()

    data class Success(
        val productList: List<ProductEntity>
    ): ProductListState()

    object Error: ProductListState()
}