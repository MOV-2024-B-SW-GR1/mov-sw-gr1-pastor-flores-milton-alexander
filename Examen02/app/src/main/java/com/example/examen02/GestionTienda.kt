package com.example.examen02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class GestionTienda : AppCompatActivity() {

    private var idTienda: Int = -1
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_tienda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener referencias a los campos de entrada
        val etNombre = findViewById<EditText>(R.id.etNombreTienda)
        val etUbicacion = findViewById<EditText>(R.id.etUbicacionTienda)
        val switchFranquicia = findViewById<Switch>(R.id.switchFranquiciaTienda)
        val etFechaCreacion = findViewById<EditText>(R.id.etFechaCreacionTienda)
        val botonGuardarTienda = findViewById<Button>(R.id.btnCrearTienda)

        // Recuperar el ID de la tienda (si se está editando)
        idTienda = intent.getIntExtra("ID_TIENDA", -1)

        // Si ID_TIENDA es válido, estamos editando una tienda existente
        if (idTienda != -1) {
            botonGuardarTienda.text = "Actualizar Tienda"

            // Consultar la base de datos para obtener los datos de la tienda
            val tienda = EBaseDeDatos.tablaTienda?.consultarTiendaPorId(idTienda)
            tienda?.let {
                etNombre.setText(it.nombre)
                etUbicacion.setText(it.ubicacion)
                switchFranquicia.isChecked = it.esFranquicia
                etFechaCreacion.setText(formatoFecha.format(it.fechaDeCreacion))
            }
        } else {
            botonGuardarTienda.text = "Crear Tienda"
        }

        // Acción de crear o actualizar tienda
        botonGuardarTienda.setOnClickListener {
            val nombre = etNombre.text.toString()
            val ubicacion = etUbicacion.text.toString()
            val esFranquicia = switchFranquicia.isChecked
            val fechaDeCreacionStr = etFechaCreacion.text.toString()

            if (nombre.isNotEmpty() && ubicacion.isNotEmpty() && fechaDeCreacionStr.isNotEmpty()) {
                val fecha = try {
                    formatoFecha.parse(fechaDeCreacionStr)
                } catch (e: ParseException) {
                    null
                }

                if (fecha != null) {
                    val respuesta = if (idTienda == -1) {
                        // Crear nueva tienda
                        EBaseDeDatos.tablaTienda?.crearTienda(nombre, ubicacion, esFranquicia, fecha)
                    } else {
                        // Actualizar tienda existente
                        EBaseDeDatos.tablaTienda?.actualizarTienda(idTienda, nombre, ubicacion, esFranquicia, fecha)
                    }

                    if (respuesta == true) {
                        mostrarSnackbar(if (idTienda == -1) "Tienda creada exitosamente" else "Tienda actualizada exitosamente")

                        // Regresar a la lista de tiendas
                        val intent = Intent(this, Tiendas::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        mostrarSnackbar("Error al guardar la tienda")
                    }
                } else {
                    mostrarSnackbar("Fecha de creación no válida")
                }
            } else {
                mostrarSnackbar("Por favor, completa todos los campos correctamente")
            }
        }
    }

    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.main), texto, Snackbar.LENGTH_LONG).show()
    }
}
