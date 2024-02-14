package fastcampus.aop.pjt24_online_store.domain

import fastcampus.aop.pjt24_online_store.data.entity.product.ProductEntity
import fastcampus.aop.pjt24_online_store.data.repository.ProductRepository

internal class OrderProductItemUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke(productEntity: ProductEntity): Long {
        return productRepository.insertProductItem(productEntity)
    }
}