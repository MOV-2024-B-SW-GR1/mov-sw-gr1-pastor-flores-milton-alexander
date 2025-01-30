package com.example.examen02

import android.os.Parcel
import android.os.Parcelable
import java.text.NumberFormat
import java.util.Locale

data class BProducto(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val stock: Int,
    val tiendaId: Int
) : Parcelable {

    override fun toString(): String {
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val precioFormateado = formatoMoneda.format(precio)
        return "$nombre - $categoria\nPrecio: $precioFormateado | Stock: $stock unidades"
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(categoria)
        parcel.writeDouble(precio)
        parcel.writeInt(stock)
        parcel.writeInt(tiendaId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BProducto> {
        override fun createFromParcel(parcel: Parcel): BProducto {
            return BProducto(parcel)
        }

        override fun newArray(size: Int): Array<BProducto?> {
            return arrayOfNulls(size)
        }
    }
}
