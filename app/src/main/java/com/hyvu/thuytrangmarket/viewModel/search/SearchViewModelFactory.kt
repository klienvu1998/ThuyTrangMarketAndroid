package com.hyvu.thuytrangmarket.viewModel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyvu.thuytrangmarket.models.repository.ProductRepository

class SearchViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(ProductRepository.getInstance()) as T
    }

}