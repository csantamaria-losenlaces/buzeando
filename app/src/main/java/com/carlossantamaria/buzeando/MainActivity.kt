package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.carlossantamaria.buzeando.objects.User
import com.carlossantamaria.buzeando.offerlist.OfferListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnIniciarSesion: Button
    private lateinit var btnRegistro: Button
    private lateinit var btnMapaOfertas: Button
    private lateinit var btnNuevaOferta: Button
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

        btnMapaOfertas = findViewById(R.id.btnMapa)
        btnNuevaOferta = findViewById(R.id.btnNuevaOferta)
        btnListaOfertas = findViewById(R.id.btnListaOfertas)
        btnDetallesOferta = findViewById(R.id.btnDetallesOferta)


        btnIniciarSesion.setOnClickListener { abrirIdentificar() }
        btnRegistro.setOnClickListener { abrirRegistro() }

        btnNuevaOferta.setOnClickListener { abrirNuevaOferta() }
        btnMapaOfertas.setOnClickListener { abrirMapa() }
        btnListaOfertas.setOnClickListener { abrirListaOfertas() }
        btnDetallesOferta.setOnClickListener { abrirDetallesOferta() }

    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "Has vuelto a la actividad", Toast.LENGTH_SHORT).show()
        if (User.id_usr.isNotBlank()) {
            btnIniciarSesion.isEnabled = false
            btnRegistro.isEnabled = false
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