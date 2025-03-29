package com.hyvu.thuytrangmarket.viewModel.mainAcitivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyvu.thuytrangmarket.models.repository.CategoryRepository
import com.hyvu.thuytrangmarket.models.repository.ProductRepository

class MainActivityViewModelFactory (): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(CategoryRepository.getInstance(), ProductRepository.getInstance()) as T
    }

}