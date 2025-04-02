package com.hyvu.thuytrangmarket

import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.hyvu.thuytrangmarket.base.BaseActivity
import com.hyvu.thuytrangmarket.databinding.MainActivityBinding
import com.hyvu.thuytrangmarket.ui.home.HomeFragment
import com.hyvu.thuytrangmarket.viewModel.mainAcitivity.MainActivityViewModel
import com.hyvu.thuytrangmarket.viewModel.mainAcitivity.MainActivityViewModelFactory

class MainActivity : BaseActivity<MainActivityBinding>() {

    private val mViewModel by lazy {
        ViewModelProvider(this, MainActivityViewModelFactory())[MainActivityViewModel::class.java]
    }

    override fun getViewBinding(): MainActivityBinding {
        return MainActivityBinding.inflate(layoutInflater, null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        showHomeView()
    }

    private fun showHomeView() {
        supportFragmentManager.beginTransaction().add(R.id.container, HomeFragment::class.java, null, HomeFragment.TAG).commit()
    }

    fun showNetworkLoading(isShow: Boolean) {
        mBinding.networkLoading.isVisible = isShow
    }

    override fun onStart() {
        super.onStart()
        mViewModel.syncLocalDataToServer()
    }

}
