package com.example.proyecto_final.ObjetosParcelables

import android.os.Parcel
import android.os.Parcelable

class Productos() : Parcelable{
    var idproducto = ""
    var correoVendedor = ""
    var idnegocio = ""
    var NombreNegocio = ""
    var Nombreproducto = ""
    var SeccionProducto = ""
    var ExistenciaProducto = ""
    var DescripcionProducto = ""
    var TipoOferta = ""
    var ImagenProducto = ""
    var Precio = ""
    var PrecioEnvio = ""
    var Visibilidad = ""

    constructor(parcel: Parcel) : this() {
        idproducto = parcel.readString().toString()
        correoVendedor = parcel.readString().toString()
        idnegocio = parcel.readString().toString()
        NombreNegocio = parcel.readString().toString()
        Nombreproducto  = parcel.readString().toString()
        SeccionProducto = parcel.readString().toString()
        ExistenciaProducto = parcel.readString().toString()
        DescripcionProducto = parcel.readString().toString()
        TipoOferta = parcel.readString().toString()
        ImagenProducto = parcel.readString().toString()
        Precio = parcel.readString().toString()
        PrecioEnvio = parcel.readString().toString()
        Visibilidad = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idproducto)
        parcel.writeString(correoVendedor)
        parcel.writeString(idnegocio)
        parcel.writeString(NombreNegocio)
        parcel.writeString(Nombreproducto)
        parcel.writeString(SeccionProducto)
        parcel.writeString(ExistenciaProducto)
        parcel.writeString(DescripcionProducto)
        parcel.writeString(TipoOferta)
        parcel.writeString(ImagenProducto)
        parcel.writeString(Precio)
        parcel.writeString(PrecioEnvio)
        parcel.writeString(Visibilidad)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Productos> {
        override fun createFromParcel(parcel: Parcel): Productos {
            return Productos(parcel)
        }

        override fun newArray(size: Int): Array<Productos?> {
            return arrayOfNulls(size)
        }
    }
}