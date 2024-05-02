package com.carlossantamaria.buzeando.imageupload

import com.carlossantamaria.buzeando.imageupload.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageService {
    @Multipart
    @POST("http://77.90.13.129/android/uploadimage.php?apicall=upload")
    suspend fun uploadImage(
        @Part("desc") requestBody: RequestBody,
        @Part image: MultipartBody.Part
    ): UploadResponse
}