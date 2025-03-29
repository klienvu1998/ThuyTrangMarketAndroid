package com.hyvu.thuytrangmarket.ui.listProduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.databinding.ItemProductBinding
import com.hyvu.thuytrangmarket.models.data.Product
import com.hyvu.thuytrangmarket.utils.NumberUtils

class ListProductAdapter: RecyclerView.Adapter<ListProductAdapter.ViewHolder>() {

    var items = ArrayList<Product>()
        private set

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        var mBinding: ItemProductBinding = ItemProductBinding.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder.mBinding) {
            img.setImageResource(R.drawable.ic_img_picker)
            tvName.text = item.name
            tvPrice.text = NumberUtils.formatPrice(item.price)
        }
    }

    fun setData(items: List<Product>) {
        this.items.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }
}