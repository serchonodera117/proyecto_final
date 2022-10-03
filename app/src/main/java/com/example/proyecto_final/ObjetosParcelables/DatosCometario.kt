package com.example.proyecto_final.ObjetosParcelables

import android.os.Parcel
import android.os.Parcelable

class DatosCometario() :Parcelable {
    var id_comentario = ""
    var id_comentario_respuesta = ""
    var id_producto = ""
    var Tipo_Comentario = ""
    var Nombre_Usuario = ""
    var Imagen_Usuario = ""
    var Nombre_Usuario_Respuesta = ""
    var Contenido = ""
    var Fecha_Comentario = ""

    constructor(parcel: Parcel) : this() {
        id_comentario = parcel.readString().toString()
        id_comentario_respuesta = parcel.readString().toString()
        id_producto = parcel.readString().toString()
        Tipo_Comentario = parcel.readString().toString()
        Nombre_Usuario = parcel.readString().toString()
        Imagen_Usuario = parcel.readString().toString()
        Nombre_Usuario_Respuesta = parcel.readString().toString()
        Contenido = parcel.readString().toString()
        Fecha_Comentario = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_comentario)
        parcel.writeString(id_comentario_respuesta)
        parcel.writeString(id_producto)
        parcel.writeString(Tipo_Comentario)
        parcel.writeString(Nombre_Usuario)
        parcel.writeString(Imagen_Usuario)
        parcel.writeString(Nombre_Usuario_Respuesta)
        parcel.writeString(Contenido)
        parcel.writeString(Fecha_Comentario)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DatosCometario> {
        override fun createFromParcel(parcel: Parcel): DatosCometario {
            return DatosCometario(parcel)
        }

        override fun newArray(size: Int): Array<DatosCometario?> {
            return arrayOfNulls(size)
        }
    }

}