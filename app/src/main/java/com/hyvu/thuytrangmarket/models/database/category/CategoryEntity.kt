package com.hyvu.thuytrangmarket.models.database.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hyvu.thuytrangmarket.models.data.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val isSync: Boolean = false
) {


}

fun CategoryEntity.toCategory(): Category {
    return Category(id, name)
}