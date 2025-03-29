package com.hyvu.thuytrangmarket

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import com.hyvu.thuytrangmarket.base.BaseActivity
import com.hyvu.thuytrangmarket.databinding.MainActivityBinding
import com.hyvu.thuytrangmarket.ui.home.HomeFragment
import com.hyvu.thuytrangmarket.ui.search.SearchFragment

class MainActivity : BaseActivity<MainActivityBinding>() {

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

}
