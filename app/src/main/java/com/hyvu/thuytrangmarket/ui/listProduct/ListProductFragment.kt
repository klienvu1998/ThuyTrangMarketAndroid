package com.hyvu.thuytrangmarket.ui.listProduct

import android.R.attr
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.savedstate.SavedStateRegistryOwner
import com.hyvu.thuytrangmarket.MainActivity
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.base.BaseFragment
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.databinding.FragmentListProductBinding
import com.hyvu.thuytrangmarket.models.data.Product
import com.hyvu.thuytrangmarket.ui.dialog.ProductContextMenuView
import com.hyvu.thuytrangmarket.utils.DividerItemDecoration
import com.hyvu.thuytrangmarket.utils.toast
import com.hyvu.thuytrangmarket.viewModel.home.HomeUiState
import com.hyvu.thuytrangmarket.viewModel.listProduct.ListProductUiState
import com.hyvu.thuytrangmarket.viewModel.listProduct.ListProductViewModel
import com.hyvu.thuytrangmarket.viewModel.listProduct.ListProductViewModelFactory
import kotlinx.coroutines.launch
import android.R.attr.bitmap
import java.io.ByteArrayOutputStream
import android.R.attr.name
import androidx.core.view.contains


class ListProductFragment : BaseFragment<FragmentListProductBinding>() {

    companion object {
        const val TAG = "ListProductFragment"

        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"

        const val IS_LOADED = "IS_LOADED"
    }

    private lateinit var categoryId: String
    private val mViewModel by lazy {
        ViewModelProvider(this, ListProductViewModelFactory())[ListProductViewModel::class.java]
    }
    private val mAdapter by lazy { ListProductAdapter() }
    private var isLoaded = false

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListProductBinding {
        val v = inflater.inflate(R.layout.fragment_list_product, container, false)
        return FragmentListProductBinding.bind(v)
    }

    override fun getSavedInstanceState(savedInstanceState: Bundle?) {
        super.getSavedInstanceState(savedInstanceState)
        isLoaded = savedInstanceState?.getBoolean(IS_LOADED) ?: false
    }

    override fun getBundle() {
        super.getBundle()
        categoryId = arguments?.getString(ARG_CATEGORY_ID, "") ?: ""
    }

    override fun initData() {
        super.initData()
        if (!isLoaded) {
            mViewModel.loadData(categoryId)
            isLoaded = true
        }
    }

    private val onRecyclerViewListener = object : ListProductAdapter.Listener {

        override fun onLongClickItem(bm: Bitmap, product: Product) {
            val contextMenu = ProductContextMenuView.newInstance(product.id, product.globalId, product.name, product.price)
            contextMenu.show(parentFragmentManager, ProductContextMenuView.TAG)
        }

        override fun onClickListener(product: Product) {

        }

    }

    override fun initView() {
        super.initView()

        mBinding.rcvProducts.apply {
            adapter = mAdapter
            mAdapter.setupListener(onRecyclerViewListener)
            val dividerDrawable = ContextCompat.getDrawable(this.context, android.R.drawable.divider_horizontal_bright)

            if (dividerDrawable != null) {
                val dividerItemDecoration = DividerItemDecoration()
//                addItemDecoration(dividerItemDecoration)
            }
            layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        }

    }

    override fun initObserver() {
        super.initObserver()

        lifecycleScope.launch {
            launch {
                mViewModel.uiEvent.collect { event ->

                }
            }

            launch {
                mViewModel.uiState.collect { uiState ->
                    if (uiState !is ListProductUiState.Loading) {
                        mBinding.root.removeView(loadingView)
                    } else {
                        if (activity is MainActivity) {
                            (activity as MainActivity).showNetworkLoading(false)
                        }
                    }

                    when (uiState) {
                        is ListProductUiState.Loading -> {
                            if (uiState.dataSource == DataSource.LOCAL) {
                                if (!mBinding.root.contains(loadingView)) {
                                    mBinding.root.addView(loadingView)
                                }
                            } else {
                                if (activity is MainActivity) {
                                    (activity as MainActivity).showNetworkLoading(true)
                                }
                            }
                        }

                        is ListProductUiState.Success -> {
                            onSuccessState(uiState)
                        }

                        is ListProductUiState.Error -> {
                            context?.toast(uiState.msg)
                        }

                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun onSuccessState(uiState: ListProductUiState.Success) {
        mAdapter.setData(uiState.products)
    }

}