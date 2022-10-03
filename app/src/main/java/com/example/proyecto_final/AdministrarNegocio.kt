package com.example.proyecto_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.proyecto_final.Fragmentos.AgregarProducto_f
import com.example.proyecto_final.Fragmentos.MiListaProductos_f
import com.example.proyecto_final.ObjetosParcelables.DatosNegocio
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_administrar_negocio.*

class AdministrarNegocio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrar_negocio)

         //--------------------------------- Fragments
        var losProductos = MiListaProductos_f()
        var addProductos = AgregarProducto_f()

        //-----------------------------------------------------------recivimiento de información
         var bunle = intent.extras
         var miInfoNegocio = bunle?.getParcelable<DatosNegocio>("administracionNegocio")
         var miInfoUsuario = bunle?.getParcelable<DatosUsuario>("usuarioDatos")

        //----------------------------------------------------------- envío de información
                          //enviar al fragment de la lista de productos
          var dato = Bundle()
             dato.putParcelable("negocioLisraProducto", miInfoNegocio)
             dato.putParcelable("usuarioVerCOm", miInfoUsuario)
             losProductos.arguments = dato

        //-----------------------------------------------------------definir el fragment por defecto  Fragments
          cambioFragment(losProductos)


        //------------------------------------------------------------ Asignar infotmación
        Picasso.get().load(miInfoNegocio?.LogoNegocio).into(fotoAdminNegocio)
        nombreAdminNegocio.setText(miInfoNegocio?.NombreNegocio)

        nombreAdminMunicipio.setText(miInfoNegocio?.MunicipioNegocio)
        nombreAdminDireccion.setText(miInfoNegocio?.DireccionNegocio)

        var formasPago = ""
        var misOfertas = ""

        if(miInfoNegocio?.PagoOxxo != "No aplica"){formasPago += "-${miInfoNegocio?.PagoOxxo}\n"}
        if(miInfoNegocio?.Efectivo != "No aplica"){formasPago += "-${miInfoNegocio?.Efectivo}\n"}
        if(miInfoNegocio?.Tarjeta != "No aplica"){formasPago += "-${miInfoNegocio?.Tarjeta}\n"}

        misFormasDePago.setText(formasPago)

        if(miInfoNegocio?.Oferta_producto != "No aplica"){misOfertas += "-${miInfoNegocio?.Oferta_producto}\n"}
        if(miInfoNegocio?.Oferta_servicio != "No aplica"){misOfertas += "-${miInfoNegocio?.Oferta_servicio}\n"}

        ofertas.setText(misOfertas)

        //------------------------------------------------------------------- cambiar de fragment
        textoRecargar.visibility = View.GONE
        recargarLista.visibility = View.GONE

                                   //--------------------------------------- Cambio al fragment de agregar
        formularioAgregarProducto.setOnClickListener{
            textoRecargar.visibility = View.VISIBLE
            recargarLista.visibility = View.VISIBLE
            formularioAgregarProducto.visibility = View.GONE

             var envioInfoNegocio = Bundle()
                 envioInfoNegocio.putParcelable("id_nombreNegocio", miInfoNegocio)
                 envioInfoNegocio.putParcelable("llaveCorreo", miInfoUsuario)
                 addProductos.arguments = envioInfoNegocio

            cambioFragment(addProductos)
        }
        recargarLista.setOnClickListener {
            textoRecargar.visibility = View.GONE
            recargarLista.visibility = View.GONE
            formularioAgregarProducto.visibility = View.VISIBLE
           cambioFragment(losProductos)
           //recargar-listar pagina

        }


        Volver_miPerfil_de_admin.setOnClickListener { finish() }

    }

    fun cambioFragment(miFragment: Fragment)
    { if(miFragment != null){
            val transaccion = supportFragmentManager.beginTransaction()
            transaccion.replace(R.id.contenedorListaProductos, miFragment)
            transaccion.commit()
        }

    }
}