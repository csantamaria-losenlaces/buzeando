package com.carlossantamaria.buzeando

import android.content.Context
import android.content.Intent
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
import com.carlossantamaria.buzeando.objects.User
import com.carlossantamaria.buzeando.offerlist.OfferListActivity
import org.json.JSONObject

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
            it.ocultarTeclado()
            identificarUsuario()
        }

    }

    private fun View.ocultarTeclado() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
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
                    populateUserAttributes(response)
                    Toast.makeText(this, "Te has identificado correctamente", Toast.LENGTH_SHORT).show()
                    abrirListaOfertas()
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

    private fun populateUserAttributes(response: JSONObject) {
        User.id_usr = response.getString("id_usr")
        User.nombre = response.getString("nombre")
        User.apellidos = response.getString("apellidos")
        User.dir = response.getString("dir")
        User.cod_postal = response.getString("cod_postal")
        User.movil = response.getString("movil")
        User.mail = response.getString("mail")
        User.hash_pwd = response.getString("hash_pwd")
    }

    private fun abrirListaOfertas() {
        val intent = Intent(this, OfferListActivity::class.java)
        finish()
        startActivity(intent)
    }

}