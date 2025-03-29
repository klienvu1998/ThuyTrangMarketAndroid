package com.hyvu.thuytrangmarket.base

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding>: FragmentActivity() {

    protected lateinit var mBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundle()
        mBinding = getViewBinding()
        setContentView(mBinding.root)
    }

    open fun getBundle() {}

    abstract fun getViewBinding(): T

}