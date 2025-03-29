package com.hyvu.thuytrangmarket.viewModel.listProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyvu.thuytrangmarket.models.repository.ProductRepository

class ListProductViewModelFactory(): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListProductViewModel(ProductRepository.getInstance()) as T
    }


}