package com.example.examen02

import android.content.ContentValues
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ESqliteHelperTienda(private val dbHelper: ESqliteHelper) {

    //------------- CRUD ----------------------------

    fun crearTienda(
        nombre: String,
        ubicacion: String,
        esFranquicia: Boolean,
        fechaDeCreacion: Date,
        latitud: Double,
        longitud: Double
    ): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresGuardar = ContentValues()

        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaDeCreacion)

        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("ubicacion", ubicacion)
        valoresGuardar.put("esFranquicia", if (esFranquicia) 1 else 0)
        valoresGuardar.put("fechaDeCreacion", fechaFormateada)
        valoresGuardar.put("latitud", latitud)
        valoresGuardar.put("longitud", longitud)

        val resultadoGuardar = baseDatosEscritura.insert("TIENDA", null, valoresGuardar)

        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    fun eliminarTienda(id: Int): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())

        val resultadoEliminar = baseDatosEscritura.delete(
            "TIENDA",
            "id=?",
            parametrosConsultaDelete
        )

        baseDatosEscritura.close()
        return resultadoEliminar > 0
    }

    fun consultarTodasLasTiendas(): List<BTienda> {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = "SELECT * FROM TIENDA"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        val arregloRespuesta = arrayListOf<BTienda>()
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val tienda = BTienda(
                    id = resultadoConsultaLectura.getInt(0),
                    nombre = resultadoConsultaLectura.getString(1),
                    ubicacion = resultadoConsultaLectura.getString(2),
                    esFranquicia = resultadoConsultaLectura.getInt(3) == 1,
                    fechaDeCreacion = formatoFecha.parse(resultadoConsultaLectura.getString(4))!!,
                    latitud = resultadoConsultaLectura.getDouble(5),
                    longitud = resultadoConsultaLectura.getDouble(6)
                )
                arregloRespuesta.add(tienda)
            } while (resultadoConsultaLectura.moveToNext())
        }
        Log.d("ConsultaTiendas", "$arregloRespuesta")

        resultadoConsultaLectura.close()
        return arregloRespuesta
    }

    fun actualizarTienda(
        id: Int,
        nombre: String,
        ubicacion: String,
        esFranquicia: Boolean,
        fechaDeCreacion: Date,
        latitud: Double,
        longitud: Double
    ): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresAActualizar = ContentValues()

        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaDeCreacion)

        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("ubicacion", ubicacion)
        valoresAActualizar.put("esFranquicia", if (esFranquicia) 1 else 0)
        valoresAActualizar.put("fechaDeCreacion", fechaFormateada)
        valoresAActualizar.put("latitud", latitud)
        valoresAActualizar.put("longitud", longitud)

        val parametrosConsultaActualizar = arrayOf(id.toString())

        val resultadoActualizar = baseDatosEscritura.update(
            "TIENDA",
            valoresAActualizar,
            "id=?",
            parametrosConsultaActualizar
        )

        baseDatosEscritura.close()
        return resultadoActualizar > 0
    }

    fun consultarTiendaPorId(id: Int): BTienda? {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = "SELECT * FROM TIENDA WHERE id = ?"
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        if (resultadoConsultaLectura.moveToFirst()) {
            val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val tienda = BTienda(
                id = resultadoConsultaLectura.getInt(0),
                nombre = resultadoConsultaLectura.getString(1),
                ubicacion = resultadoConsultaLectura.getString(2),
                esFranquicia = resultadoConsultaLectura.getInt(3) == 1,
                fechaDeCreacion = formatoFecha.parse(resultadoConsultaLectura.getString(4))!!,
                latitud = resultadoConsultaLectura.getDouble(5),
                longitud = resultadoConsultaLectura.getDouble(6)
            )
            resultadoConsultaLectura.close()
            return tienda
        }
        resultadoConsultaLectura.close()
        return null
    }
}

