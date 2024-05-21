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
import com.carlossantamaria.buzeando.objects.Offer
import java.time.format.DateTimeFormatter
import java.util.Locale

class OfferDetailsActivity : AppCompatActivity() {

    private lateinit var offer: Offer
    private lateinit var tvTituloOferta: TextView
    private lateinit var tvFechaCreacion: TextView
    private lateinit var tvDescOferta: TextView
    private lateinit var tvPrecio: TextView
    private lateinit var btnPerfil: Button
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

        // DESCOMENTAR CUANDO HAYA LISTA DE CHATS
        /*if (offer.idUsr != Integer.parseInt(User.id_usr)) {
            btnChat.isEnabled = true
        }*/
        btnChat.isEnabled = true // QUITAR ESTO CUANDO HAYA LISTA DE CHATS

    }

    private fun initComponents() {
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
}