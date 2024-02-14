package fastcampus.aop.pjt24_online_store.data.network

import fastcampus.aop.pjt24_online_store.data.response.ProductResponse
import fastcampus.aop.pjt24_online_store.data.response.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(): Response<ProductsResponse>

    @GET("products/{productId}")
    suspend fun getProduct(
        @Path("productId") productId: Long
    ): Response<ProductResponse>
}