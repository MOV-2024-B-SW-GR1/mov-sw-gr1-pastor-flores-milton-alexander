package org.example

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class Producto(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val stock: Int,
    val tiendaId: Int //relación uno a muchos
)

data class Tienda(
        val id: Int,
        val nombre: String,
        val ubicacion: String,
        val esFranquicia: Boolean,
        val fechaDeCreacion: Date
)

const val PATH_TIENDAS = "data/tiendas.txt" // Archivo para datos de tiendas
const val PATH_PRODUCTOS = "data/productos.txt"

fun main() {
    while (true) {
        println("\n--- MENU CRUD ---")
        println("1. Crear Tienda")
        println("2. Leer Tiendas")
        println("3. Actualizar Tienda")
        println("4. Eliminar Tienda")
        println("5. Crear Producto")
        println("6. Leer Productos")
        println("7. Actualizar Producto")
        println("8. Eliminar Producto")
        println("9. Salir")

        when (readInput("Ingresa una opción: ")) {
            "1" -> crearTienda()
            "2" -> leerTiendas()
            "3" -> actualizarTienda()
            "4" -> eliminarTienda()
            "5" -> crearProducto()
            "6" -> leerProductos()
            "7" -> actualizarProducto()
            "8" -> eliminarProducto()
            "9" -> {
                println("¡Hasta pronto!")
                return
            }
            else -> println("Opción no válida, intenta de nuevo.")
        }
    }
}

// ---------------------- Función para Entrada de Datos ----------------------
fun readInput(mensaje: String): String {
    print(mensaje)
    return Scanner(System.`in`).nextLine().trim()
}

// ---------------------- Funciones Auxiliares para Manejo de Archivos ----------------------
fun readFileLines(path: String): MutableList<String> =
    File(path).takeIf { it.exists() }?.readLines()?.toMutableList() ?: mutableListOf()

fun writeFileLines(path: String, lines: List<String>) { // Escribe líneas en un archivo, sobrescribiendo el contenido
    File(path).writeText(lines.joinToString("\n"))
}

// ---------------------- CRUD Tiendas ----------------------
fun crearTienda() {
    println("\n--- Ingrese los datos de la nueva tienda ---")
    val nombre = readInput("Nombre de la tienda: ")
    val ubicacion = readInput("Ubicación: ")
    val esFranquicia = readInput("¿Es franquicia? (true/false): ").toBoolean()

    val fechaDeCreacion = obtenerFechaCreacion()

    val tiendaId = readFileLines(PATH_TIENDAS).size + 1 // Auto-incrementa el ID de tienda
    val tienda = Tienda(tiendaId, nombre, ubicacion, esFranquicia, fechaDeCreacion)

    File(PATH_TIENDAS).appendText("${tienda.id},${tienda.nombre},${tienda.ubicacion},${tienda.esFranquicia},${SimpleDateFormat("yyyy-MM-dd").format(tienda.fechaDeCreacion)}\n")
    println("Tienda creada con éxito.")
}

fun leerTiendas() {
    println("\n--- Lista de Tiendas ---")
    readFileLines(PATH_TIENDAS).forEach {
        val datos = it.split(",")
        println("ID: ${datos[0]}, Nombre: ${datos[1]}, Ubicación: ${datos[2]}, Franquicia: ${datos[3]}, Fecha: ${datos[4]}")
    }
}

fun actualizarTienda() {
    val id = readInput("Introduce el ID de la tienda a actualizar: ").toIntOrNull() ?: return

    val tiendas = readFileLines(PATH_TIENDAS)
    val tienda = tiendas.find { it.startsWith("$id,") } //buscamos la tienda

    if (tienda != null) { //si existe la tienda
        val nombre = readInput("Nombre de la tienda: ")
        val ubicacion = readInput("Ubicación: ")
        val esFranquicia = readInput("¿Es franquicia? (true/false): ").toBoolean()
        val fechaActualizada = obtenerFechaCreacion()

        val tiendaActualizada = "$id,$nombre,$ubicacion,$esFranquicia,${SimpleDateFormat("yyyy-MM-dd").format(fechaActualizada)}"
        val index = tiendas.indexOf(tienda)
        tiendas[index] = tiendaActualizada

        writeFileLines(PATH_TIENDAS, tiendas)
        println("Tienda actualizada con éxito.")
    } else {
        println("No se encontró la tienda con el ID $id.")
    }
}

