package com.example.proyecto_final.ObjetosParcelables

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class CategoriaNegocio() : Parcelable{
    var idcategoria = ""
    var NombreCategoria = ""
    var ImagenCategoria=  ""
    constructor(parcel: Parcel) : this() {
        idcategoria = parcel.readString().toString()
        NombreCategoria = parcel.readString().toString()
        ImagenCategoria = parcel.readString().toString()
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idcategoria)
        parcel.writeString(NombreCategoria)
        parcel.writeString(ImagenCategoria)
    }

    companion object CREATOR : Parcelable.Creator<CategoriaNegocio> {
        override fun createFromParcel(parcel: Parcel): CategoriaNegocio {
            return CategoriaNegocio(parcel)
        }

        override fun newArray(size: Int): Array<CategoriaNegocio?> {
            return arrayOfNulls(size)
        }
    }
}