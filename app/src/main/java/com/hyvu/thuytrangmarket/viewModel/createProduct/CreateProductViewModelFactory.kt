package com.hyvu.thuytrangmarket.viewModel.createProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyvu.thuytrangmarket.models.repository.CategoryRepository
import com.hyvu.thuytrangmarket.models.repository.ProductRepository

class CreateProductViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateProductViewModel(CategoryRepository.getInstance(), ProductRepository.getInstance()) as T
    }
}