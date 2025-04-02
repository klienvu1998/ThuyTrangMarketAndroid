package com.hyvu.thuytrangmarket.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hyvu.thuytrangmarket.models.data.Category
import com.hyvu.thuytrangmarket.ui.listProduct.ListProductFragment

class HomeViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    var items = ArrayList<Category>()
        private set

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return ListProductFragment().apply {
            arguments = Bundle().apply {
                putString(ListProductFragment.ARG_CATEGORY_ID, items[position].id)
            }
        }
    }

    fun setData(items: List<Category>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

}