package com.hyvu.thuytrangmarket.models.network.category

import com.hyvu.thuytrangmarket.base.BaseRetrofit
import com.hyvu.thuytrangmarket.models.network.product.NetworkProduct
import retrofit2.http.GET

interface CategoryApiService {

    @GET("/v1/categories") // Moved into the interface itself
    suspend fun getCategories(): List<NetworkCategory>

    companion object {
        fun getService(): CategoryApiService {
            return BaseRetrofit.create<CategoryApiService>()
        }
    }


}