package com.carlossantamaria.buzeando.offerlist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.MapViewActivity
import com.carlossantamaria.buzeando.R
import com.carlossantamaria.buzeando.objects.Offer

class OfferListActivity : AppCompatActivity() {

    private lateinit var rvListaOfertas: RecyclerView
    private lateinit var adapterListaOfertas: OfferListAdapter
    private val listaOfertas = listOf(
        Offer("Electrónica", "Smartphone Samsung Galaxy S21", "¡El último modelo de Samsung con cámara de alta resolución!", 999.99, "imagen1.jpg"),
        Offer("Moda", "Camisa de manga larga para hombre", "Camisa de algodón con diseño elegante y cómodo.", 49.99, "imagen2.jpg"),
        Offer("Hogar", "Set de ollas antiadherentes", "Set de 10 ollas de diferentes tamaños con recubrimiento antiadherente.", 129.99, "imagen3.jpg"),
        Offer("Electrodomésticos", "Licuadora de alta potencia", "Licuadora de 1000W con múltiples velocidades y vaso de vidrio resistente.", 79.99, "imagen4.jpg"),
        Offer("Moda", "Vestido de noche elegante", "Vestido largo con diseño de encaje perfecto para ocasiones especiales.", 89.99, "imagen5.jpg"),
        Offer("Deportes", "Raqueta de tenis Wilson Pro Staff", "Raqueta de tenis profesional con tecnología avanzada de control.", 199.99, "imagen6.jpg"),
        Offer("Hogar", "Juego de sábanas de algodón", "Juego de sábanas suaves y transpirables en tamaño king.", 59.99, "imagen7.jpg"),
        Offer("Electrónica", "Auriculares inalámbricos Sony", "Auriculares con cancelación de ruido y batería de larga duración.", 149.99, "imagen8.jpg"),
        Offer("Moda", "Zapatos deportivos Nike Air Max", "Zapatillas deportivas con tecnología de amortiguación Air Max para mayor comodidad.", 129.99, "imagen9.jpg"),
        Offer("Hogar", "Robot aspirador inteligente", "Robot aspirador con mapeo inteligente y función de limpieza programable.", 299.99, "imagen10.jpg"),
        Offer("Deportes", "Bicicleta de montaña Cannondale", "Bicicleta de montaña ligera y resistente con suspensión delantera.", 799.99, "imagen11.jpg"),
        Offer("Electrodomésticos", "Horno eléctrico multifunción", "Horno eléctrico con funciones de horneado, asado y tostado.", 179.99, "imagen12.jpg"),
        Offer("Moda", "Bolso de cuero genuino", "Bolso de cuero con múltiples compartimentos y correa ajustable.", 99.99, "imagen13.jpg"),
        Offer("Hogar", "Set de cuchillos de cocina", "Set de cuchillos de acero inoxidable con bloque de madera para almacenamiento.", 69.99, "imagen14.jpg"),
        Offer("Electrónica", "Televisor LED 4K Samsung", "Televisor LED con resolución 4K y tecnología de mejora de imagen.", 799.99, "imagen15.jpg")
    )
    private lateinit var btnMapa: Button
    private lateinit var btnListaOfertas: Button
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
    }

    private fun initComponents() {
        adapterListaOfertas = OfferListAdapter(listaOfertas)
        rvListaOfertas = findViewById(R.id.rvListaOfertas)

        btnMapa = findViewById(R.id.btnMapa)
        btnListaOfertas = findViewById(R.id.btnListaOfertas)
        btnPerfil = findViewById(R.id.btnPerfil)
    }

    private fun initUI() {
        rvListaOfertas.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvListaOfertas.adapter = adapterListaOfertas

        btnMapa.setOnClickListener { abrirMapa() }
        btnListaOfertas.setOnClickListener { abrirListaOfertas() }
        btnPerfil.setOnClickListener { abrirPerfil() }
    }

    private fun abrirMapa() {
        val intent = Intent(this, MapViewActivity::class.java)
        startActivity(intent)
    }

    private fun abrirListaOfertas() {
        val intent = Intent(this, OfferListActivity::class.java)
        startActivity(intent)
    }

    private fun abrirPerfil() {
        /*val intent = Intent(this, OfferListActivity::class.java)
        startActivity(intent)*/
        Toast.makeText(this, "Funcionalidad todavía no implementada", Toast.LENGTH_SHORT).show()
    }

}