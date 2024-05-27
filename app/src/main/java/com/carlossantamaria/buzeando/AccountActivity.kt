package com.carlossantamaria.buzeando

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.carlossantamaria.buzeando.objects.User
import com.toxicbakery.bcrypt.Bcrypt

class AccountActivity : AppCompatActivity() {

    private lateinit var tvNombreValor: TextView
    private lateinit var tvApellidosValor: TextView
    private lateinit var etDireccion: TextView
    private lateinit var tvCodPostalValor: TextView
    private lateinit var etMovil: TextView
    private lateinit var etCorreoElec: TextView
    private lateinit var etContrasena: TextView
    private lateinit var etContrasenaRepetir: TextView
    private lateinit var btnGuardar: Button
    private lateinit var btnMapa: Button
    private lateinit var btnListaOfertas: Button
    private lateinit var btnConversaciones: Button
    private lateinit var tvCerrarSesion: TextView
    private val hayCambios:Array<Boolean> = arrayOf(false, false, false, false, false)
    private val nuevosDatos = hashMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityAccount)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        initComponents()
        recuperarDatosUsuario()
        initUI()
    }

    private fun initComponents() {
        tvNombreValor = findViewById(R.id.tvNombreValor)
        tvApellidosValor = findViewById(R.id.tvApellidosValor)
        etDireccion = findViewById(R.id.etDireccion)
        tvCodPostalValor = findViewById(R.id.tvCodPostalValor)
        etMovil = findViewById(R.id.etMovil)
        etCorreoElec = findViewById(R.id.etCorreoElec)
        etContrasena = findViewById(R.id.etContrasena)
        etContrasenaRepetir = findViewById(R.id.etRepetirContrasena)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnMapa = findViewById(R.id.btnMapa)
        btnListaOfertas = findViewById(R.id.btnListaOfertas)
        btnConversaciones = findViewById(R.id.btnConversaciones)
        tvCerrarSesion = findViewById(R.id.tvCerrarSesion)
    }

    private fun initUI() {
        etDireccion.addTextChangedListener {
            hayCambios[0] = etDireccion.text.toString() != User.dir
            btnGuardar.isEnabled = hayCambios.contains(true)
            if (hayCambios[0]) nuevosDatos["dir"] = etDireccion.text.toString() else nuevosDatos.remove("dir")
        }
        etMovil.addTextChangedListener {
            hayCambios[1] = etMovil.text.toString() != User.movil
            btnGuardar.isEnabled = hayCambios.contains(true)
            if (hayCambios[1]) nuevosDatos["movil"] = etMovil.text.toString() else nuevosDatos.remove("movil")
        }
        etCorreoElec.addTextChangedListener {
            hayCambios[2] = etCorreoElec.text.toString() != User.mail
            btnGuardar.isEnabled = hayCambios.contains(true)
            if (hayCambios[2]) nuevosDatos["mail"] = etCorreoElec.text.toString() else nuevosDatos.remove("mail")
        }
        etContrasena.addTextChangedListener {
            hayCambios[3] = (etContrasena.text.isNotEmpty() && etContrasenaRepetir.text.isNotEmpty())
            hayCambios[4] = (etContrasena.text.isNotEmpty() && etContrasenaRepetir.text.isNotEmpty())
            btnGuardar.isEnabled = hayCambios.contains(true)
        }
        etContrasenaRepetir.addTextChangedListener {
            hayCambios[4] = (etContrasenaRepetir.text.isNotEmpty() && etContrasena.text.isNotEmpty())
            hayCambios[3] = (etContrasena.text.isNotEmpty() && etContrasenaRepetir.text.isNotEmpty())
            btnGuardar.isEnabled = hayCambios.contains(true)
        }
        btnGuardar.setOnClickListener {
            it.ocultarTeclado()
            if (etContrasena.text.isNotEmpty()) {
                if (!claveCoincide()) {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Los campos de contraseña no coinciden")
                        .setPositiveButton("Volver") { dialog, _ -> dialog.dismiss() }
                    builder.create().show()
                } else {
                    if (hayCambios[3]) nuevosDatos["hash_pwd"] = Bcrypt.hash(etContrasena.text.toString(), 12).decodeToString() else nuevosDatos.remove("hash_pwd")
                }
            }
            nuevosDatos["id_usr"] = User.id_usr
            actualizarDatosUsuario()
            this.recreate()
        }
        tvCerrarSesion.setOnClickListener { cerrarSesion() }
        btnMapa.setOnClickListener { abrirMapa() }
        btnListaOfertas.setOnClickListener { abrirListaOfertas() }
        btnConversaciones.setOnClickListener { abrirConversaciones() }
    }

   private fun recuperarDatosUsuario() {
        tvNombreValor.text = User.nombre
        tvApellidosValor.text = User.apellidos
        etDireccion.text = User.dir
        tvCodPostalValor.text = User.cod_postal
        etMovil.text = User.movil
        etCorreoElec.text = User.mail
    }

    private fun claveCoincide(): Boolean = (etContrasena.text.toString() == etContrasenaRepetir.text.toString())

    private fun abrirMapa() {
        val intent = Intent(this, OfferMapActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun abrirListaOfertas() {
        val intent = Intent(this, OfferListActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun abrirConversaciones() {
        val intent = Intent(this, ConversationsActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun View.ocultarTeclado() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun correoElecExiste(callback: (Int) -> Unit) {
        val url = "http://77.90.13.129/android/checkemail.php?mail=${etCorreoElec.text}"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                Log.i("Log personalizado", "Respuesta exitosa")
                val numCoincidencias = response.getString("count").toInt()
                callback(numCoincidencias)
            },
            {
                Log.i("Log personalizado", "Respuesta no exitosa")
                Toast.makeText(this, "Ha ocurrido un error al verificar el e-mail. Inténtalo más tarde.", Toast.LENGTH_LONG)
                    .show()
                callback(-1) // Envía un valor predeterminado en caso de error
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    private fun actualizarDatosUsuario() {
        val url = "http://77.90.13.129/android/updateprofile.php"
        correoElecExiste { existe ->
            if (existe != 0) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("El e-mail introducido ya existe")
                    .setPositiveButton("Volver") { dialog, _ -> dialog.dismiss() }
                builder.create().show()
            } else {
                enviarDatosUsuario(url, nuevosDatos, onResponseListener = {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Datos actualizados. Si has cambiado la contraseña, utiliza la nueva la próxima vez que inicies sesión.")
                        .setPositiveButton("Aceptar") { dialog, _ ->
                            dialog.dismiss()
                        }
                    builder.create().show()
                }, onErrorListener = {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Ha ocurrido un error al actualizar los datos. Inténtalo de nuevo más tarde.")
                        .setPositiveButton("Aceptar") { dialog, _ ->
                            dialog.dismiss()
                        }
                    builder.create().show()
                })
            }
        }
    }

    private fun enviarDatosUsuario(
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

    private fun cerrarSesion() {
        User.cerrarSesion()
        Toast.makeText(this, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
        abrirMainActivity()
    }

    private fun abrirMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

}