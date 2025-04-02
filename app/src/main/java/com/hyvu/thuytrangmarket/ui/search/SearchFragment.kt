package com.hyvu.thuytrangmarket.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.base.BaseFragment
import com.hyvu.thuytrangmarket.databinding.FragmentSearchBinding
import com.hyvu.thuytrangmarket.utils.textChanges
import com.hyvu.thuytrangmarket.viewModel.search.SearchViewModel
import com.hyvu.thuytrangmarket.viewModel.search.SearchViewModelFactory
import com.hyvu.thuytrangmarket.viewModel.search.SearchViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    companion object {
        const val TAG = "SearchFragment"
    }

    private val mViewModel by lazy { ViewModelProvider(this, SearchViewModelFactory())[SearchViewModel::class.java] }
    private val mAdapter by lazy { SearchAdapter() }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        return FragmentSearchBinding.bind(v)
    }

    override fun initView() {
        super.initView()
        mBinding.searchView.apply {
            setIconifiedByDefault(false)
            textChanges().debounce(300)
                .onEach { mViewModel.onTextChange(it.toString()) }
                .launchIn(lifecycleScope)
        }

        mBinding.rcvProducts.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun initObserver() {
        super.initObserver()
        lifecycleScope.launch {
            launch {
                mViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is SearchViewState.Loading -> {

                        }

                        is SearchViewState.Success -> {
                            onSuccessUi(uiState)
                        }

                        is SearchViewState.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun onSuccessUi(uiState: SearchViewState.Success) {
        mAdapter.setData(uiState.products)
    }


}