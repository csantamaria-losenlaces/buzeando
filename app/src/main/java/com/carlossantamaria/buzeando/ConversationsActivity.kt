package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.carlossantamaria.buzeando.conversations.ConversationsListAdapter
import com.carlossantamaria.buzeando.objects.ConversationItem
import com.carlossantamaria.buzeando.objects.User

class ConversationsActivity : AppCompatActivity() {

    private lateinit var adapterListaConversaciones: ConversationsListAdapter
    private lateinit var rvConversaciones: RecyclerView
    private lateinit var btnMapa: Button
    private lateinit var btnListaOfertas: Button
    private lateinit var btnCuenta: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_conversations)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityConversations)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        initComponents()
        initUI()

        crearConversaciones()

    }

    private fun initComponents() {
        adapterListaConversaciones = ConversationsListAdapter(emptyList()) { abrirConversacion(it.idReceptor) }
        rvConversaciones = findViewById(R.id.rvConversaciones)
        btnMapa = findViewById(R.id.btnMapa)
        btnListaOfertas = findViewById(R.id.btnListaOfertas)
        btnCuenta = findViewById(R.id.btnCuenta)
    }

    private fun initUI() {
        rvConversaciones.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvConversaciones.adapter = adapterListaConversaciones
        btnMapa.setOnClickListener { abrirMapa() }
        btnListaOfertas.setOnClickListener { abrirListaOfertas() }
        btnCuenta.setOnClickListener { abrirCuenta() }
    }

    private fun abrirMapa() {
        val intent = Intent(this, OfferMapActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun abrirListaOfertas() {
        val intent = Intent(this, OfferListActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun abrirCuenta() {
        val intent = Intent(this, AccountActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun crearConversaciones() {
        cargarListaConversaciones { listaConversaciones ->
            if (listaConversaciones.isNotEmpty()) {
                adapterListaConversaciones.update(listaConversaciones)
            }
        }
    }

    private fun cargarListaConversaciones(callback: (MutableList<ConversationItem>) -> Unit) {
        val url = "http://77.90.13.129/android/getconversationslist.php?id_usr=${User.id_usr}"
        val requestQueue = Volley.newRequestQueue(this)
        val conversationList = mutableListOf<ConversationItem>()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val conversacionesArray = response.getJSONArray("conversaciones")
                for (i in 0 until conversacionesArray.length()) {
                    val conversation = conversacionesArray.getJSONObject(i)
                    conversationList.add(
                        ConversationItem(
                            conversation.getInt("id_conver"),
                            conversation.getInt("id_usr"),
                            conversation.getString("nombre"),
                            conversation.getString("apellidos"),
                            conversation.getString("texto"),
                            conversation.getString("fecha")
                        )
                    )
                }
                callback(conversationList)
            },
            { error ->
                Toast.makeText(this, "No se han podido cargar las conversaciones", Toast.LENGTH_SHORT).show()
                Log.i("Error de consulta", error.message.toString())
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    private fun abrirConversacion(idRecipient: Int) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("id_recipient", idRecipient)
        startActivity(intent)
    }

}