fun eliminarTienda() {
    val id = readInput("Introduce el ID de la tienda a eliminar: ").toIntOrNull() ?: return

    val tiendas = readFileLines(PATH_TIENDAS)
    val tienda = tiendas.find { it.startsWith("$id,") }

    if (tienda != null) {
        val confirmacion = readInput("¿Estás seguro de eliminar la tienda? (si/no): ")
        if (confirmacion.equals("si", ignoreCase = true)) {
            val tiendasFiltradas = tiendas.filterNot { it.startsWith("$id,") }
            writeFileLines(PATH_TIENDAS, tiendasFiltradas)
            println("Tienda eliminada con éxito.")
        }
    } else {
        println("No se encontró la tienda con el ID $id.")
    }
}

// ---------------------- Fecha de Creación ----------------------
fun obtenerFechaCreacion(): Date {
    print("Fecha de creación (yyyy-MM-dd): ")
    val fechaInput = readInput("")
    return try {
        SimpleDateFormat("yyyy-MM-dd").apply { isLenient = false }.parse(fechaInput)
    } catch (e: Exception) {
        println("Formato inválido, se establecerá la fecha actual.")
        Date()
    }
}

// ---------------------- CRUD Productos ----------------------
fun crearProducto() {
    leerTiendas()

    val idTienda = readInput("Ingresa el ID de la tienda: ").toIntOrNull() ?: return
    println("\n--- Ingrese los datos del nuevo producto ---")
    val nombre = readInput("Nombre del producto: ")
    val categoria = readInput("Categoría: ")
    val precio = readInput("Precio: ").toDoubleOrNull() ?: 0.0
    val stock = readInput("Stock: ").toIntOrNull() ?: 0

    val productoId = readFileLines(PATH_PRODUCTOS).size + 1
    val producto = Producto(productoId, nombre, categoria, precio, stock, idTienda)

    File(PATH_PRODUCTOS).appendText("${producto.id},${producto.nombre},${producto.categoria},${producto.precio},${producto.stock},${producto.tiendaId}\n")
    println("Producto creado con éxito.")
}

fun leerProductos() {
    println("\n--- Lista de Productos ---")
    readFileLines(PATH_PRODUCTOS).forEach {
        val datos = it.split(",")
        println("ID: ${datos[0]}, Nombre: ${datos[1]}, Categoría: ${datos[2]}, Precio: ${datos[3]}, Stock: ${datos[4]}, ID Tienda: ${datos[5]}")
    }
}

fun actualizarProducto() {
    val id = readInput("Introduce el ID del producto a actualizar: ").toIntOrNull() ?: return
    val productos = readFileLines(PATH_PRODUCTOS)
    val producto = productos.find { it.startsWith("$id,") }

    if (producto != null) {
        val nombre = readInput("Nombre del producto: ")
        val categoria = readInput("Categoría: ")
        val precio = readInput("Precio: ").toDoubleOrNull() ?: 0.0
        val stock = readInput("Stock: ").toIntOrNull() ?: 0
        leerTiendas()

        val idTienda = readInput("Actualiza el ID de la tienda: ").toIntOrNull() ?: return
        val productoActualizado = "$id,$nombre,$categoria,$precio,$stock,$idTienda"

        val index = productos.indexOf(producto)
        productos[index] = productoActualizado

        writeFileLines(PATH_PRODUCTOS, productos)
        println("Producto actualizado con éxito.")
    } else {
        println("No se encontró el producto con el ID $id.")
    }
}

fun eliminarProducto() {
    val idEliminar = readInput("Introduce el ID del producto a eliminar: ").toIntOrNull() ?: return
    val productos = readFileLines(PATH_PRODUCTOS)
    val producto = productos.find { it.startsWith("$idEliminar,") }

    if (producto != null) {
        val confirmacion = readInput("¿Estás seguro de eliminar el producto? (si/no): ")
        if (confirmacion.equals("si", ignoreCase = true)) {
            val productosFiltrados = productos.filterNot { it.startsWith("$idEliminar,") }
            writeFileLines(PATH_PRODUCTOS, productosFiltrados)
            println("Producto eliminado con éxito.")
        }
    } else {
        println("No se encontró el producto con el ID $idEliminar.")
    }
}