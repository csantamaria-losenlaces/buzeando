package com.carlossantamaria.buzeando.imageupload

data class UploadImageState(
    val uploadResponse: UploadResponse = UploadResponse(),
    var isLoading: Boolean = false,
    val error: String? = null
)