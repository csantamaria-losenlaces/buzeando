package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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
    private lateinit var btnChat: Button

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
        cargarGaleriaImagenes()

        // DESCOMENTAR CUANDO HAYA LISTA DE CHATS
        /*if (offer.idUsr != Integer.parseInt(User.id_usr)) {
            btnChat.isEnabled = true
        }*/
        btnChat.isEnabled = true // QUITAR ESTO CUANDO HAYA LISTA DE CHATS

    }

    private fun initComponents() {
        adapterImageGallery = ImageGalleryAdapter(emptyList())
        rvImagenes = findViewById(R.id.rvImagenes)

        tvTituloOferta = findViewById(R.id.tvTituloOferta)
        tvFechaCreacion = findViewById(R.id.tvFechaCreacion)
        tvDescOferta = findViewById(R.id.tvDescOferta)
        tvPrecio = findViewById(R.id.tvPrecio)
        btnChat = findViewById(R.id.btnChat)
    }

    private fun initUI() {
        btnChat.setOnClickListener {
            abrirChat(offer)
        }
        rvImagenes.setHasFixedSize(true)
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

    private fun cargarGaleriaImagenes() {
        val listaImagenes = mutableListOf<Image>()
        if (offer.rutaImg1.isNotEmpty()) listaImagenes.add(Image(offer.rutaImg1))
        if (offer.rutaImg2.isNotEmpty()) listaImagenes.add(Image(offer.rutaImg2))
        if (offer.rutaImg3.isNotEmpty()) listaImagenes.add(Image(offer.rutaImg3))
        adapterImageGallery.update(listaImagenes)
    }

}