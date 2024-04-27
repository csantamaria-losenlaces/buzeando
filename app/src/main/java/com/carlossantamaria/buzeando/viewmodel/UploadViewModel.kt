package com.carlossantamaria.buzeando.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeFile
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlossantamaria.buzeando.Resource
import com.carlossantamaria.buzeando.data.UploadImageState
import com.carlossantamaria.buzeando.interfaces.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _sharedFlowUploadImage = MutableSharedFlow<UploadImageState>()
    val sharedFlowUploadImage = _sharedFlowUploadImage.asSharedFlow()

    fun uploadImage(image: MultipartBody.Part) {
        viewModelScope.launch {
            _sharedFlowUploadImage.emit(UploadImageState(isLoading = true))

            imageRepository.uploadImage(image).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.i("upload", "success")

                        result.data?.let { uploadImageInfo ->
                            _sharedFlowUploadImage.emit(
                                UploadImageState(
                                    isLoading = false,
                                    uploadResponse = uploadImageInfo
                                )
                            )
                        }
                    }

                    is Resource.Error -> {
                        Log.i("upload", "fail")

                        _sharedFlowUploadImage.emit(
                            UploadImageState(
                                error = result.message,
                                isLoading = false,
                            )
                        )
                    }

                    is Resource.Loading -> {
                        _sharedFlowUploadImage.emit(
                            UploadImageState(
                                isLoading = result.isLoading
                            )
                        )
                    }
                }
            }
        }
    }

    fun createMultipartBody(uri: Uri, multipartName: String): MultipartBody.Part {
        val documentImage = decodeFile(uri.path!!)
        val file = File(uri.path!!)
        val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
        documentImage.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.close()
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name = multipartName, file.name, requestBody)
    }

}