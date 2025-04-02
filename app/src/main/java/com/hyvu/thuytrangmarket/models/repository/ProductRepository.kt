package com.hyvu.thuytrangmarket.models.repository

import android.util.Log
import com.hyvu.thuytrangmarket.MainApplication
import com.hyvu.thuytrangmarket.base.BaseApiResponse
import com.hyvu.thuytrangmarket.base.DataSource
import com.hyvu.thuytrangmarket.base.NetworkErrorCode
import com.hyvu.thuytrangmarket.models.data.Product
import com.hyvu.thuytrangmarket.models.data.toNetworkCreateProduct
import com.hyvu.thuytrangmarket.models.data.toNetworkProduct
import com.hyvu.thuytrangmarket.models.data.toProductEntity
import com.hyvu.thuytrangmarket.models.database.DatabaseProvider
import com.hyvu.thuytrangmarket.models.database.product.toProduct
import com.hyvu.thuytrangmarket.models.dataSource.ProductLocalDataSource
import com.hyvu.thuytrangmarket.models.dataSource.ProductNetworkDatasource
import com.hyvu.thuytrangmarket.models.dataSource.ProductNetworkDatasource.Companion.TAG
import com.hyvu.thuytrangmarket.models.database.product.ProductEntity
import com.hyvu.thuytrangmarket.models.network.product.ProductApiService
import com.hyvu.thuytrangmarket.models.network.product.toProduct
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProductRepository(
    private val localDatasource: ProductLocalDataSource,
    private val networkDatasource: ProductNetworkDatasource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    object HOLDER {
        val repository by lazy {
            ProductRepository(
                ProductLocalDataSource(DatabaseProvider.getProductDao(MainApplication.getAppContext())),
                ProductNetworkDatasource(ProductApiService.getService())
            )
        }
    }

    companion object {
        fun getInstance() = HOLDER.repository
    }

    suspend fun insertProduct(product: Product, dataSource: DataSource) {
        val isSync = dataSource == DataSource.NETWORK
        localDatasource.insertProduct(product.toProductEntity(isSync))
    }

    fun getAllProducts(): Flow<List<Product>> {
        return localDatasource.getAllProducts().map { it.map { it.toProduct() } }
    }

    suspend fun deleteProduct(product: Product, dataSource: DataSource) {
        val isSync = dataSource == DataSource.NETWORK
        localDatasource.deleteProduct(product.toProductEntity(isSync, isDeleted = true))
    }

    suspend fun updateProduct(product: Product, dataSource: DataSource) {
        val isSync = dataSource == DataSource.NETWORK
        localDatasource.updateProduct(product.toProductEntity(isSync))
    }

    fun getProductById(productId: String): Flow<Product?> {
        return localDatasource.getProductById(productId).map { it?.toProduct() }
    }

    suspend fun deleteProductById(productId: String) {
        localDatasource.deleteProductById(productId)
    }

    suspend fun deleteAllProducts() {
        localDatasource.deleteAllProducts()
    }

    fun getProductsByCategory(categoryId: String): Flow<List<Product>> {
        return localDatasource.getProductsByCategoryId(categoryId).map { it.map { it.toProduct() } }
    }

    suspend fun getProductsBySyncStatus(isSync: Boolean): List<ProductEntity> {
        return localDatasource.getProductsBySyncStatus(isSync)
    }

    suspend fun getProductsByNameContaining(inputText: String): List<Product> {
        return localDatasource.getProductsByNameContaining(inputText).map { it.toProduct() }
    }

    suspend fun markItemAsDeleted(productId: String) {
        return localDatasource.markItemAsDeleted(productId)
    }

    suspend fun fetchProductsByCategory(categoryId: String): BaseApiResponse<List<Product>> = withContext(ioDispatcher) {
        return@withContext try {
            val data = networkDatasource.fetchProductsByCategory(categoryId).map { it.toProduct() }
            BaseApiResponse.Success(data)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
            BaseApiResponse.Error(NetworkErrorCode.UNKNOWN, e.message ?: "")
        }
    }

    suspend fun createProduct(product: Product): BaseApiResponse<Product> = withContext(ioDispatcher) {
        return@withContext try {
            val data = networkDatasource.createProduct(product.toNetworkCreateProduct())
            BaseApiResponse.Success(data.toProduct())
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
            BaseApiResponse.Error(NetworkErrorCode.UNKNOWN, e.message ?: "")
        }
    }

    suspend fun updateProductNetwork(product: Product): BaseApiResponse<Product> = withContext(ioDispatcher) {
        return@withContext try {
            val data = networkDatasource.updateProduct(product.globalId, product.toNetworkCreateProduct())
            BaseApiResponse.Success(data.toProduct())
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
            BaseApiResponse.Error(NetworkErrorCode.UNKNOWN, e.message ?: "")
        }
    }

    suspend fun deleteProductNetwork(productId: String): BaseApiResponse<Boolean> = withContext(ioDispatcher) {
        return@withContext try {
            val data = networkDatasource.deleteProduct(productId)
            BaseApiResponse.Success(data)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
            BaseApiResponse.Error(NetworkErrorCode.UNKNOWN, e.message ?: "")
        }
    }

}