package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.carlossantamaria.buzeando.chat.ChatAdapterViewHolder
import com.carlossantamaria.buzeando.chat.ChatWebSocket
import com.carlossantamaria.buzeando.chat.CreateOrLoadConversation
import com.carlossantamaria.buzeando.chat.LoadMessages
import com.carlossantamaria.buzeando.chat.SendMessage
import com.carlossantamaria.buzeando.databinding.ActivityChatBinding
import com.carlossantamaria.buzeando.objects.ChatModel
import com.carlossantamaria.buzeando.objects.Offer
import com.carlossantamaria.buzeando.objects.User
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity(), ChatWebSocket.ChatWebSocketListener {

    private lateinit var offer: Offer
    private lateinit var tvTitulo: TextView
    private lateinit var rvConversacion: RecyclerView
    private lateinit var etMensaje: EditText
    private lateinit var btnEnviar: Button

    private var idConversacion: Int = -1
    private var listaMensajes = mutableListOf<ChatModel>()

    private lateinit var chatWebSocket: ChatWebSocket
    private lateinit var chatAdapter: ChatAdapterViewHolder

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initUI()

        cargarDatosOferta()
        cargarDatosDestinatario {
            tvTitulo.text = buildString {
                append("Chat con ")
                append(it[0])
                append(" ")
                append(it[1])
            }
        }

        binding.rvConversacion.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(
                applicationContext, LinearLayoutManager.VERTICAL, false
            )
        }

        CreateOrLoadConversation(this).createOrLoadConversation(Integer.parseInt(User.id_usr), offer.idUsr) { idRecibido ->
            idConversacion = idRecibido
            LoadMessages(this).loadMessages(idConversacion) { mensajesRecibidos ->
                listaMensajes = mensajesRecibidos
                chatAdapter.setData(listaMensajes)
            }
        }
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        this.recreate()
    }

    override fun onOpen() {
        runOnUiThread {
            Toast.makeText(this, "Conectado al chat", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMessage(text: String) {
        runOnUiThread {
            addMessageToChat(text)
        }
    }

    override fun onClosing(code: Int, reason: String) {
        runOnUiThread {
            Toast.makeText(this, "Chat cerrado: $reason", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(t: Throwable) {
        runOnUiThread {
            Toast.makeText(this, "Error en el chat: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun addMessageToChat(message: String) {
        val chatModel = ChatModel("SEND", message, getCurrentTime(), Integer.parseInt(User.id_usr))
        chatAdapter.addMessage(chatModel)
        binding.rvConversacion.scrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        chatWebSocket.close()
    }

    private fun initComponents() {
        tvTitulo = findViewById(R.id.tvTitulo)
        rvConversacion = findViewById(R.id.rvConversacion)
        etMensaje = findViewById(R.id.etMensaje)
        btnEnviar = findViewById(R.id.btnEnviar)

        chatWebSocket = ChatWebSocket(this)
        chatWebSocket.connect()

        chatAdapter = ChatAdapterViewHolder()
    }

    private fun initUI() {
        btnEnviar.setOnClickListener {
            val message = etMensaje.text.toString()
            val currTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
            chatWebSocket.send(message)
            addMessageToChat(message) // Añadir mensaje a la UI localmente
            SendMessage(this).sendMessage(idConversacion, message, User.id_usr, currTime) { callback ->
                Log.i("SendMessage.sendMessage()", callback.toString())
            }
        }
    }

    private fun cargarDatosOferta() {
        offer = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("offer_object", Offer::class.java)!!
        } else {
            (intent.getParcelableExtra("offer_object") as? Offer)!!
        }
    }

    private fun cargarDatosDestinatario(callback: (List<String>) -> Unit) {
        Log.i("Valor de oferta al entrar a cargarDatosDestinatario", "${offer.idUsr}")
        val url = "http://77.90.13.129/android/chatrecipient.php?id_usr=${offer.idUsr}"
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val nombre = response.getString("nombre")
            val apellidos = response.getString("apellidos")
            callback(listOf(nombre, apellidos))
        }, {
            Toast.makeText(this, "Los datos son incorrectos o la cuenta no existe", Toast.LENGTH_SHORT)
                .show()
        })
        requestQueue.add(jsonObjectRequest)
    }

}