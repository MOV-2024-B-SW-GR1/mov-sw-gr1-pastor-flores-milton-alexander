package com.example.examen02

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ESqliteHelperTienda(
    private val dbHelper: ESqliteHelper)
{

    //-------------CRUD----------------------------
    fun crearTienda(nombre: String, ubicacion: String, esFranquicia: Boolean, fechaDeCreacion: Date): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresGuardar = ContentValues()

        // Convertir la fecha a formato String (ISO)
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaDeCreacion)

        // Agregar los valores a ContentValues
        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("ubicacion", ubicacion)
        valoresGuardar.put("esFranquicia", if (esFranquicia) 1 else 0) // Guardamos 1 para franquicia y 0 para no
        valoresGuardar.put("fechaDeCreacion", fechaFormateada)

        // Realizar la inserción
        val resultadoGuardar = baseDatosEscritura.insert(
            "TIENDA",  // Nombre de la tabla
            null,       // Columna que puede contener un valor null (usualmente para la columna autoincrementable)
            valoresGuardar // Valores a insertar
        )

        // Cerrar la base de datos
        baseDatosEscritura.close()

        // Verificar si la inserción fue exitosa
        return resultadoGuardar != -1L  // Si el resultado es -1, significa que hubo un error
    }

    fun eliminarTienda(id: Int): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())

        val resultadoEliminar = baseDatosEscritura.delete(
            "TIENDA", // Nombre de la tabla
            "id=?", // Condición WHERE
            parametrosConsultaDelete // Parámetros de la consulta
        )

        baseDatosEscritura.close()
        return resultadoEliminar > 0 // Retorna true si se eliminó al menos una fila
    }


    // Función para obtener todas las tiendas
    fun consultarTodasLasTiendas(): List<BTienda> {

        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = """
        SELECT * FROM TIENDA
    """.trimIndent()
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()

        val arregloRespuesta = arrayListOf<BTienda>()

        if (existeAlMenosUno) {
            do {
                val tienda = BTienda(
                    resultadoConsultaLectura.getInt(0), // 0 = id
                    resultadoConsultaLectura.getString(1)!!, // 1 = nombre
                    resultadoConsultaLectura.getString(2)!!, // 2 = ubicacion
                    resultadoConsultaLectura.getInt(3) == 1, // 3 = esFranquicia (0 o 1)
                    Date(resultadoConsultaLectura.getLong(4)) // 4 = fechaDeCreacion (en milisegundos)
                )
                arregloRespuesta.add(tienda)
            } while (resultadoConsultaLectura.moveToNext())
        }
        Log.d("Error", "$arregloRespuesta")

        resultadoConsultaLectura.close()
        return arregloRespuesta
    }


    fun actualizarTienda(id: Int, nombre: String, ubicacion: String, esFranquicia: Boolean, fechaDeCreacion: Date): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresAActualizar = ContentValues()

        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("ubicacion", ubicacion)
        valoresAActualizar.put("esFranquicia", if (esFranquicia) 1 else 0) // SQLite no tiene boolean, usamos 1/0
        valoresAActualizar.put("fechaDeCreacion", fechaDeCreacion.time) // Guardamos la fecha en milisegundos

        // Condición WHERE para actualizar la tienda correcta
        val parametrosConsultaActualizar = arrayOf(id.toString())

        val resultadoActualizar = baseDatosEscritura.update(
            "TIENDA", // Nombre de la tabla
            valoresAActualizar, // Valores a actualizar
            "id=?", // Condición WHERE (id = ?)
            parametrosConsultaActualizar // Valores para reemplazar el ?
        )

        baseDatosEscritura.close()

        return resultadoActualizar > 0 // Devuelve true si se actualizó correctamente
    }


    fun consultarTiendaPorId(id: Int): BTienda? {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = """
        SELECT * FROM TIENDA WHERE id = ?
    """.trimIndent()

        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        if (resultadoConsultaLectura.moveToFirst()) {
            val tienda = BTienda(
                id = resultadoConsultaLectura.getInt(0), // 0 = id
                nombre = resultadoConsultaLectura.getString(1), // 1 = nombre
                ubicacion = resultadoConsultaLectura.getString(2), // 2 = ubicación
                esFranquicia = resultadoConsultaLectura.getInt(3) == 1, // 3 = esFranquicia (0 o 1)
                fechaDeCreacion = Date(resultadoConsultaLectura.getLong(4)) // 4 = fecha en milisegundos
            )
            resultadoConsultaLectura.close()
            return tienda
        } else {
            resultadoConsultaLectura.close()
            return null
        }
    }



}
