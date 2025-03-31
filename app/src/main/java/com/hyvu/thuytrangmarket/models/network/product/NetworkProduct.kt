package com.hyvu.thuytrangmarket.models.network.product

import com.google.gson.annotations.SerializedName
import com.hyvu.thuytrangmarket.models.data.Product

data class NetworkProduct(
    @SerializedName("_id")
    val id: String,
    @SerializedName("clientId")
    val clientId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("slug")
    val slug: String
)

fun NetworkProduct.toProduct(): Product {
    return Product(
        clientId, id, name, categoryId, description, price
    )
}
