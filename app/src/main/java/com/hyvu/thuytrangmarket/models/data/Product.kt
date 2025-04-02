package com.hyvu.thuytrangmarket.models.data

import com.hyvu.thuytrangmarket.models.database.product.ProductEntity
import com.hyvu.thuytrangmarket.models.network.product.NetworkCreateProduct
import com.hyvu.thuytrangmarket.models.network.product.NetworkProduct
import java.io.Serializable

data class Product(
    val id: String,
    val globalId: String,
    val name: String,
    val categoryId: String,
    val description: String,
    val price: Double,
    val slug: String
): Serializable {
    fun isValid(): Boolean {
        return name.isNotEmpty() && categoryId.isNotEmpty() && price >= 0
    }
}

fun Product.toProductEntity(isSync: Boolean): ProductEntity {
    return ProductEntity(id, globalId, name, categoryId, description, price, isSync = isSync, slug)
}

fun Product.toNetworkProduct(): NetworkProduct {
    return NetworkProduct(globalId, id, name, categoryId, description, price, slug)
}

fun Product.toNetworkCreateProduct(): NetworkCreateProduct {
    return NetworkCreateProduct(id, name, categoryId, description, price)
}