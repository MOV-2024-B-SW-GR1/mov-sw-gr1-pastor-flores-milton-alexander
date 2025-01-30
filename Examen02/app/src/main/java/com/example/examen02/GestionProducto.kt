package com.example.examen02

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class CrearProducto : AppCompatActivity() {

    private var idProducto: Int = -1
    private var idTienda: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener referencias a los campos de entrada
        val etNombre = findViewById<EditText>(R.id.etNombreProducto)
        val etCategoria = findViewById<EditText>(R.id.etCategoriaProducto)
        val etPrecio = findViewById<EditText>(R.id.etPrecioProducto)
        val etStock = findViewById<EditText>(R.id.etStockProducto)
        val botonGuardarProducto = findViewById<Button>(R.id.btnCrearProducto)

        // Recuperar el ID de la tienda y el ID del producto (si se está editando)
        idTienda = intent.getIntExtra("ID_TIENDA", -1)
        idProducto = intent.getIntExtra("ID_PRODUCTO", -1)

        // Si ID_PRODUCTO es válido, estamos editando un producto existente
        if (idProducto != -1) {
            botonGuardarProducto.text = "Actualizar Producto"

            // Consultar la base de datos para obtener los datos del producto
            val producto = EBaseDeDatos.tablaProducto?.consultarProductoPorId(idProducto)
            producto?.let {
                etNombre.setText(it.nombre)
                etCategoria.setText(it.categoria)
                etPrecio.setText(it.precio.toString())
                etStock.setText(it.stock.toString())
            }
        } else {
            botonGuardarProducto.text = "Crear Producto"
        }

        // Acción de crear o actualizar producto
        botonGuardarProducto.setOnClickListener {
            val nombre = etNombre.text.toString()
            val categoria = etCategoria.text.toString()
            val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val stock = etStock.text.toString().toIntOrNull() ?: 0

            if (nombre.isNotEmpty() && categoria.isNotEmpty() && precio > 0 && stock > 0) {
                val respuesta = if (idProducto == -1) {
                    // Crear producto nuevo
                    EBaseDeDatos.tablaProducto?.crearProducto(nombre, categoria, precio, stock, idTienda)
                } else {
                    // Actualizar producto existente
                    EBaseDeDatos.tablaProducto?.actualizarProducto(idProducto, nombre, categoria, precio, stock)
                }

                if (respuesta == true) {
                    mostrarSnackbar(if (idProducto == -1) "Producto creado exitosamente" else "Producto actualizado exitosamente")


                    // Redirigir a otra actividad (por ejemplo, MainActivity)
                    val intent = Intent(this, Productos::class.java)
                    intent.putExtra("ID_TIENDA", idTienda) // Pasa el ID de la tienda a la actividad de productos
                    startActivity(intent)
                    finish() // Cierra la actividad actual

                } else {
                    mostrarSnackbar("Error al guardar el producto")
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
