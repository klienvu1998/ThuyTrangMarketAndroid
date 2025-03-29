package com.hyvu.thuytrangmarket.viewModel.createProduct

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
import com.hyvu.thuytrangmarket.viewModel.home.HomeUiEvent
import com.hyvu.thuytrangmarket.viewModel.home.HomeUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateProductViewModel(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
): BaseViewModel() {

    companion object {
        const val TAG = "CreateProductViewModel"
    }

    protected val _uiState: MutableStateFlow<CreateProductState> = MutableStateFlow(CreateProductState.Loading(DataSource.LOCAL))
    val uiState: StateFlow<CreateProductState>
        get() = _uiState

    protected val _uiEvent: MutableSharedFlow<CreateProductEvent> = MutableSharedFlow()
    val uiEvent: SharedFlow<CreateProductEvent>
        get() = _uiEvent

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.emit(CreateProductState.Loading(DataSource.LOCAL))
            try {
                categoryRepository.getAllCategories().collect {
                    _uiState.emit(CreateProductState.Success(it))
                }
            } catch (e: Exception) {
                _uiState.emit(CreateProductState.Error("Load category failed"))
            }
        }
    }

    fun createProduct(product: Product) {
        viewModelScope.launch {
            if (product.isValid()) {
                productRepository.insertProduct(product)
                NetworkCoroutineScope.getInstance().launchSuspend {
                    val result = productRepository.createProduct(product)
                    if (result is BaseApiResponse.Success) {
                        productRepository.insertProduct(result.data)
                    } else if (result is BaseApiResponse.Error) {
                        Log.e(TAG, "insert product failed")
                    }
                }
                _uiEvent.emit(CreateProductEvent.CreateProductSuccess)
            } else {
                _uiState.emit(CreateProductState.Error(getString(R.string.str_product_non_valid)))
            }
        }
    }

}

sealed class CreateProductState: UiState {
    data class Loading(val dataSource: DataSource): CreateProductState()
    data class Success(val categories: List<Category>): CreateProductState()
    data class Error(val msg: String): CreateProductState()
}

sealed class CreateProductEvent: UiEvent {
    object CreateProductSuccess: CreateProductEvent()
}