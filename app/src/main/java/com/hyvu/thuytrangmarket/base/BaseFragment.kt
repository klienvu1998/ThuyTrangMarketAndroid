package com.hyvu.thuytrangmarket.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hyvu.thuytrangmarket.R

abstract class BaseFragment<T: ViewBinding>: Fragment() {

    protected lateinit var mBinding: T

    protected val loadingView by lazy {
        LayoutInflater.from(context).inflate(R.layout.view_loading, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSavedInstanceState(savedInstanceState)
        getBundle()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = getViewBinding(inflater, container)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        initData()
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): T
    open fun getSavedInstanceState(savedInstanceState: Bundle?) {}
    open fun getBundle() {}
    open fun initView() {}
    open fun initObserver() {}
    open fun initData() {}

}