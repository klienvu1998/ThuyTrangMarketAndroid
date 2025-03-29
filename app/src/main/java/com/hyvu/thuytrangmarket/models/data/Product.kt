package com.hyvu.thuytrangmarket.models.data

import com.hyvu.thuytrangmarket.models.database.product.ProductEntity
import com.hyvu.thuytrangmarket.models.network.product.NetworkCreateProduct
import com.hyvu.thuytrangmarket.models.network.product.NetworkProduct

data class Product(
    val id: String,
    val name: String,
    val categoryId: String,
    val description: String,
    val price: Double
) {
    fun isValid(): Boolean {
        return name.isNotEmpty() && categoryId.isNotEmpty() && price >= 0
    }
}

fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(id, name, categoryId, description, price, isSync = true)
}

fun Product.toNetworkProduct(): NetworkProduct {
    return NetworkProduct("", name, categoryId, description, price, "")
}

fun Product.toNetworkCreateProduct(): NetworkCreateProduct {
    return NetworkCreateProduct(name, categoryId, description, price)
}