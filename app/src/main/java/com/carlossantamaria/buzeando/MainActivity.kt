package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.carlossantamaria.buzeando.objects.User

class MainActivity : AppCompatActivity() {

    private lateinit var btnIniciarSesion: Button
    private lateinit var btnRegistro: Button
    private lateinit var tvCerrarSesion: TextView

    // Temporal
    private lateinit var btnNuevaOferta: Button
    private lateinit var btnMapaOfertas: Button
    private lateinit var btnListaOfertas: Button
    private lateinit var btnDetallesOferta: Button

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
        tvCerrarSesion = findViewById(R.id.tvCerrarSesion)

        // Temporal
        btnNuevaOferta = findViewById(R.id.btnNuevaOferta)
        btnMapaOfertas = findViewById(R.id.btnMapa)
        btnListaOfertas = findViewById(R.id.btnListaOfertas)
        btnDetallesOferta = findViewById(R.id.btnDetallesOferta)


        btnIniciarSesion.setOnClickListener { abrirIdentificar() }
        btnRegistro.setOnClickListener { abrirRegistro() }
        tvCerrarSesion.setOnClickListener { cerrarSesion() }

        // Temporal
        btnNuevaOferta.setOnClickListener { abrirNuevaOferta() }
        btnMapaOfertas.setOnClickListener { abrirMapa() }
        btnListaOfertas.setOnClickListener { abrirListaOfertas() }
        btnDetallesOferta.setOnClickListener { abrirDetallesOferta() }

    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "Has vuelto a la actividad principal", Toast.LENGTH_SHORT).show()
        if (User.id_usr.isNotBlank()) {
            btnIniciarSesion.isEnabled = false
            btnRegistro.isEnabled = false
            tvCerrarSesion.isVisible = true
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

    private fun cerrarSesion() {
        btnIniciarSesion.isEnabled = true
        btnRegistro.isEnabled = true
        tvCerrarSesion.isVisible = false
        User.cerrarSesion()
        Toast.makeText(this, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
    }

    // Temporal
    private fun abrirNuevaOferta() {
        val intent = Intent(this, AddOfferActivity::class.java)
        startActivity(intent)
    }

    private fun abrirMapa() {
        val intent = Intent(this, MapViewActivity::class.java)
        startActivity(intent)
    }

    private fun abrirListaOfertas() {
        val intent = Intent(this, OfferListActivity::class.java)
        startActivity(intent)
    }

    private fun abrirDetallesOferta() {
        val intent = Intent(this, OfferDetailsActivity::class.java)
        startActivity(intent)
    }
}