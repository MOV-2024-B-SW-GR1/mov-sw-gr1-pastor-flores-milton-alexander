package com.example.examen02

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class Tiendas : AppCompatActivity() {

    // Lista de tiendas
    var tiendas = mutableListOf<BTienda>()

    // ListView declarado
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tiendas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonIrCrearTienda = findViewById<Button>(R.id.btnIrCrearTienda)
        botonIrCrearTienda.setOnClickListener {
            irActividad(GestionTienda::class.java)
        }

        // Obtener el ListView
        listView = findViewById<ListView>(R.id.lv_lista_tiendas)

        // Cargar y mostrar la lista de tiendas
        actualizarListaTiendas()

        // Registrar el ListView para el menú contextual
        registerForContextMenu(listView)
    }

    var posicionItemSeleccionado = -1 // Variable global para la posición del item seleccionado


    // Menú contextual (al mantener presionado un item)
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        // Llenamos las opciones del menú
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        // Obtener el ID del item seleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    // Manejar las acciones del menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_ver_producto -> {

                // Redirigir a otra actividad (por ejemplo, MainActivity)
                val tiendaSeleccionada = tiendas[posicionItemSeleccionado] // Obtener la tienda seleccionada
                val intent = Intent(this, Productos::class.java)
                intent.putExtra("ID_TIENDA", tiendaSeleccionada.id) // Pasa el ID de la tienda a la actividad de productos
                startActivity(intent)
                return true
            }

            R.id.mi_editar -> {
                val tiendaSeleccionada = tiendas[posicionItemSeleccionado] // Obtener la tienda seleccionada
                val intent = Intent(this, GestionTienda::class.java)
                intent.putExtra("ID_TIENDA", tiendaSeleccionada.id)  // Enviar ID de la tienda a editar
                startActivity(intent)
                finish()
                return true
            }

            R.id.mi_eliminar -> {
                abrirDialogo()
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    // Función para mostrar un Snackbar
    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    // Función para abrir un diálogo de confirmación
    fun abrirDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Deseas Eliminar esta tienda?")

        builder.setPositiveButton("Aceptar") { dialog, which ->
            if (posicionItemSeleccionado != -1) {
                val tiendaSeleccionada = tiendas[posicionItemSeleccionado] // Obtiene la tienda de la lista
                val exito = EBaseDeDatos.tablaTienda?.eliminarTienda(tiendaSeleccionada.id)
                if (exito == true) {
                    mostrarSnackbar("Tienda eliminada exitosamente")
                    actualizarListaTiendas() // Refresca la lista en el ListView
                } else {
                    mostrarSnackbar("Error al eliminar la tienda")
                }
            }
        }

        builder.setNegativeButton("Cancelar", null)

        val dialogo = builder.create()
        dialogo.show()


    }

    fun actualizarListaTiendas() {
        tiendas.clear() // Limpia la lista antes de volver a llenarla
        tiendas.addAll(EBaseDeDatos.tablaTienda?.consultarTodasLasTiendas() ?: listOf())

        if (tiendas.isNotEmpty()) {
            val adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                tiendas
            )
            listView.adapter = adaptador
            adaptador.notifyDataSetChanged()
        } else {
            listView.adapter = null // Si la lista está vacía, limpiamos el adapter
        }
    }


    // Función para navegar a la actividad de CrearTienda
    fun irActividad(clase: Class<*>) {
        startActivity(Intent(this, clase))
    }



}