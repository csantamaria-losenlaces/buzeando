package com.carlossantamaria.buzeando

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeFile
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.carlossantamaria.buzeando.imageupload.UploadViewModel
import com.carlossantamaria.buzeando.objects.User
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddOfferActivity : AppCompatActivity() {

    private lateinit var rgTipo: RadioGroup
    private lateinit var etTituloOferta: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var tvCodPostalValor: TextView
    private lateinit var btnSubirImagen: Button
    private lateinit var btnElegirUbicacion: Button
    private lateinit var tvDireccion: TextView
    private lateinit var btnCrearOferta: Button
    private lateinit var uploadViewModel: UploadViewModel
    private lateinit var startAutocomplete: ActivityResultLauncher<Intent>
    private lateinit var coordsLatLng: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_offer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initUI()
        inicializarPlacesAPI()

        startAutocomplete =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent: Intent? = result.data
                    if (intent != null) {
                        val place = Autocomplete.getPlaceFromIntent(intent)
                        val addressComponents = place.addressComponents

                        coordsLatLng = place.latLng!!
                        Log.i("Valor de coordsLatLng", "${coordsLatLng.latitude}, ${coordsLatLng.longitude}")

                        tvDireccion.text = StringBuilder()
                            .append("Dirección: ")
                            .append(place.name)

                        addressComponents?.asList()?.forEach { component ->
                            Log.i("Places API", component.toString())
                            val codPostal = component.types.find { "postal_code" == it }
                            if (!codPostal.isNullOrEmpty()) tvCodPostalValor.text = component.name
                        }
                    }
                } else if (result.resultCode == Activity.RESULT_CANCELED) {
                    Log.i("Places API", "User canceled autocomplete")
                }
            }

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

    }

    private fun initComponents() {
        rgTipo = findViewById(R.id.rgTipo)
        etTituloOferta = findViewById(R.id.etTituloOferta)
        etDescripcion = findViewById(R.id.etDescripcion)
        etPrecio = findViewById(R.id.etPrecio)
        tvCodPostalValor = findViewById(R.id.tvCodPostalValor)
        btnSubirImagen = findViewById(R.id.btnSubirImagen)
        btnElegirUbicacion = findViewById(R.id.btnElegirUbicacion)
        tvDireccion = findViewById(R.id.tvDireccion)
        btnCrearOferta = findViewById(R.id.btnCrearOferta)
        // uploadViewModel = ViewModelProvider(this)[UploadViewModel::class.java]
    }

    private fun initUI() {
        btnSubirImagen.setOnClickListener {
            Toast.makeText(this, "Esta función estará disponible muy pronto", Toast.LENGTH_SHORT).show()
            /*val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(galleryIntent)*/
        }
        btnElegirUbicacion.setOnClickListener {
            it.ocultarTeclado()
            lanzarPlacesAPI()
        }
        btnCrearOferta.setOnClickListener {
            if (!camposCumplimentados()) {
                Toast.makeText(this, "Asegúrate de elegir el tipo de oferta y rellenar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                it.ocultarTeclado()
                crearOferta()
            }
        }
    }

    private fun inicializarPlacesAPI() {
        // Define a variable to hold the Places API key.
        val apiKey = BuildConfig.PLACES_API_KEY

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        // Initialize the SDK
        Places.initialize(applicationContext, apiKey)
    }

    private fun lanzarPlacesAPI() {
        // Set the fields to specify which types of place data to return after the user has made a selection
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG)

        // Start the autocomplete intent
        val autoCompleteIntent: Intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountries(listOf("ES"))
                .setTypesFilter(listOf("address"))
                .build(this)

        startAutocomplete.launch(autoCompleteIntent)
    }

    private fun camposCumplimentados(): Boolean {
        return !(rgTipo.checkedRadioButtonId == -1 || etTituloOferta.text.isNullOrEmpty() || etDescripcion.text.isNullOrEmpty() || etPrecio.text.isNullOrEmpty() || tvCodPostalValor.text == "(Sin definir)")
    }

    private fun View.ocultarTeclado() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun crearOferta() {
        val url = "http://77.90.13.129/android/addoffer.php"
        var tipoOferta = ""

        when (rgTipo.checkedRadioButtonId) {
            R.id.rbProducto -> tipoOferta = "Producto"
            R.id.rbServicio -> tipoOferta = "Servicio"
        }

        Log.i("coordsLatLng", "${coordsLatLng.latitude}, ${coordsLatLng.longitude}")

        val parametros = hashMapOf(
            "id_usr" to User.id_usr,
            "tipo" to tipoOferta,
            "fecha" to LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString(),
            "titulo" to etTituloOferta.text.toString(),
            "descripcion" to etDescripcion.text.toString(),
            "coste" to etPrecio.text.toString(),
            "cod_postal" to tvCodPostalValor.text.toString(),
            "coords_lat" to coordsLatLng.latitude.toString(),
            "coords_long" to coordsLatLng.longitude.toString()
        )

        parametros.forEach { (t, u) -> Log.i("Array de parámetros:", "$t = $u") }

        enviarDatosNuevaOferta(url, parametros, onResponseListener = {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Oferta creada con éxito. Vas a regresar al listado de ofertas")
                .setPositiveButton("Aceptar") { _, _ -> abrirListaOfertas() }
            builder.create().show()
        }) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Ha ocurrido un error al crear la oferta. Revisa si la información es correcta o inténtalo de nuevo más tarde")
                .setPositiveButton("Volver") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create().show()
        }
    }

    private fun enviarDatosNuevaOferta(
        url: String,
        parametros: HashMap<String, String>,
        onResponseListener: (String) -> Unit,
        onErrorListener: (String) -> Unit
    ) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
            onResponseListener(response)
            Log.i("Volley", response.toString())
        }, Response.ErrorListener { error ->
            onErrorListener(error.message ?: "Error desconocido")
            Log.i("Volley", error.toString())
        }) {
            override fun getParams(): MutableMap<String, String> {
                return parametros
            }
        }
        requestQueue.add(stringRequest)
    }

    private fun abrirListaOfertas() {
        val intent = Intent(this, OfferListActivity::class.java)
        finish()
        startActivity(intent)
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