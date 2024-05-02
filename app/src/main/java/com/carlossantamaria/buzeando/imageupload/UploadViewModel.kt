package com.carlossantamaria.buzeando.imageupload

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlossantamaria.buzeando.objects.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
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

}