package com.hyvu.thuytrangmarket.viewModel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyvu.thuytrangmarket.models.repository.CategoryRepository
import com.hyvu.thuytrangmarket.models.repository.ProductRepository

class HomeViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(CategoryRepository.getInstance(), ProductRepository.getInstance()) as T
    }

}