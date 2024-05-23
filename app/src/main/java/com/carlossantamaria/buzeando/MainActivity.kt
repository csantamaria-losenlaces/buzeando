package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.carlossantamaria.buzeando.objects.User

class MainActivity : AppCompatActivity() {

    private lateinit var btnIniciarSesion: Button
    private lateinit var btnRegistro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnIniciarSesion = findViewById(R.id.btnTengoCuenta)
        btnRegistro = findViewById(R.id.btnRegistrarCuenta)

        btnIniciarSesion.setOnClickListener { abrirIdentificar() }
        btnRegistro.setOnClickListener { abrirRegistro() }
    }

    override fun onResume() {
        super.onResume()
        if (User.id_usr.isNotBlank()) {
            btnIniciarSesion.isEnabled = false
            btnRegistro.isEnabled = false
            finish()
            abrirListaOfertas()
        }
    }

    private fun abrirIdentificar() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun abrirRegistro() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun abrirListaOfertas() {
        val intent = Intent(this, OfferListActivity::class.java)
        startActivity(intent)
    }

}