package com.hyvu.thuytrangmarket.viewModel.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hyvu.thuytrangmarket.base.BaseViewModel
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.base.UiEvent
import com.hyvu.thuytrangmarket.base.UiState
import com.hyvu.thuytrangmarket.models.data.Category
import com.hyvu.thuytrangmarket.models.repository.CategoryRepository
import com.hyvu.thuytrangmarket.models.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
): BaseViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    protected val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading(DataSource.LOCAL))
    val uiState: StateFlow<HomeUiState>
        get() = _uiState

    protected val _uiEvent: MutableSharedFlow<HomeUiEvent> = MutableSharedFlow()
    val uiEvent: SharedFlow<HomeUiEvent>
        get() = _uiEvent

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.emit(HomeUiState.Loading(dataSource = DataSource.LOCAL))

            try {
                val localData = categoryRepository.getAllCategories().firstOrNull() ?: emptyList()
                _uiState.emit(HomeUiState.Success(localData))

                _uiState.emit(HomeUiState.Loading(dataSource = DataSource.NETWORK))
                try {
                    val networkCategories = categoryRepository.fetchAllCategories()
                    networkCategories.forEach { nCategories ->
                        categoryRepository.insertCategory(nCategories)
                    }
                    _uiState.emit(HomeUiState.Success(networkCategories))
                } catch (e: Exception) {
                    Log.e(TAG, e.message ?: "")
                    if (localData.isEmpty()) {
                        _uiState.emit(HomeUiState.Error(e.message ?: ""))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "")
                _uiState.emit(HomeUiState.Error(e.message ?: ""))
            }
        }
    }


}

sealed class HomeUiState: UiState {
    data class Loading(val dataSource: DataSource): HomeUiState()
    data class Success(val categories: List<Category>): HomeUiState()
    data class Error(val msg: String): HomeUiState()
}

sealed class HomeUiEvent: UiEvent {

}