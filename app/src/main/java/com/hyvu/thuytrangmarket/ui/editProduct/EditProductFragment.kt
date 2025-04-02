package com.hyvu.thuytrangmarket.ui.editProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.contains
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.base.BaseFragment
import com.hyvu.thuytrangmarket.databinding.FragmentCreateProductBinding
import com.hyvu.thuytrangmarket.models.data.Category
import com.hyvu.thuytrangmarket.models.data.Product
import com.hyvu.thuytrangmarket.utils.toast
import com.hyvu.thuytrangmarket.viewModel.editProduct.EditProductEvent
import com.hyvu.thuytrangmarket.viewModel.editProduct.EditProductState
import com.hyvu.thuytrangmarket.viewModel.editProduct.EditProductViewModel
import com.hyvu.thuytrangmarket.viewModel.editProduct.EditProductViewModelFactory
import kotlinx.coroutines.launch


class EditProductFragment : BaseFragment<FragmentCreateProductBinding>() {

    companion object {
        const val TAG = "EditProductFragment"

        const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        const val IS_LOADED = "IS_LOADED"
    }

    private val mViewModel by lazy {
        ViewModelProvider(this, EditProductViewModelFactory())[EditProductViewModel::class.java]
    }

    private var currentCategories = emptyList<Category>()
    private var productId: String = ""
    private var isLoaded = false

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateProductBinding {
        val v = inflater.inflate(R.layout.fragment_create_product, container, false)
        return FragmentCreateProductBinding.bind(v)
    }

    override fun getBundle() {
        super.getBundle()
        productId = arguments?.getString(ARG_PRODUCT_ID) ?: throw Exception("Empty productId")
    }

    override fun getSavedInstanceState(savedInstanceState: Bundle?) {
        super.getSavedInstanceState(savedInstanceState)
        isLoaded = savedInstanceState?.getBoolean(IS_LOADED) ?: false
    }

    override fun initView() {
        super.initView()
        mBinding.priceContainer.apply {
            editText.inputType = EditorInfo.TYPE_CLASS_NUMBER
            tvTitle.text = "Giá"
        }
        mBinding.nameContainer.tvTitle.text = "Tên"
        mBinding.descriptionContainer.tvTitle.text = "Mô tả"
        mBinding.btnSubmit.text = "Cập nhật"
    }

    override fun initData() {
        super.initData()
        if (!isLoaded) {
            mViewModel.loadData(productId)
            isLoaded = true
        }
    }

    private fun isValidInput(): Boolean {
        return getInputName().isNotEmpty() && getPrice() >= 0 && getCategoryId().isNotEmpty()
    }

    override fun initObserver() {
        super.initObserver()

        lifecycleScope.launch {
            launch {
                mViewModel.uiState.collect { uiState ->
                    if (uiState !is EditProductState.Loading) {
                        mBinding.root.removeView(loadingView)
                    }

                    when (uiState) {
                        is EditProductState.Loading -> {
                            if (!mBinding.root.contains(loadingView)) {
                                mBinding.root.addView(loadingView)
                            }
                        }
                        is EditProductState.Success -> {
                            val categories = uiState.categories
                            currentCategories = categories
                            populateSpinner(categories = categories)

                            val product = uiState.product
                            setProductDetailsView(product, categories)
                        }
                        is EditProductState.Error -> {
                            context.toast(uiState.msg)
                        }
                    }
                }
            }

            launch {
                mViewModel.uiEvent.collect { event ->
                    when (event) {
                        EditProductEvent.EditSuccess -> {
                            parentFragmentManager.popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun setProductDetailsView(product: Product, categories: List<Category>) {
        mBinding.nameContainer.editText.setText(product.name)
        mBinding.descriptionContainer.editText.setText(product.description)
        mBinding.priceContainer.editText.setText(product.price.toString())
        mBinding.categoryPicker.setSelection(categories.indexOfFirst { it.id == product.categoryId })

        mBinding.btnSubmit.setOnClickListener {
            if (isValidInput()) {
                mViewModel.updateProduct(Product(
                    id = product.id,
                    globalId = product.globalId,
                    name = getInputName(),
                    categoryId = getCategoryId(),
                    description = getDescription(),
                    price = getPrice(),
                    slug = product.slug
                ))
            } else {
                context.toast(getString(R.string.str_input_product_invalid))
            }
        }
    }

    private fun populateSpinner(categories: List<Category>) {
        val context = context ?: return
        val categoryNames = categories.map { it.name } // Assuming Category has a 'name' property
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.categoryPicker.apply {
            this.adapter = adapter

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedCategory = categories[position]
                    // Handle the selected category (e.g., store it in a variable)
//                    context.toast("Selected: ${selectedCategory.name}")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case where nothing is selected (optional)
                }
            }
        }
    }

    private fun getInputName(): String {
        return mBinding.nameContainer.editText.text.toString()
    }

    private fun getDescription(): String {
        return mBinding.descriptionContainer.editText.text.toString()
    }

    private fun getCategoryId(): String {
        return currentCategories[mBinding.categoryPicker.selectedItemPosition].id
    }

    private fun getPrice(): Double {
        return mBinding.priceContainer.editText.text.toString().takeIf { it.isNotEmpty() }?.toDouble() ?: -1.0
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_LOADED, isLoaded)
    }
}