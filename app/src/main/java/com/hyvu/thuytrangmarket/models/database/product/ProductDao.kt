package com.hyvu.thuytrangmarket.models.database.product

import androidx.room.*
import com.hyvu.thuytrangmarket.models.data.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Delete
    suspend fun delete(product: ProductEntity)

    @Update
    suspend fun update(product: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: String): Flow<ProductEntity?> //flow will emit null if item not found.

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteById(productId: String)

    @Query("DELETE FROM products")
    suspend fun deleteAll()

    @Query("SELECT * FROM products WHERE category_id = :categoryId")
    fun getProductsByCategoryId(categoryId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isSync = :isSync")
    suspend fun getProductsBySyncStatus(isSync: Boolean): List<ProductEntity>

    @Query("SELECT * FROM products WHERE slug LIKE '%' || :inputText || '%'")
    suspend fun getProductsByNameContaining(inputText: String): List<ProductEntity>

}