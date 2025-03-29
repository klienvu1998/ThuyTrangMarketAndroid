package com.hyvu.thuytrangmarket.models.dataSource

import com.hyvu.thuytrangmarket.models.network.category.CategoryApiService

class CategoryNetworkDataSource(private val categoryApiService: CategoryApiService) {

    suspend fun getCategories() = categoryApiService.getCategories()

}