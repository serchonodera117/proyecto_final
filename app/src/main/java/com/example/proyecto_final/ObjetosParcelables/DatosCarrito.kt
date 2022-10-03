package com.example.proyecto_final.ObjetosParcelables

import android.os.Parcel
import android.os.Parcelable

class DatosCarrito() :Parcelable{




    var idproductocarrito = ""
    var correo_Cliente = ""
    var id_producto = ""
    var Cantidad_Solicitada = ""
    var id_negocio = ""
    var correo_Vendedor = ""
    var Nombre_Negocio = ""
    var Nombre_Producto = ""
    var Seccion_Producto = ""
    var Existencia_Producto = ""
    var Descripcion_Producto = ""
    var Tipo_Oferta = ""
    var Imagen_Producto = ""
    var el_Precio = ""
    var Precio_Envio = ""

    constructor(parcel: Parcel) : this() {
        idproductocarrito = parcel.readString().toString()
        correo_Cliente = parcel.readString().toString()
        id_producto = parcel.readString().toString()
        Cantidad_Solicitada = parcel.readString().toString()
        id_negocio = parcel.readString().toString()
        correo_Vendedor = parcel.readString().toString()
        Nombre_Negocio = parcel.readString().toString()
        Nombre_Producto = parcel.readString().toString()
        Seccion_Producto = parcel.readString().toString()
        Existencia_Producto = parcel.readString().toString()
        Descripcion_Producto = parcel.readString().toString()
        Tipo_Oferta = parcel.readString().toString()
        Imagen_Producto = parcel.readString().toString()
        el_Precio = parcel.readString().toString()
        Precio_Envio = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idproductocarrito)
        parcel.writeString(correo_Cliente)
        parcel.writeString(id_producto)
        parcel.writeString(Cantidad_Solicitada)
        parcel.writeString(id_negocio)
        parcel.writeString(correo_Vendedor)
        parcel.writeString(Nombre_Negocio)
        parcel.writeString(Nombre_Producto)
        parcel.writeString(Seccion_Producto)
        parcel.writeString(Existencia_Producto)
        parcel.writeString(Descripcion_Producto)
        parcel.writeString(Tipo_Oferta)
        parcel.writeString(Imagen_Producto)
        parcel.writeString(el_Precio)
        parcel.writeString(Precio_Envio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DatosCarrito> {
        override fun createFromParcel(parcel: Parcel): DatosCarrito {
            return DatosCarrito(parcel)
        }

        override fun newArray(size: Int): Array<DatosCarrito?> {
            return arrayOfNulls(size)
        }
    }
}