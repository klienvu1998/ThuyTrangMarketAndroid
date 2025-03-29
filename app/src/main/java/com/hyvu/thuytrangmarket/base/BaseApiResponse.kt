package com.hyvu.thuytrangmarket.base

sealed class BaseApiResponse<T> {
    data class Success<T>(val data: T): BaseApiResponse<T>()
    data class Error<T>(val errCode: Int, val errMsg: String, val data: T? = null): BaseApiResponse<T>()
}