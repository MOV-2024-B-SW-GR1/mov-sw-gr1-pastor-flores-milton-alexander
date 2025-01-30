package com.example.examen02

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BTienda(
    val id: Int,
    val nombre: String,
    val ubicacion: String,
    val esFranquicia: Boolean,
    val fechaDeCreacion: Date
) : Parcelable {

    override fun toString(): String {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaDeCreacion)
        val tipo = if (esFranquicia) "Franquicia" else "Independiente"
        return "$nombre - $ubicacion ($tipo) \nCreado: $fechaFormateada"
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(), // Convertir byte a Boolean
        Date(parcel.readLong()) // Leer la fecha en formato Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(ubicacion)
        parcel.writeByte(if (esFranquicia) 1 else 0) // Convertir Boolean a byte
        parcel.writeLong(fechaDeCreacion.time) // Guardar la fecha como Long
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BTienda> {
        override fun createFromParcel(parcel: Parcel): BTienda {
            return BTienda(parcel)
        }

        override fun newArray(size: Int): Array<BTienda?> {
            return arrayOfNulls(size)
        }
    }
}