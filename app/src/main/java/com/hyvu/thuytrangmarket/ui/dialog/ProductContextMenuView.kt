package com.hyvu.thuytrangmarket.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.contains
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.base.BaseApiResponse
import com.hyvu.thuytrangmarket.base.BaseViewModel
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.base.UiEvent
import com.hyvu.thuytrangmarket.base.UiState
import com.hyvu.thuytrangmarket.databinding.DialogProductContextMenuBinding
import com.hyvu.thuytrangmarket.models.repository.ProductRepository
import com.hyvu.thuytrangmarket.ui.dialog.ProductContextMenuViewModel.ProductContextMenuViewState
import com.hyvu.thuytrangmarket.ui.editProduct.EditProductFragment
import com.hyvu.thuytrangmarket.utils.NumberUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductContextMenuView: BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ConfirmDialog"

        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        private const val ARG_PRODUCT_NAME = "ARG_PRODUCT_NAME"
        private const val ARG_PRODUCT_PRICE = "ARG_PRODUCT_PRICE"
        private const val ARG_GLOBAL_PRODUCT_ID = "ARG_GLOBAL_PRODUCT_ID"

        fun newInstance(productId: String, globalId: String, productName: String, price: Double): ProductContextMenuView {
            val f = ProductContextMenuView()
            f.arguments = Bundle().apply {
                putString(ARG_PRODUCT_ID, productId)
                putString(ARG_GLOBAL_PRODUCT_ID, globalId)
                putString(ARG_PRODUCT_NAME, productName)
                putDouble(ARG_PRODUCT_PRICE, price)
            }
            return f
        }
    }

    private val loadingView by lazy {
        LayoutInflater.from(context).inflate(R.layout.view_loading, null)
    }

    private val mViewModel by lazy {
        ViewModelProvider(this, ProductContextMenuViewModelFactory())[ProductContextMenuViewModel::class.java]
    }

    private lateinit var mBinding: DialogProductContextMenuBinding
    private var productName = ""
    private var productPrice = 0.0
    private var productId = ""
    private var globalId = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        productId = arguments?.getString(ARG_PRODUCT_ID) ?: ""
        globalId = arguments?.getString(ARG_GLOBAL_PRODUCT_ID) ?: ""
        productName = arguments?.getString(ARG_PRODUCT_NAME) ?: ""
        productPrice = arguments?.getDouble(ARG_PRODUCT_PRICE) ?: 0.0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogProductContextMenuBinding.bind(inflater.inflate(R.layout.dialog_product_context_menu, container, false))
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            launch {
                mViewModel.uiState.collect { uiState ->
                    if (uiState !is ProductContextMenuViewState.Loading) {
                        mBinding.root.removeView(loadingView)
                    }

                    when (uiState) {
                        is ProductContextMenuViewState.Loading -> {
                            if (uiState.dataSource == DataSource.NETWORK) {
                                if (!mBinding.root.contains(loadingView)) {
                                    mBinding.root.addView(loadingView)
                                }
                            }
                        }
                        else -> {

                        }
                    }
                }
            }

            launch {
                mViewModel.uiEvent.collect {
                    if (it is ProductContextMenuViewModel.ProductContextMenuViewEvent.Deleted) {
                        dismiss()
                    }
                }
            }
        }
    }

    private fun initView() {
        mBinding.productContainer.apply {
            tvName.text = productName
            tvPrice.text = NumberUtils.formatPrice(productPrice) + " VND"
        }

        mBinding.btnEdit.setOnClickListener {
            showEditView()
            dismiss()
        }

        mBinding.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog(requireContext(), productName) {
                mViewModel.deleteProduct(productId, globalId)
            }
        }
    }

    private fun showEditView() {
        requireParentFragment().parentFragmentManager.beginTransaction().add(R.id.container, EditProductFragment::class.java, Bundle().apply {
            putString(EditProductFragment.ARG_PRODUCT_ID, productId)
        }, EditProductFragment.TAG).addToBackStack(EditProductFragment.TAG).commit()
    }

    private fun showDeleteConfirmationDialog(context: Context, itemName: String, onConfirm: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Xóa sản phẩm")
        builder.setMessage("Bạn muốn xóa sản phẩm \"$itemName\"?")

        builder.setPositiveButton("Xóa") { dialog, _ ->
            onConfirm() // Execute the delete action
            dialog.dismiss()
        }

        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

}

class ProductContextMenuViewModel(
    val productRepository: ProductRepository
): BaseViewModel() {

    companion object {
        const val TAG = "ProductContextMenuViewModel"
    }

    protected val _uiState: MutableStateFlow<ProductContextMenuViewState> = MutableStateFlow(
        ProductContextMenuViewState.Loading(DataSource.LOCAL))
    val uiState: StateFlow<ProductContextMenuViewState>
        get() = _uiState

    protected val _uiEvent: MutableSharedFlow<ProductContextMenuViewEvent> = MutableSharedFlow()
    val uiEvent: SharedFlow<ProductContextMenuViewEvent>
        get() = _uiEvent


    fun deleteProduct(productId: String, globalId: String) {
        viewModelScope.launch {
            try {
                _uiState.emit(ProductContextMenuViewState.Loading(DataSource.LOCAL))
                productRepository.markItemAsDeleted(productId)

                if (globalId.isNotEmpty()) {
                    _uiState.emit(ProductContextMenuViewState.Loading(DataSource.NETWORK))
                    val result = productRepository.deleteProductNetwork(globalId)
                    if (result is BaseApiResponse.Success) {
                        productRepository.deleteProductById(productId)
                        _uiState.emit(ProductContextMenuViewState.Success)
                    }
                }
                _uiEvent.emit(ProductContextMenuViewEvent.Deleted)
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }
    }


    sealed class ProductContextMenuViewState: UiState {
        data class Loading(val dataSource: DataSource): ProductContextMenuViewState()
        object Success: ProductContextMenuViewState()
        data class Error(val msg: String): ProductContextMenuViewState()
    }

    sealed class ProductContextMenuViewEvent: UiEvent {
        object Deleted: ProductContextMenuViewEvent()
    }

}

class ProductContextMenuViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductContextMenuViewModel(ProductRepository.getInstance()) as T
    }
}