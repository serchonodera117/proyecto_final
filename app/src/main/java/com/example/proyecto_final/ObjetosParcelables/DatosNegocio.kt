package com.example.proyecto_final.ObjetosParcelables

import android.os.Parcel
import android.os.Parcelable

class DatosNegocio() : Parcelable {
    var idnegocio = ""
    var CorreoUsuario = ""
    var NombreNegocio = ""
    var LogoNegocio = ""
    var Efectivo = ""
    var Tarjeta = ""
    var PagoOxxo = ""
    var Oferta_producto = ""
    var Oferta_servicio = ""
    var CategoriaNegocio = ""
    var MunicipioNegocio = ""
    var Colonianegocio = ""
    var DireccionNegocio = ""

    constructor(parcel: Parcel) : this() {
        idnegocio = parcel.readString().toString()
        CorreoUsuario = parcel.readString().toString()
        NombreNegocio = parcel.readString().toString()
        LogoNegocio = parcel.readString().toString()
        Efectivo = parcel.readString().toString()
        Tarjeta = parcel.readString().toString()
        PagoOxxo = parcel.readString().toString()
        Oferta_producto = parcel.readString().toString()
        Oferta_servicio = parcel.readString().toString()
        CategoriaNegocio = parcel.readString().toString()
        MunicipioNegocio = parcel.readString().toString()
        Colonianegocio = parcel.readString().toString()
        DireccionNegocio = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idnegocio)
        parcel.writeString(CorreoUsuario)
        parcel.writeString(NombreNegocio)
        parcel.writeString(LogoNegocio)
        parcel.writeString(Efectivo)
        parcel.writeString(Tarjeta)
        parcel.writeString(PagoOxxo)
        parcel.writeString(Oferta_producto)
        parcel.writeString(Oferta_servicio)
        parcel.writeString(CategoriaNegocio)
        parcel.writeString(MunicipioNegocio)
        parcel.writeString(Colonianegocio)
        parcel.writeString(DireccionNegocio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DatosNegocio> {
        override fun createFromParcel(parcel: Parcel): DatosNegocio {
            return DatosNegocio(parcel)
        }

        override fun newArray(size: Int): Array<DatosNegocio?> {
            return arrayOfNulls(size)
        }
    }


}