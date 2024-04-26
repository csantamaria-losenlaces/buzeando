package com.carlossantamaria.buzeando.interfaces

import com.carlossantamaria.buzeando.Resource
import com.carlossantamaria.buzeando.data.UploadResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ImageRepository {
    suspend fun uploadImage(image: MultipartBody.Part): Flow<Resource<UploadResponse>>
}