package com.hyvu.thuytrangmarket.ui.createProduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hyvu.thuytrangmarket.R
import com.hyvu.thuytrangmarket.base.BaseFragment
import com.hyvu.thuytrangmarket.databinding.FragmentCreateProductBinding
import com.hyvu.thuytrangmarket.models.data.Category
import com.hyvu.thuytrangmarket.models.data.Product
import com.hyvu.thuytrangmarket.utils.toast
import com.hyvu.thuytrangmarket.viewModel.createProduct.CreateProductEvent
import com.hyvu.thuytrangmarket.viewModel.createProduct.CreateProductState
import com.hyvu.thuytrangmarket.viewModel.createProduct.CreateProductViewModel
import com.hyvu.thuytrangmarket.viewModel.createProduct.CreateProductViewModelFactory
import kotlinx.coroutines.launch
import kotlin.random.Random


class CreateProductFragment : BaseFragment<FragmentCreateProductBinding>() {

    companion object {
        const val TAG = "CreateProductFragment"
    }

    private val mViewModel by lazy {
        ViewModelProvider(this, CreateProductViewModelFactory())[CreateProductViewModel::class.java]
    }

    private var currentCategories = emptyList<Category>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateProductBinding {
        val v = inflater.inflate(R.layout.fragment_create_product, container, false)
        return FragmentCreateProductBinding.bind(v)
    }

    override fun initView() {
        super.initView()
        mBinding.priceContainer.apply {
            editText.inputType = EditorInfo.TYPE_CLASS_NUMBER
            tvTitle.text = "Giá"
        }
        mBinding.nameContainer.tvTitle.text = "Tên"
        mBinding.descriptionContainer.tvTitle.text = "Mô tả"

        mBinding.btnSubmit.setOnClickListener {
            if (isValidInput()) {
                mViewModel.createProduct(Product(
                    id = System.currentTimeMillis().toString(),
                    globalId = "",
                    name = getInputName(),
                    categoryId = getCategoryId(),
                    description = getDescription(),
                    price = getPrice()
                ))
            } else {
                context.toast(getString(R.string.str_input_product_invalid))
            }
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
                    if (uiState !is CreateProductState.Loading) {
                        mBinding.root.removeView(loadingView)
                    }

                    when (uiState) {
                        is CreateProductState.Loading -> {
                            mBinding.root.addView(loadingView)
                        }
                        is CreateProductState.Success -> {
                            currentCategories = uiState.categories
                            populateSpinner(categories = uiState.categories)
                        }
                        is CreateProductState.Error -> {
                            context.toast(uiState.msg)
                        }
                    }
                }
            }

            launch {
                mViewModel.uiEvent.collect { event ->
                    when (event) {
                        is CreateProductEvent.CreateProductSuccess -> {
                            parentFragmentManager.popBackStack()
                        }
                    }
                }
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
                    context.toast("Selected: ${selectedCategory.name}")
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
}