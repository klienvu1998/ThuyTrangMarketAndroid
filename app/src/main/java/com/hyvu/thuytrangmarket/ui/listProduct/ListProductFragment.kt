package com.hyvu.thuytrangmarket.ui.listProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.savedstate.SavedStateRegistryOwner
import com.hyvu.thuytrangmarket.MainActivity
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.base.BaseFragment
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.databinding.FragmentListProductBinding
import com.hyvu.thuytrangmarket.utils.toast
import com.hyvu.thuytrangmarket.viewModel.home.HomeUiState
import com.hyvu.thuytrangmarket.viewModel.listProduct.ListProductUiState
import com.hyvu.thuytrangmarket.viewModel.listProduct.ListProductViewModel
import com.hyvu.thuytrangmarket.viewModel.listProduct.ListProductViewModelFactory
import kotlinx.coroutines.launch

class ListProductFragment : BaseFragment<FragmentListProductBinding>() {

    companion object {
        const val TAG = "ListProductFragment"

        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
    }

    private lateinit var categoryId: String
    private val mViewModel by lazy {
        ViewModelProvider(this, ListProductViewModelFactory())[ListProductViewModel::class.java]
    }
    private val mAdapter by lazy { ListProductAdapter() }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListProductBinding {
        val v = inflater.inflate(R.layout.fragment_list_product, container, false)
        return FragmentListProductBinding.bind(v)
    }

    override fun getBundle() {
        super.getBundle()
        categoryId = arguments?.getString(ARG_CATEGORY_ID, "") ?: ""
    }

    override fun initData() {
        super.initData()
        mViewModel.loadData(categoryId)
    }

    override fun initView() {
        super.initView()

        mBinding.rcvProducts.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
                                mBinding.root.addView(loadingView)
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
                    }
                }
            }
        }
    }

    private fun onSuccessState(uiState: ListProductUiState.Success) {
        mAdapter.setData(uiState.products)
    }

}