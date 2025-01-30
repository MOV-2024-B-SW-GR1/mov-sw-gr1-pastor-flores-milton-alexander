package com.example.examen02

import android.content.Context

class EBaseDeDatos {
    companion object {
        var dbHelper: ESqliteHelper? = null
        var tablaProducto: ESqliteHelperProducto? = null
        var tablaTienda: ESqliteHelperTienda? = null

        fun inicializarBaseDeDatos(contexto: Context) {
            dbHelper = ESqliteHelper(contexto)
            tablaProducto = ESqliteHelperProducto(dbHelper!!)
            tablaTienda = ESqliteHelperTienda(dbHelper!!)
        }
    }
}
