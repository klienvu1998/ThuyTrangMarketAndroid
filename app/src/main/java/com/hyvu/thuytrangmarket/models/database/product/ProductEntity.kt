package com.hyvu.thuytrangmarket.models.database.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hyvu.thuytrangmarket.models.data.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val globalId: String,
    val name: String,
    @ColumnInfo(name = "category_id")
    val categoryId: String,
    val description: String,
    val price: Double,
    val isSync: Boolean = false,
    val slug: String
)

fun ProductEntity.toProduct(): Product {
    return Product(id, globalId, name, categoryId, description, price, slug)
}