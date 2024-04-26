package com.carlossantamaria.buzeando.interfaces

import com.carlossantamaria.buzeando.data.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageService {
    @Multipart
    @POST("your/api/endpoint")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("image") requestBody: RequestBody
    ): UploadResponse
}