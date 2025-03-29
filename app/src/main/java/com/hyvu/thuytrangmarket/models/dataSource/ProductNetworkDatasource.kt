package com.hyvu.thuytrangmarket.models.dataSource

import com.hyvu.thuytrangmarket.models.network.product.NetworkCreateProduct
import com.hyvu.thuytrangmarket.models.network.product.NetworkProduct
import com.hyvu.thuytrangmarket.models.network.product.ProductApiService

class ProductNetworkDatasource(private val productApiService: ProductApiService) {

    companion object {
        const val TAG = "ProductNetworkDatasource"
    }

    suspend fun fetchProductsByCategory(categoryId: String): List<NetworkProduct> {
        return productApiService.getProductByCategoryId(categoryId)
    }

    suspend fun createProduct(product: NetworkCreateProduct): NetworkProduct {
        return productApiService.createProduct(product)
    }

}