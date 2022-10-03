package com.example.proyecto_final.ObjetosParcelables

import android.os.Parcel
import android.os.Parcelable

class Pedidos() : Parcelable {
    var id_pedido = ""
    var Correo_Vendedor = ""
    var id_usuario = ""
    var Nombre_Usuario = ""
    var Municipio_Usuario = ""
    var Direccion_Usuario = ""
    var id_negocio = ""
    var Nombre_Negocio = ""
    var id_producto = ""
    var Nombre_Producto = ""
    var Descripcion_producto = ""
    var Seccion_Producto = ""
    var Imagen_Producto = ""
    var Cantidad_Producto = ""
    var Precio_Final = ""
    var Tipo_Pedido = ""
    var Fecha_Pedido = ""
    var Estado_Producto = ""
    var Estado_Servicio = ""

    constructor(parcel: Parcel) : this() {
        id_pedido = parcel.readString().toString()
        Correo_Vendedor = parcel.readString().toString()
        id_usuario = parcel.readString().toString()
        Nombre_Usuario = parcel.readString().toString()
        Municipio_Usuario = parcel.readString().toString()
        Direccion_Usuario = parcel.readString().toString()
        id_negocio = parcel.readString().toString()
        Nombre_Negocio = parcel.readString().toString()
        id_producto = parcel.readString().toString()
        Nombre_Producto = parcel.readString().toString()
        Descripcion_producto = parcel.readString().toString()
        Seccion_Producto = parcel.readString().toString()
        Imagen_Producto = parcel.readString().toString()
        Cantidad_Producto = parcel.readString().toString()
        Precio_Final = parcel.readString().toString()
        Tipo_Pedido = parcel.readString().toString()
        Fecha_Pedido = parcel.readString().toString()
        Estado_Producto = parcel.readString().toString()
        Estado_Servicio = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_pedido)
        parcel.writeString(Correo_Vendedor)
        parcel.writeString(id_usuario)
        parcel.writeString(Nombre_Usuario)
        parcel.writeString(Municipio_Usuario)
        parcel.writeString(Direccion_Usuario)
        parcel.writeString(id_negocio)
        parcel.writeString(Nombre_Negocio)
        parcel.writeString(id_producto)
        parcel.writeString(Nombre_Producto)
        parcel.writeString(Descripcion_producto)
        parcel.writeString(Seccion_Producto)
        parcel.writeString(Imagen_Producto)
        parcel.writeString(Cantidad_Producto)
        parcel.writeString(Precio_Final)
        parcel.writeString(Tipo_Pedido)
        parcel.writeString(Fecha_Pedido)
        parcel.writeString(Estado_Producto)
        parcel.writeString(Estado_Servicio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pedidos> {
        override fun createFromParcel(parcel: Parcel): Pedidos {
            return Pedidos(parcel)
        }

        override fun newArray(size: Int): Array<Pedidos?> {
            return arrayOfNulls(size)
        }
    }
}



