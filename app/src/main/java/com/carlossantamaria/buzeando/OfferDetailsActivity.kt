package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.imagegallery.ImageGalleryAdapter
import com.carlossantamaria.buzeando.objects.Image
import com.carlossantamaria.buzeando.objects.Offer
import java.time.format.DateTimeFormatter
import java.util.Locale

class OfferDetailsActivity : AppCompatActivity() {

    private lateinit var offer: Offer
    private lateinit var tvTituloOferta: TextView
    private lateinit var tvFechaCreacion: TextView
    private lateinit var tvDescOferta: TextView
    private lateinit var adapterImageGallery: ImageGalleryAdapter
    private lateinit var rvImagenes: RecyclerView
    private lateinit var tvPrecio: TextView
    private lateinit var btnPerfil: Button
    private lateinit var btnChat: Button

    private val listaImagenes = listOf(
        Image("https://picsum.photos/226/170"),
        Image("https://picsum.photos/226/170"),
        Image("https://picsum.photos/226/170")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_offer_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        initUI()
        cargarDatosOferta()

        // DESCOMENTAR CUANDO HAYA LISTA DE CHATS
        /*if (offer.idUsr != Integer.parseInt(User.id_usr)) {
            btnChat.isEnabled = true
        }*/
        btnChat.isEnabled = true // QUITAR ESTO CUANDO HAYA LISTA DE CHATS

    }

    private fun initComponents() {
        adapterImageGallery = ImageGalleryAdapter(listaImagenes)
        rvImagenes = findViewById(R.id.rvImagenes)

        tvTituloOferta = findViewById(R.id.tvTituloOferta)
        tvFechaCreacion = findViewById(R.id.tvFechaCreacion)
        tvDescOferta = findViewById(R.id.tvDescOferta)
        tvPrecio = findViewById(R.id.tvPrecio)
        btnPerfil = findViewById(R.id.btnCuenta)
        btnChat = findViewById(R.id.btnChat)
    }

    private fun initUI() {
        btnPerfil.setOnClickListener { Toast.makeText(this, "Esta función estará disponible muy pronto", Toast.LENGTH_SHORT).show() }
        btnChat.setOnClickListener {
            abrirChat(offer)
        }
        rvImagenes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvImagenes.adapter = adapterImageGallery
    }

    private fun cargarDatosOferta() {
        offer = if (SDK_INT >= 33) {
            intent.getParcelableExtra("offer_object", Offer::class.java)!!
        } else {
            (intent.getParcelableExtra("offer_object") as? Offer)!!
        }

        tvTituloOferta.text = offer.titulo
        tvFechaCreacion.text = buildString {
            append("Creada el ")
            append(DateTimeFormatter.ofPattern("d/MM/yyyy").format(offer.fecha))
            append(" a las ")
            append(DateTimeFormatter.ofPattern("HH:mm").format(offer.fecha))
        }
        tvDescOferta.text = offer.descripcion
        tvPrecio.text = String.format(Locale.GERMAN, "%.2f €", offer.coste)
    }

    private fun abrirChat(offer: Offer) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("offer_object", offer)
        startActivity(intent)
        Log.i("Oferta", offer.toString())
    }

    /*private fun cargarOfertas(callback: (MutableList<Offer>) -> Unit) {
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
    }*/

    /*private fun actualizarRecyclerView() {
        cargarOfertas { offerList ->
            if (offerList.isNotEmpty()) {
                adapterListaOfertas.update(offerList)
            }
        }
    }*/

}