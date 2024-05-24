package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.carlossantamaria.buzeando.objects.Offer
import com.carlossantamaria.buzeando.offerlist.OfferListAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class OfferListActivity : AppCompatActivity() {

    private lateinit var adapterListaOfertas: OfferListAdapter
    private lateinit var rvListaOfertas: RecyclerView
    private lateinit var etBusqueda: EditText
    private lateinit var btnFiltrar: Button
    private lateinit var btnCrearOferta: Button
    private lateinit var btnMapa: Button
    private lateinit var btnCuenta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_offer_list)
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
        adapterListaOfertas = OfferListAdapter(emptyList()) { abrirDetallesOferta(it) }
        rvListaOfertas = findViewById(R.id.rvListaOfertas)
        etBusqueda = findViewById(R.id.etBusqueda)
        btnFiltrar = findViewById(R.id.btnFiltrar)
        btnCrearOferta = findViewById(R.id.btnCrearOferta)
        btnMapa = findViewById(R.id.btnMapa)
        btnCuenta = findViewById(R.id.btnCuenta)
    }

    private fun initUI() {
        rvListaOfertas.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvListaOfertas.adapter = adapterListaOfertas

        etBusqueda.doOnTextChanged { text, _, _, _ ->
            adapterListaOfertas.filter.filter(text)
        }

        btnCrearOferta.setOnClickListener { abrirCrearOferta() }
        btnMapa.setOnClickListener { abrirMapa() }
        btnCuenta.setOnClickListener { abrirCuenta() }
    }

    private fun abrirCrearOferta() {
        val intent = Intent(this, AddOfferActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun abrirMapa() {
        val intent = Intent(this, OfferMapActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun abrirCuenta() {
        val intent = Intent(this, AccountActivity::class.java)
        finish()
        startActivity(intent)
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
                (0 until response.length()).forEach {
                    val offer = response.getJSONObject(it)
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
            { _ ->
                Toast.makeText(this, "No se han podido cargar las ofertas", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun actualizarRecyclerView() {
        cargarOfertas { offerList ->
            if (offerList.isNotEmpty()) {
                adapterListaOfertas.setData(offerList)
            }
        }
    }

    private fun abrirDetallesOferta(offer: Offer) {
        val intent = Intent(this, OfferDetailsActivity::class.java)
        intent.putExtra("offer_object", offer)
        startActivity(intent)
    }

}