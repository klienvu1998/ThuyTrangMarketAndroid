package com.hyvu.thuytrangmarket.models.data

import com.hyvu.thuytrangmarket.models.database.category.CategoryEntity

data class Category(
    val id: String,
    val name: String
) {

}

fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(id, name)
}