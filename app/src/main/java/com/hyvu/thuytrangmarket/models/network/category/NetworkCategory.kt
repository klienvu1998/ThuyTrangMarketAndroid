package com.hyvu.thuytrangmarket.models.network.category

import com.google.gson.annotations.SerializedName
import com.hyvu.thuytrangmarket.models.data.Category
import com.hyvu.thuytrangmarket.models.database.category.CategoryEntity

data class NetworkCategory(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String
) {
}

fun NetworkCategory.toCategory() = Category(id, name)
fun NetworkCategory.toCategoryEntity() = CategoryEntity(id, name, true)