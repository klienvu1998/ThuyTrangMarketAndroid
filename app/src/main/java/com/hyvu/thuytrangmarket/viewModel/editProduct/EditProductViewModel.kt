package com.hyvu.thuytrangmarket.viewModel.editProduct

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.base.BaseApiResponse
import com.hyvu.thuytrangmarket.base.BaseViewModel
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.base.NetworkCoroutineScope
import com.hyvu.thuytrangmarket.base.UiEvent
import com.hyvu.thuytrangmarket.base.UiState
import com.hyvu.thuytrangmarket.models.data.Category
import com.hyvu.thuytrangmarket.models.data.Product
import com.hyvu.thuytrangmarket.models.repository.CategoryRepository
import com.hyvu.thuytrangmarket.models.repository.ProductRepository
import com.hyvu.thuytrangmarket.utils.getString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class EditProductViewModel(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
): BaseViewModel() {

    companion object {
        const val TAG = "CreateProductViewModel"
    }

    protected val _uiState: MutableStateFlow<EditProductState> = MutableStateFlow(EditProductState.Loading(DataSource.LOCAL))
    val uiState: StateFlow<EditProductState>
        get() = _uiState

    protected val _uiEvent: MutableSharedFlow<EditProductEvent> = MutableSharedFlow()
    val uiEvent: SharedFlow<EditProductEvent>
        get() = _uiEvent


    fun loadData(productId: String) {
        viewModelScope.launch {
            _uiState.emit(EditProductState.Loading(DataSource.LOCAL))
            try {
                categoryRepository.getAllCategories().combine(productRepository.getProductById(productId)) { categories, product ->
                    Pair(categories, product)
                }.collect {
                    if (it.second != null && it.first.isNotEmpty()) {
                        _uiState.emit(EditProductState.Success(it.first, it.second!!))
                    }
                }
            } catch (e: Exception) {
                _uiState.emit(EditProductState.Error("Exception load item"))
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {

        }
    }

}

sealed class EditProductState: UiState {
    data class Loading(val dataSource: DataSource): EditProductState()
    data class Success(val categories: List<Category>, val product: Product): EditProductState()
    data class Error(val msg: String): EditProductState()
}

sealed class EditProductEvent: UiEvent {

}