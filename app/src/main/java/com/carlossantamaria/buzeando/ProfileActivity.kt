package com.carlossantamaria.buzeando

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.carlossantamaria.buzeando.objects.User

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvNombreValor: TextView
    private lateinit var tvApellidosValor: TextView
    private lateinit var etDireccion: TextView
    private lateinit var tvCodPostalValor: TextView
    private lateinit var etMovil: TextView
    private lateinit var etCorreoElec: TextView
    private lateinit var etContrasena: TextView
    private lateinit var etRepetirContrasena: TextView
    private lateinit var btnGuardar: Button
    private val hayCambios:Array<Boolean> = arrayOf(false, false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        recuperarDatosUsuario()
        initUI()

    }

    private fun initComponents() {

        tvNombreValor = findViewById(R.id.tvNombreValor)
        tvApellidosValor = findViewById(R.id.tvApellidosValor)
        etDireccion = findViewById(R.id.etDireccion)
        tvCodPostalValor = findViewById(R.id.tvCodPostalValor)
        etMovil = findViewById(R.id.etMovil)
        etCorreoElec = findViewById(R.id.etCorreoElec)
        etContrasena = findViewById(R.id.etContrasena)
        etRepetirContrasena = findViewById(R.id.etRepetirContrasena)
        btnGuardar = findViewById(R.id.btnGuardar)

    }

    private fun initUI() {

        etDireccion.addTextChangedListener {
            hayCambios[0] = etDireccion.text.toString() != User.dir
            btnGuardar.isEnabled = hayCambios.contains(true)
        }
        etMovil.addTextChangedListener {
            hayCambios[1] = etMovil.text.toString() != User.movil
            btnGuardar.isEnabled = hayCambios.contains(true)
        }
        etCorreoElec.addTextChangedListener {
            hayCambios[2] = etCorreoElec.text.toString() != User.mail
            btnGuardar.isEnabled = hayCambios.contains(true)
        }
        etContrasena.addTextChangedListener {
            hayCambios[3] = (etContrasena.text.isNotEmpty() && etRepetirContrasena.text.isNotEmpty())
            hayCambios[4] = (etContrasena.text.isNotEmpty() && etRepetirContrasena.text.isNotEmpty())
            btnGuardar.isEnabled = hayCambios.contains(true)
        }
        etRepetirContrasena.addTextChangedListener {
            hayCambios[4] = (etRepetirContrasena.text.isNotEmpty() && etContrasena.text.isNotEmpty())
            hayCambios[3] = (etContrasena.text.isNotEmpty() && etRepetirContrasena.text.isNotEmpty())
            btnGuardar.isEnabled = hayCambios.contains(true)
        }
        btnGuardar.setOnClickListener {
            if (!claveCoincide()) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Los campos de contraseña no coinciden")
                    .setPositiveButton("Volver") { dialog, _ ->
                        dialog.dismiss()
                    }
                builder.create().show()
            } else {
                Log.i("Cambio de contraseña", "Las contraseñas coinciden")
                //actualizarUsuario()
            }
        }

    }

    private fun recuperarDatosUsuario() {

        tvNombreValor.text = User.nombre
        tvApellidosValor.text = User.apellidos
        etDireccion.text = User.dir
        tvCodPostalValor.text = User.cod_postal
        etMovil.text = User.movil
        etCorreoElec.text = User.mail

    }

    private fun claveCoincide(): Boolean {
        return (etContrasena.text.toString() == etRepetirContrasena.text.toString())
    }

}