package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.carlossantamaria.buzeando.objects.Offer
import com.carlossantamaria.buzeando.offerlist.OfferListAdapter
import com.google.android.material.search.SearchBar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OfferListActivity : AppCompatActivity() {

    private lateinit var adapterListaOfertas: OfferListAdapter
    private lateinit var rvListaOfertas: RecyclerView

    private lateinit var sbDireccion: SearchBar
    private lateinit var btnFiltrar: Button
    private lateinit var btnCrearOferta: Button
    private lateinit var btnMapa: Button
    private lateinit var btnPerfil: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initUI()

        actualizarRecyclerView()
    }

    private fun initComponents() {
        adapterListaOfertas = OfferListAdapter(emptyList())
        rvListaOfertas = findViewById(R.id.rvListaOfertas)

        sbDireccion = findViewById(R.id.sbDireccion)
        btnFiltrar = findViewById(R.id.btnFiltrar)
        btnCrearOferta = findViewById(R.id.btnCrearOferta)
        btnMapa = findViewById(R.id.btnMapa)
        btnPerfil = findViewById(R.id.btnPerfil)
    }

    private fun initUI() {
        rvListaOfertas.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvListaOfertas.adapter = adapterListaOfertas

        sbDireccion.setOnClickListener {  }
        btnFiltrar.setOnClickListener {  }
        btnCrearOferta.setOnClickListener { abrirCrearOferta() }
        btnMapa.setOnClickListener { abrirMapa() }
        btnPerfil.setOnClickListener { abrirPerfil() }
    }

    private fun abrirCrearOferta() {
        val intent = Intent(this, AddOfferActivity::class.java)
        startActivity(intent)
    }

    private fun abrirMapa() {
        val intent = Intent(this, OfferMapActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun abrirPerfil() {
        /*val intent = Intent(this, OfferListActivity::class.java)
        startActivity(intent)*/
        Toast.makeText(this, "Esta sección estará disponible muy pronto", Toast.LENGTH_SHORT).show()
    }

    private fun cargarOfertas(callback: (MutableList<Offer>) -> Unit) {
        val url = "http://77.90.13.129/android/getoffers.php"
        val requestQueue = Volley.newRequestQueue(this)
        val offerList = mutableListOf<Offer>()

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                Log.i("Volley", response.toString())
                (0 until response.length()).forEach {
                    val offer = response.getJSONObject(it)
                    Log.i("Volley", "Título de la oferta: ${offer.get("titulo")}")
                    offerList.add(Offer(
                        offer.get("id_oferta").toString().toInt(),
                        offer.get("id_usr").toString().toInt(),
                        offer.get("tipo").toString(),
                        LocalDateTime.parse(offer.get("fecha").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        offer.get("titulo").toString(),
                        offer.get("descripcion").toString(),
                        offer.get("coste").toString().toDouble(),
                        offer.get("cod_postal").toString(),
                        offer.get("ruta_img_1").toString(),
                        offer.get("ruta_img_2").toString(),
                        offer.get("ruta_img_3").toString(),
                        offer.get("coords_lat").toString().toDouble(),
                        offer.get("coords_long").toString().toDouble()
                    ))
                }
                callback(offerList)
            },
            { error ->
                Toast.makeText(this, "No se han podido cargar las ofertas", Toast.LENGTH_SHORT).show()
                Log.i("Volley", error.message.toString())
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun actualizarRecyclerView() {
        cargarOfertas { offerList ->
            if (offerList.isNotEmpty()) {
                adapterListaOfertas.update(offerList)
            }
        }
    }

}