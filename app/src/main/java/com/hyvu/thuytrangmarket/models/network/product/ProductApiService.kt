package com.hyvu.thuytrangmarket.models.network.product

import com.hyvu.thuytrangmarket.base.BaseRetrofit
import com.hyvu.thuytrangmarket.models.network.category.NetworkCategory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @GET("/v1/products")
    suspend fun getProductByCategoryId(@Query("categoryId") categoryId: String): List<NetworkProduct>

    @POST("/v1/products")
    suspend fun createProduct(@Body product: NetworkCreateProduct): NetworkProduct

    @PUT("/v1/products/{id}")
    suspend fun updateProduct(@Path("id") productId: String, @Body product: NetworkCreateProduct): NetworkProduct

    @DELETE("/v1/products/{id}")
    suspend fun deleteProduct(@Path("id") productId: String): DeleteResponse

    companion object {
        fun getService(): ProductApiService {
            return BaseRetrofit.create<ProductApiService>()
        }
    }


}