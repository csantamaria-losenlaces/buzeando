package com.carlossantamaria.buzeando.data

data class UploadImageState(
    val uploadResponse: UploadResponse = UploadResponse(),
    var isLoading: Boolean = false,
    val error: String? = null
)