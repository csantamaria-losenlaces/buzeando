package com.carlossantamaria.buzeando

import com.carlossantamaria.buzeando.data.UploadResponse

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String? = null, val error: Throwable? = null) : Resource<UploadResponse>()
    data class Loading<T>(val isLoading: Boolean = false) : Resource<T>()
}