package com.hyvu.thuytrangmarket.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hyvu.thuytrangmarket.models.database.category.CategoryDao
import com.hyvu.thuytrangmarket.models.database.category.CategoryEntity
import com.hyvu.thuytrangmarket.models.database.product.ProductDao
import com.hyvu.thuytrangmarket.models.database.product.ProductEntity


@Database(entities = [ProductEntity::class, CategoryEntity::class], version = 1) // Replace ItemEntity and version
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
}

object DatabaseProvider {

    private const val DATABASE_NAME = "app_db"

    private var database: AppDatabase? = null

    private fun getDatabase(context: Context): AppDatabase {
        if (database == null) {
            synchronized(this) {
                if (database == null) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
        }
        return database!!
    }

    fun getProductDao(context: Context): ProductDao {
        return getDatabase(context).productDao()
    }

    fun getCategoryDao(context: Context): CategoryDao {
        return getDatabase(context).categoryDao()
    }
}
