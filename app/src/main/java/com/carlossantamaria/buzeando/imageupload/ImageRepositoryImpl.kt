package com.carlossantamaria.buzeando.imageupload

import com.carlossantamaria.buzeando.objects.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val imageService: ImageService
) : ImageRepository {

    override suspend fun uploadImage(image: MultipartBody.Part): Flow<Resource<UploadResponse>> {
        val requestBody: RequestBody =
            "image".toRequestBody("multipart/form-data".toMediaTypeOrNull())

        return flow {
            try {
                emit(Resource.Loading(true))
                val uploadImageResponse = imageService.uploadImage(
                    requestBody = requestBody,
                    image = image
                )
                emit(Resource.Success(data = uploadImageResponse))
                emit(Resource.Loading(false))
            } catch (e: IOException) {
                emit(Resource.Error(message = "No se ha podido subir la imagen"))
            } catch (e: HttpException) {
                emit(Resource.Error(message = "${e.message}"))
            }
        }
    }

}