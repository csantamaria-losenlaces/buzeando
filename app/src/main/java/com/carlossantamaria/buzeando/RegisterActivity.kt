package com.carlossantamaria.buzeando

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etCodPostal: EditText
    private lateinit var etMovil: EditText
    private lateinit var etCorreoElec: EditText
    private lateinit var etContrasena: EditText

    private lateinit var cbCondiciones: CheckBox
    private lateinit var btnRegistro: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        etDireccion = findViewById(R.id.etDireccion)
        etCodPostal = findViewById(R.id.etCodPostal)
        etMovil = findViewById(R.id.etMovil)
        etCorreoElec = findViewById(R.id.etCorreoElec)
        etContrasena = findViewById(R.id.etContrasena)

        cbCondiciones = findViewById(R.id.cbCondiciones)
        btnRegistro = findViewById(R.id.btnRegistro)


        btnRegistro.setOnClickListener {
            crearCuenta()
            it.ocultarTeclado()
        }

        inicializarPlacesAPI()

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(this)

        val startAutocomplete =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    if (intent != null) {
                        val place = Autocomplete.getPlaceFromIntent(intent)
                        Log.i("Places API", "Place: ${place.name}, ${place.id}")
                    }
                } else if (result.resultCode == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i("Places API", "User canceled autocomplete")
                }
            }

        startAutocomplete.launch(intent)

    }

    private fun crearCuenta() {
        if (!camposCumplimentados()) {
            Toast.makeText(this, "Asegúrate de rellenar todos los campos", Toast.LENGTH_SHORT)
                .show()
        } else if (!cbCondiciones.isChecked) {
            Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT)
                .show()
        } else {
            val url = "http://77.90.13.129/android/save.php"
            val parametros = hashMapOf(
                "nombre" to etNombre.text.toString(),
                "apellidos" to etApellidos.text.toString(),
                "dir" to etDireccion.text.toString(),
                "cod_postal" to etCodPostal.text.toString(),
                "movil" to etMovil.text.toString(),
                "mail" to etCorreoElec.text.toString(),
                "hash_pwd" to etContrasena.text.toString()
            )

            enviarDatos(url, parametros, onResponseListener = {
                Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show()
            }, onErrorListener = {
                Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun camposCumplimentados(): Boolean {
        return !(etNombre.text.isNullOrEmpty() || etApellidos.text.isNullOrEmpty() || etDireccion.text.isNullOrEmpty() || etCodPostal.text.isNullOrEmpty() || etMovil.text.isNullOrEmpty() || etCorreoElec.text.isNullOrEmpty() || etContrasena.text.isNullOrEmpty())
    }

    private fun enviarDatos(
        url: String,
        parametros: HashMap<String, String>,
        onResponseListener: (String) -> Unit,
        onErrorListener: (String) -> Unit
    ) {
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
            onResponseListener(response)
        }, Response.ErrorListener { error ->
            onErrorListener(error.message ?: "Error desconocido")
        }) {
            override fun getParams(): MutableMap<String, String> {
                return parametros
            }
        }

        requestQueue.add(stringRequest)
    }

    private fun View.ocultarTeclado() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
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

}