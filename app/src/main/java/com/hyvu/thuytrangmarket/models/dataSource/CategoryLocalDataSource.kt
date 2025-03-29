package com.hyvu.thuytrangmarket.models.dataSource

import com.hyvu.thuytrangmarket.models.database.category.CategoryDao
import com.hyvu.thuytrangmarket.models.database.category.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryLocalDataSource(private val categoryDao: CategoryDao) {

    suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insert(category)
    }

    fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    suspend fun getCategoriesBySyncStatus(isSync: Boolean): List<CategoryEntity> {
        return categoryDao.getCategoriesBySyncStatus(isSync)
    }

    suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.update(category)
    }

    suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.delete(category)
    }
}