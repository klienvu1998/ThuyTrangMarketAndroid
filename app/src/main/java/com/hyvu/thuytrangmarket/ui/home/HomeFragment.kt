package com.hyvu.thuytrangmarket.ui.home

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.hyvu.thuytrangmarket.MainActivity
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.base.BaseFragment
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.databinding.FragmentHomeBinding
import com.hyvu.thuytrangmarket.ui.createProduct.CreateProductFragment
import com.hyvu.thuytrangmarket.ui.search.SearchFragment
import com.hyvu.thuytrangmarket.utils.toast
import com.hyvu.thuytrangmarket.viewModel.home.HomeUiState
import com.hyvu.thuytrangmarket.viewModel.home.HomeViewModel
import com.hyvu.thuytrangmarket.viewModel.home.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val adapter by lazy { HomeViewPagerAdapter(this) }
    private val mViewModel by lazy { ViewModelProvider(this, HomeViewModelFactory())[HomeViewModel::class.java] }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()

        initToolBar()
        mBinding.viewPager.adapter = adapter
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab, position ->
            tab.text = adapter.items[position].name
        }.attach()

        mBinding.btnAdd.setOnClickListener {
            showCreateProductView()
        }
    }

    private fun showCreateProductView() {
        parentFragmentManager.beginTransaction().add(R.id.container, CreateProductFragment::class.java, null, CreateProductFragment.TAG).addToBackStack(CreateProductFragment.TAG).commit()
    }

    private fun initToolBar() {
        mBinding.btnSearch.setOnClickListener {
            showSearchView()
        }
    }

    private fun showSearchView() {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.add(R.id.container, SearchFragment::class.java, null, SearchFragment.TAG)
        transaction.addToBackStack(SearchFragment.TAG) // Optional: Add to back stack
        transaction.commit()
    }

    override fun initObserver() {
        super.initObserver()
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                mViewModel.uiEvent.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect { uiEvent ->
                    when (uiEvent) {
                        else -> {}
                    }
                }
            }

            launch {
                mViewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect { uiState ->
                    if (uiState !is HomeUiState.Loading) {
                        mBinding.root.removeView(loadingView)
                        if (activity is MainActivity) {
                            (activity as MainActivity).showNetworkLoading(false)
                        }
                    }

                    when (uiState) {
                        is HomeUiState.Loading -> {
                            if (uiState.dataSource == DataSource.LOCAL) {
//                                mBinding.root.addView(loadingView)
                            } else {
                                if (activity is MainActivity) {
                                    (activity as MainActivity).showNetworkLoading(true)
                                }
                            }
                        }

                        is HomeUiState.Success -> {
                            adapter.setData(uiState.categories)
                        }

                        is HomeUiState.Error -> {
                            context?.toast(uiState.msg)
                        }
                    }
                }
            }
        }
    }
}