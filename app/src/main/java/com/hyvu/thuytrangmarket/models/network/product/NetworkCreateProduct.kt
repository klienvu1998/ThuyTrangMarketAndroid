package com.hyvu.thuytrangmarket.models.network.product

import com.google.gson.annotations.SerializedName

data class NetworkCreateProduct (
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
)