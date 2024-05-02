package com.carlossantamaria.buzeando.imageupload

import com.carlossantamaria.buzeando.objects.Resource
import com.carlossantamaria.buzeando.imageupload.UploadResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ImageRepository {
    suspend fun uploadImage(image: MultipartBody.Part): Flow<Resource<UploadResponse>>
}