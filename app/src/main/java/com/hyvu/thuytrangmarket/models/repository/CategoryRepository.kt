package com.hyvu.thuytrangmarket.models.repository

import android.util.Log
import com.hyvu.thuytrangmarket.MainApplication
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.models.data.Category
import com.hyvu.thuytrangmarket.models.data.toCategoryEntity
import com.hyvu.thuytrangmarket.models.database.DatabaseProvider
import com.hyvu.thuytrangmarket.models.database.category.toCategory
import com.hyvu.thuytrangmarket.models.dataSource.CategoryLocalDataSource
import com.hyvu.thuytrangmarket.models.dataSource.CategoryNetworkDataSource
import com.hyvu.thuytrangmarket.models.network.category.CategoryApiService
import com.hyvu.thuytrangmarket.models.network.category.toCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CategoryRepository(
    private val localDataSource: CategoryLocalDataSource,
    private val networkCategory: CategoryNetworkDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    object HOLDER {
        val repository by lazy {
            CategoryRepository(
                CategoryLocalDataSource(DatabaseProvider.getCategoryDao(MainApplication.getAppContext())),
                CategoryNetworkDataSource(CategoryApiService.getService())
            )
        }
    }

    companion object {
        fun getInstance(): CategoryRepository {
            return HOLDER.repository
        }
    }

    suspend fun insertCategory(category: Category, dataSource: DataSource) {
        val isSync = dataSource == DataSource.NETWORK
        localDataSource.insertCategory(category.toCategoryEntity().copy(isSync = isSync))
    }

    fun getAllCategories(): Flow<List<Category>> {
        val categories = localDataSource.getAllCategories().map { it.map { it.toCategory() } }
        return categories
    }

    suspend fun getCategoriesBySyncStatus(isSync: Boolean): List<Category> {
        val categories = localDataSource.getCategoriesBySyncStatus(isSync).map { it.toCategory() }
        return categories
    }

    suspend fun deleteCategory(category: Category) {
        localDataSource.deleteCategory(category.toCategoryEntity())
    }

    suspend fun updateCategory(category: Category) {
        localDataSource.updateCategory(category.toCategoryEntity())
    }

    suspend fun fetchAllCategories(): List<Category> = withContext(ioDispatcher) {
        return@withContext networkCategory.getCategories().map { it.toCategory() }
    }
}