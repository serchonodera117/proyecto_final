package com.example.proyecto_final.ObjetosParcelables

import android.os.Parcel
import android.os.Parcelable

class DatosUsuario() : Parcelable {
    var idusuario = ""
    var NombreUsuario = ""
    var FotoUsuario = ""
    var MunicipioUsuario = ""
    var DomicilioUsuario = ""
    var ColoniaUsuario = ""
    var CorreoUsuario= ""
    var ContrasenaUsuario = ""
    var TelefonoUsuario= ""
    var TipoDeUsuario = ""
    var VerificacionUsuario = ""

    constructor(parcel: Parcel) : this() {
        idusuario = parcel.readString().toString()
        NombreUsuario = parcel.readString().toString()
        FotoUsuario = parcel.readString().toString()
        MunicipioUsuario = parcel.readString().toString()
        DomicilioUsuario = parcel.readString().toString()
        ColoniaUsuario = parcel.readString().toString()
        CorreoUsuario = parcel.readString().toString()
        ContrasenaUsuario = parcel.readString().toString()
        TelefonoUsuario = parcel.readString().toString()
        TipoDeUsuario = parcel.readString().toString()
        VerificacionUsuario = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idusuario)
        parcel.writeString(NombreUsuario)
        parcel.writeString(FotoUsuario)
        parcel.writeString(MunicipioUsuario)
        parcel.writeString(DomicilioUsuario)
        parcel.writeString(ColoniaUsuario)
        parcel.writeString(CorreoUsuario)
        parcel.writeString(ContrasenaUsuario)
        parcel.writeString(TelefonoUsuario)
        parcel.writeString(TipoDeUsuario)
        parcel.writeString(VerificacionUsuario)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DatosUsuario> {
        override fun createFromParcel(parcel: Parcel): DatosUsuario {
            return DatosUsuario(parcel)
        }

        override fun newArray(size: Int): Array<DatosUsuario?> {
            return arrayOfNulls(size)
        }
    }

}