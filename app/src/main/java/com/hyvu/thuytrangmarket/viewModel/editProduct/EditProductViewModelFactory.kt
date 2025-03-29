package com.hyvu.thuytrangmarket.viewModel.editProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyvu.thuytrangmarket.models.repository.CategoryRepository
import com.hyvu.thuytrangmarket.models.repository.ProductRepository

class EditProductViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditProductViewModel(CategoryRepository.getInstance(), ProductRepository.getInstance()) as T
    }
}