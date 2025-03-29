package com.hyvu.thuytrangmarket.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.base.BaseFragment
import com.hyvu.thuytrangmarket.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    companion object {
        const val TAG = "SearchFragment"
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        val v = inflater.inflate(R.layout.fragment_search, container)
        return FragmentSearchBinding.bind(v)
    }

    fun filterList(newText: String?) {

    }


}