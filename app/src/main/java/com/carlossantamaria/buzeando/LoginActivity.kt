package com.carlossantamaria.buzeando

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class LoginActivity : AppCompatActivity() {

    private lateinit var etCorreoElec: EditText
    private lateinit var etContrasena: EditText

    private lateinit var btnAcceder: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) {v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etCorreoElec = findViewById(R.id.etCorreoElec)
        etContrasena = findViewById(R.id.etContrasena)

        btnAcceder = findViewById(R.id.btnAcceder)

        btnAcceder.setOnClickListener {
            identificarUsuario()
            it.ocultarTeclado()
        }

    }

    private fun identificarUsuario() {

        if (!camposCumplimentados()) {
            Toast.makeText(this, "Asegúrate de rellenar todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            val url = "http://77.90.13.129/android/fetch.php?mail=${etCorreoElec.text}&hash_pwd=${etContrasena.text}"
            val requestQueue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    val nombre = response.getString("nombre")
                    Toast.makeText(this, "¡Bienvenido, $nombre!", Toast.LENGTH_SHORT).show()
                },
                {
                    Toast.makeText(this, "Los datos son incorrectos o la cuenta no existe", Toast.LENGTH_SHORT).show()
                }
            )

            requestQueue.add(jsonObjectRequest)
        }
    }

    private fun camposCumplimentados(): Boolean {
        return !(etCorreoElec.text.isNullOrEmpty() || etContrasena.text.isNullOrEmpty())
    }

    private fun View.ocultarTeclado() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}