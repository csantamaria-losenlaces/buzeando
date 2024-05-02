package com.carlossantamaria.buzeando

import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeFile
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.carlossantamaria.buzeando.imageupload.UploadViewModel
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class AddOfferActivity : AppCompatActivity() {

    private lateinit var btnSubirImagen: MaterialButton
    private lateinit var uploadViewModel: UploadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_offer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //uploadViewModel = ViewModelProvider(this)[UploadViewModel::class.java]

        /*val galleryLauncher =
            registerForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = it.data
                    if (data != null) {
                        val imageUri = Uri.parse(data.data.toString())
                        if (imageUri.toString().isNotEmpty()) {
                            Log.d("myImageUri", "$imageUri ")
                            uploadViewModel.uploadImage(
                                createMultipartBody(
                                    uri = imageUri,
                                    multipartName = "image"
                                )
                            )
                        }
                    }
                }
            }*/

        btnSubirImagen = findViewById(R.id.btnSubirImagen)

        btnSubirImagen.setOnClickListener {
            /*val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(galleryIntent)*/
        }
    }

    // Recibe URI de la imagen y devuelve MultiBody.Part para usar en petición HTTP
    private fun createMultipartBody(uri: Uri, multipartName: String): MultipartBody.Part {
        val documentImage = decodeFile(uri.path!!)
        val file = File(uri.path!!)
        val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
        documentImage.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.close()
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name = multipartName, file.name, requestBody)
    }

}