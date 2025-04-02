package com.hyvu.thuytrangmarket.models.dataSource

import com.hyvu.thuytrangmarket.models.data.Product
import com.hyvu.thuytrangmarket.models.database.product.ProductDao
import com.hyvu.thuytrangmarket.models.database.product.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductLocalDataSource(private val productDao: ProductDao) {

    suspend fun insertProduct(product: ProductEntity) {
        productDao.insert(product)
    }

    fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    suspend fun deleteProduct(product: ProductEntity) {
        productDao.delete(product)
    }

    suspend fun updateProduct(product: ProductEntity) {
        productDao.update(product)
    }

    fun getProductById(itemId: String): Flow<ProductEntity?> {
        return productDao.getProductById(itemId)
    }

    suspend fun deleteProductById(productId: String) {
        productDao.deleteById(productId)
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAll()
    }

    fun getProductsByCategoryId(categoryId: String): Flow<List<ProductEntity>> {
        return productDao.getProductsByCategoryId(categoryId)
    }

    suspend fun getProductsBySyncStatus(isSync: Boolean): List<ProductEntity> {
        return productDao.getProductsBySyncStatus(isSync)
    }

    suspend fun getProductsByNameContaining(inputText: String): List<ProductEntity> {
        return productDao.getProductsByNameContaining(inputText)
    }

    suspend fun markItemAsDeleted(productId: String) {
        return productDao.markItemAsDeleted(productId)
    }
}
