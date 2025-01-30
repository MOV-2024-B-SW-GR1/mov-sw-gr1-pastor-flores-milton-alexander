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

class Productos : AppCompatActivity() {
    // Lista de tiendas
    var productos = mutableListOf<BProducto>()
    var idTienda: Int = 0

    // ListView declarado
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_productos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener el ListView
        listView = findViewById<ListView>(R.id.lv_productos)

        // Dentro de la actividad Productos:
        idTienda = intent.getIntExtra("ID_TIENDA", -1) // Recupera el ID de la tienda
        if (idTienda != -1) {
            // Si el ID es válido (no es -1), realiza la consulta o cualquier acción con este ID
            actualizarListaProductos(idTienda)
        }

        val botonCrearProducto = findViewById<Button>(R.id.btnIrCrearProducto)
        botonCrearProducto.setOnClickListener {
            val intent = Intent(this, GestionProducto::class.java)
            intent.putExtra("ID_TIENDA", idTienda)  // Enviamos el ID de la tienda
            startActivity(intent)
        }

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
            R.id.mi_editar -> {
                val ProductoSeleccionado = productos[posicionItemSeleccionado] // Obtener la tienda seleccionada
                val intent = Intent(this, GestionProducto::class.java)
                intent.putExtra("ID_PRODUCTO", ProductoSeleccionado.id)  // Enviar ID de la tienda a editar
                intent.putExtra("ID_TIENDA", idTienda)  // Enviamos el ID de la tienda
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

    // Función para abrir un diálogo de confirmación
    fun abrirDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Deseas Eliminar este Producto?")

        builder.setPositiveButton("Aceptar") { dialog, which ->
            if (posicionItemSeleccionado != -1) {
                val productoSeleccionado = productos[posicionItemSeleccionado] // Obtiene la tienda de la lista
                val exito = EBaseDeDatos.tablaProducto?.eliminarProducto(productoSeleccionado.id)
                if (exito == true) {
                    mostrarSnackbar("Producto eliminado exitosamente")
                    actualizarListaProductos(idTienda) // Refresca la lista en el ListView
                } else {
                    mostrarSnackbar("Error al eliminar el producto")
                }
            }
        }

        builder.setNegativeButton("Cancelar", null)

        val dialogo = builder.create()
        dialogo.show()
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

    fun irActividad(clase:Class<*>){
        startActivity(Intent(this, clase))
    }

    fun actualizarListaProductos(id_Tienda: Int) {
        productos.clear() // Limpia la lista antes de volver a llenarla
        productos.addAll(EBaseDeDatos.tablaProducto?.consultarProductosPorTienda(id_Tienda) ?: listOf()) // Consulta productos por ID de tienda

        if (productos.isNotEmpty()) {
            val adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                productos
            )
            listView.adapter = adaptador
            adaptador.notifyDataSetChanged()
        } else {
            listView.adapter = null // Si la lista está vacía, limpiamos el adapter
        }
    }

}