package com.hyvu.thuytrangmarket.viewModel.listProduct

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hyvu.thuytrangmarket.base.BaseApiResponse
import com.hyvu.thuytrangmarket.base.BaseViewModel
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.base.UiEvent
import com.hyvu.thuytrangmarket.base.UiState
import com.hyvu.thuytrangmarket.models.data.Product
import com.hyvu.thuytrangmarket.models.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ListProductViewModel(
    private val productRepository: ProductRepository
): BaseViewModel() {

    companion object {
        const val TAG = "ListProductViewModel"
    }

    private val _uiState: MutableStateFlow<ListProductUiState> = MutableStateFlow(ListProductUiState.Loading(DataSource.LOCAL))
    val uiState: StateFlow<ListProductUiState>
        get() = _uiState

    private val _uiEvent: MutableSharedFlow<ListProductUiEvent> = MutableSharedFlow()
    val uiEvent: SharedFlow<ListProductUiEvent>
        get() = _uiEvent

    fun loadData(categoryId: String) {
        viewModelScope.launch {
            try {
                _uiState.emit(ListProductUiState.Loading(DataSource.LOCAL))
                launch {
                    productRepository.getProductsByCategory(categoryId).collect {
                        _uiState.emit(ListProductUiState.Success(it))
                    }
                }
                try {
                    val result = productRepository.fetchProductsByCategory(categoryId)
                    if (result is BaseApiResponse.Success) {
                        result.data.forEach {
                            productRepository.insertProduct(it, DataSource.NETWORK)
                        }
                    } else {
                        throw Exception("Can not get products by categoryId")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "")
                _uiState.emit(ListProductUiState.Error(e.message ?: ""))
            }
        }
    }
}

sealed class ListProductUiState: UiState {
    data class Loading(val dataSource: DataSource): ListProductUiState()
    data class Success(val products: List<Product>): ListProductUiState()
    data class Error(val msg: String): ListProductUiState()
}

sealed class ListProductUiEvent: UiEvent {

}