package com.example.examen02

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelper(
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "movilesTiendaExamen", // Nombre único de la base de datos
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaTienda = """
            CREATE TABLE TIENDA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                ubicacion VARCHAR(100),
                esFranquicia INTEGER,
                fechaDeCreacion TEXT,
                latitud REAL,  
                longitud REAL  
            );
        """.trimIndent()

        val scriptSQLCrearTablaProducto = """
            CREATE TABLE PRODUCTO (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                categoria VARCHAR(50),
                precio REAL,
                stock INTEGER,
                tiendaId INTEGER,
                FOREIGN KEY (tiendaId) REFERENCES TIENDA(id) ON DELETE CASCADE
            );
        """.trimIndent()

        db?.execSQL(scriptSQLCrearTablaTienda)
        db?.execSQL(scriptSQLCrearTablaProducto)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS PRODUCTO")
        db?.execSQL("DROP TABLE IF EXISTS TIENDA")
        onCreate(db)
    }
}
