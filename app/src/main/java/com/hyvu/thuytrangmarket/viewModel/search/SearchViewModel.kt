package com.hyvu.thuytrangmarket.viewModel.search

import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

class SearchViewModel(
    private val productRepository: ProductRepository
): BaseViewModel() {

    private val _uiState: MutableStateFlow<SearchViewState> = MutableStateFlow(SearchViewState.Loading(DataSource.LOCAL))
    val uiState: StateFlow<SearchViewState>
        get() = _uiState

    private val _uiEvent: MutableSharedFlow<SearchViewEvent> = MutableSharedFlow()
    val uiEvent: SharedFlow<SearchViewEvent>
        get() = _uiEvent

    fun onTextChange(searchText: String) {
        viewModelScope.launch {
            _uiState.emit(SearchViewState.Loading(DataSource.LOCAL))
            val items = productRepository.getProductsByNameContaining(searchText)
            _uiState.emit(SearchViewState.Success(items))
        }
    }

}

sealed class SearchViewState: UiState {
    data class Loading(val dataSource: DataSource): SearchViewState()
    data class Success(val products: List<Product>): SearchViewState()
    data class Error(val msg: String): SearchViewState()
}

sealed class SearchViewEvent: UiEvent {

}