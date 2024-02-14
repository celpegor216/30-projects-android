package fastcampus.aop.pjt24_online_store.data.repository

import fastcampus.aop.pjt24_online_store.data.entity.product.ProductEntity

interface ProductRepository {

    suspend fun getProductItem(itemId: Long): ProductEntity?

    suspend fun getProductList(): List<ProductEntity>

    suspend fun getLocalProductList(): List<ProductEntity>

    suspend fun insertProductItem(productItem: ProductEntity): Long

    suspend fun insertProductList(productList: List<ProductEntity>)

    suspend fun updateProductItem(itemId: Long)

    suspend fun deleteProductItem(itemId: Long)

    suspend fun deleteAll()
}