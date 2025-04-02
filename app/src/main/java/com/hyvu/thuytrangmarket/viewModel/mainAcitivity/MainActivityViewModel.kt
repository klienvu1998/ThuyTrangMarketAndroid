package com.hyvu.thuytrangmarket.viewModel.mainAcitivity

import androidx.lifecycle.viewModelScope
import com.hyvu.thuytrangmarket.base.BaseApiResponse
import com.hyvu.thuytrangmarket.base.BaseViewModel
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.models.database.product.toProduct
import com.hyvu.thuytrangmarket.models.repository.CategoryRepository
import com.hyvu.thuytrangmarket.models.repository.ProductRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
): BaseViewModel() {

    fun syncLocalDataToServer() {
        viewModelScope.launch {
            val localCategories = categoryRepository.getCategoriesBySyncStatus(false)
            localCategories.forEach {
//                categoryRepository.insertCategory()
            }
            val localProducts = productRepository.getProductsBySyncStatus(false)
            localProducts.forEach {
                if (it.isDeleted) {
                    productRepository.deleteProductNetwork(it.id)
                } else {
                    val result = productRepository.createProduct(it.toProduct())
                    if (result is BaseApiResponse.Success) {
                        productRepository.insertProduct(result.data, DataSource.NETWORK)
                    }
                }
            }
        }
    }

}