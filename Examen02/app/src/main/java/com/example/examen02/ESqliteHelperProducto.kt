package com.example.examen02

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ESqliteHelperProducto(
    private val dbHelper: ESqliteHelper)
{
    //-------------CRUD----------------------------
    fun crearProducto(nombre: String, categoria: String, precio: Double, stock: Int, tiendaId: Int): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresGuardar = ContentValues()

        // Agregar valores a ContentValues
        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("categoria", categoria)
        valoresGuardar.put("precio", precio)
        valoresGuardar.put("stock", stock)
        valoresGuardar.put("tiendaId", tiendaId)

        // Realizar la inserción
        val resultadoGuardar = baseDatosEscritura.insert(
            "PRODUCTO",  // nombre de la tabla
            null,         // columna que puede contener un valor null (por lo general, se usa `null` para la primera columna autoincremental)
            valoresGuardar // los valores a insertar
        )

        // Cerrar la base de datos
        baseDatosEscritura.close()

        // Verificar si la inserción fue exitosa
        return resultadoGuardar != -1L  // Si el resultado es -1, significa que hubo un error
    }


    // Función para obtener todos los productos de una tienda específica
    fun consultarProductosPorTienda(tiendaId: Int): List<BProducto> {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = """
        SELECT * FROM PRODUCTO
        WHERE tiendaId = ?
    """.trimIndent()

        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, arrayOf(tiendaId.toString()))
        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()

        val arregloRespuesta = arrayListOf<BProducto>()

        if (existeAlMenosUno) {
            do {
                val producto = BProducto(
                    resultadoConsultaLectura.getInt(0), // 0 = id
                    resultadoConsultaLectura.getString(1)!!, // 1 = nombre
                    resultadoConsultaLectura.getString(2)!!, // 2 = categoria
                    resultadoConsultaLectura.getDouble(3), // 3 = precio
                    resultadoConsultaLectura.getInt(4), // 4 = stock
                    resultadoConsultaLectura.getInt(5) // 5 = tiendaId (relación con Tienda)
                )
                arregloRespuesta.add(producto)
            } while (resultadoConsultaLectura.moveToNext())
        }

        Log.d("Productos", "Productos encontrados: $arregloRespuesta")

        resultadoConsultaLectura.close()
        return arregloRespuesta
    }

    fun eliminarProducto(id: Int): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())

        val resultadoEliminar = baseDatosEscritura.delete(
            "PRODUCTO", // Nombre de la tabla
            "id=?", // Condición WHERE para identificar el producto
            parametrosConsultaDelete // Parámetro con el ID del producto
        )

        baseDatosEscritura.close()
        return resultadoEliminar > 0 // Retorna true si se eliminó al menos un producto
    }

    fun actualizarProducto(id: Int, nombre: String, categoria: String, precio: Double, stock: Int): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresAActualizar = ContentValues()

        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("categoria", categoria)
        valoresAActualizar.put("precio", precio)
        valoresAActualizar.put("stock", stock)

        // Condición WHERE para actualizar el producto correcto
        val parametrosConsultaActualizar = arrayOf(id.toString())

        val resultadoActualizar = baseDatosEscritura.update(
            "PRODUCTO", // Nombre de la tabla
            valoresAActualizar, // Valores a actualizar
            "id=?", // Condición WHERE (id = ?)
            parametrosConsultaActualizar // Valores para reemplazar el ?
        )

        baseDatosEscritura.close()

        return resultadoActualizar > 0 // Devuelve true si se actualizó correctamente
    }



    fun consultarProductoPorId(id: Int): BProducto? {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = """
        SELECT * FROM PRODUCTO WHERE id = ?
    """.trimIndent()

        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        if (resultadoConsultaLectura.moveToFirst()) {
            val producto = BProducto(
                id = resultadoConsultaLectura.getInt(0), // 0 = id
                nombre = resultadoConsultaLectura.getString(1), // 1 = nombre
                categoria = resultadoConsultaLectura.getString(2), // 2 = categoria
                precio = resultadoConsultaLectura.getDouble(3), // 3 = precio
                stock = resultadoConsultaLectura.getInt(4), // 4 = stock
                tiendaId = resultadoConsultaLectura.getInt(5) // 5 = tiendaId
            )
            resultadoConsultaLectura.close()
            return producto
        } else {
            resultadoConsultaLectura.close()
            return null
        }
    }


}