package com.hyvu.thuytrangmarket.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.base.BaseViewModel
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.base.UiEvent
import com.hyvu.thuytrangmarket.base.UiState
import com.hyvu.thuytrangmarket.databinding.DialogProductContextMenuBinding
import com.hyvu.thuytrangmarket.models.data.Product
import com.hyvu.thuytrangmarket.models.repository.ProductRepository
import com.hyvu.thuytrangmarket.ui.editProduct.EditProductFragment
import com.hyvu.thuytrangmarket.utils.NumberUtils

class ProductContextMenuView: BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ConfirmDialog"

        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        private const val ARG_PRODUCT_NAME = "ARG_PRODUCT_NAME"
        private const val ARG_PRODUCT_PRICE = "ARG_PRODUCT_PRICE"

        fun newInstance(productId: String, productName: String, price: Double): ProductContextMenuView {
            val f = ProductContextMenuView()
            f.arguments = Bundle().apply {
                putString(ARG_PRODUCT_ID, productId)
                putString(ARG_PRODUCT_NAME, productName)
                putDouble(ARG_PRODUCT_PRICE, price)
            }
            return f
        }
    }

    private val mViewModel by lazy {
        ViewModelProvider(this)[ProductContextMenuViewModel::class.java]
    }

    private lateinit var mBinding: DialogProductContextMenuBinding
    private var productName = ""
    private var productPrice = 0.0
    private var productId = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        productId = arguments?.getString(ARG_PRODUCT_ID) ?: ""
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

            dismiss()
        }
    }

    private fun showEditView() {
        requireParentFragment().parentFragmentManager.beginTransaction().add(R.id.container, EditProductFragment::class.java, Bundle().apply {
            putString(EditProductFragment.ARG_PRODUCT_ID, productId)
        }, EditProductFragment.TAG).addToBackStack(EditProductFragment.TAG).commit()
    }

}

class ProductContextMenuViewModel(
    val productRepository: ProductRepository
): BaseViewModel() {


    sealed class ProductContextMenuViewState: UiState {
        data class Loading(val dataSource: DataSource): ProductContextMenuViewState()
        data class Success(val product: Product): ProductContextMenuViewState()
        data class Error(val msg: String): ProductContextMenuViewState()
    }

    sealed class ProductContextMenuViewEvent: UiEvent {

    }

}