package com.carlossantamaria.buzeando

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
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
import com.carlossantamaria.buzeando.objects.User
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity(), ChatWebSocket.ChatWebSocketListener {

    private lateinit var tvTitulo: TextView
    private lateinit var rvConversacion: RecyclerView
    private lateinit var etMensaje: EditText
    private lateinit var btnEnviar: Button

    private var idRecipient: Int = -1
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityChat)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initUI()

        cargarDestinatario {
            tvTitulo.text = buildString {
                append("Chat con ")
                append(it[0])
                append(" ")
                append(it[1])
            }
        }

        rvConversacion.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(
                applicationContext, LinearLayoutManager.VERTICAL, false
            )
        }

        CreateOrLoadConversation(this).createOrLoadConversation(
            Integer.parseInt(User.id_usr),
            idRecipient
        ) { idRecibido ->
            idConversacion = idRecibido
            LoadMessages(this).loadMessages(idConversacion) { mensajesRecibidos ->
                listaMensajes = mensajesRecibidos
                chatAdapter.setData(listaMensajes)
                rvConversacion.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        this.recreate()
    }

    override fun onOpen() {}

    override fun onMessage(text: String) {
        runOnUiThread {
            addMessageToChat(text, "RECEIVE")
        }
    }

    override fun onClosing(code: Int, reason: String) {}

    override fun onFailure(t: Throwable) {
        runOnUiThread {
            Toast.makeText(this, "Error al acceder al chat", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun addMessageToChat(message: String, userType: String) {
        val currTime = getCurrentTime()
        val chatModel = ChatModel(userType, message, currTime, Integer.parseInt(User.id_usr))

        chatAdapter.addMessage(chatModel)
        rvConversacion.scrollToPosition(chatAdapter.itemCount - 1)
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

        idRecipient = intent.getIntExtra("id_recipient", -1)
    }

    private fun initUI() {
        etMensaje.addTextChangedListener {
            btnEnviar.isEnabled = etMensaje.text.toString().isNotEmpty()
        }

        btnEnviar.setOnClickListener {
            val message = etMensaje.text.toString()
            if (message.isNotEmpty()) {
                val currTime =
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        .toString()
                etMensaje.setText("")
                it.ocultarTeclado()
                SendMessage(this).sendMessage(idConversacion, message, User.id_usr, currTime) {}
                chatWebSocket.send(message)
                addMessageToChat(message, "SEND")
                rvConversacion.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }
    }

    private fun cargarDestinatario(callback: (List<String>) -> Unit) {
        val url = "http://77.90.13.129/android/chatrecipient.php?id_usr=${idRecipient}"
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val nombre = response.getString("nombre")
            val apellidos = response.getString("apellidos")
            callback(listOf(nombre, apellidos))
        }, {
            Toast.makeText(
                this,
                "Los datos son incorrectos o la cuenta no existe",
                Toast.LENGTH_SHORT
            )
                .show()
        })
        requestQueue.add(jsonObjectRequest)
    }

    private fun View.ocultarTeclado() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}