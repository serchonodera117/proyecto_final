package com.example.proyecto_final.Fragmentos

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.*
import com.example.proyecto_final.Adaptadores.AdaptadorCategoriasRegistro
import com.example.proyecto_final.Adaptadores.AdaptadorNegociosDeMiPerfil
import com.example.proyecto_final.ObjetosParcelables.DatosNegocio
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registro_vendedor.*
import kotlinx.android.synthetic.main.fragment_perfil_f.*
import org.json.JSONArray
import org.json.JSONObject

class Perfil_f : Fragment() {
    var misMegociosDelPerfil = ArrayList<DatosNegocio>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil_f, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //---------------------------------------------- Recivimiento de datos
          var usuario = arguments?.getParcelable<DatosUsuario>("infoPerfil")

        //---------------------------------------------- Envío de datos
            //enviar datos a donde se necesiten



        //----------------------------------------------------- Asignación de valores
         if(usuario?.FotoUsuario.toString().length > 10){Picasso.get().load(usuario?.FotoUsuario).into(miImagenUsuario)}
           else{Picasso.get().load("https://w7.pngwing.com/pngs/831/88/png-transparent-user-profile-computer-icons-user-interface-mystique-miscellaneous-user-interface-design-smile.png").into(miImagenUsuario)}

        miNombreUsuario.setText(usuario?.NombreUsuario)
        miTipoDeUsuario.setText(usuario?.TipoDeUsuario)
        miMunicipio.setText(usuario?.MunicipioUsuario)
        miCorreo.setText("Correo: "+usuario?.CorreoUsuario)
        miTelefono.setText("Telefono: "+usuario?.TelefonoUsuario)


        //---------------------------------------------------- Desaparecer o Aparecer el botón de administrar negocios

       if(usuario?.TipoDeUsuario == "Vendedor"){accesoNegocios.visibility = View.VISIBLE}
        else{accesoNegocios.visibility = View.GONE}


        //---------------------------------------------------- observar y listar negocios
         var esVisible = false
        misNegociosMiPerfil.visibility = View.GONE

         vistaNegocios.setOnClickListener {
             esVisible = !esVisible

             if(esVisible){
                 textView4.visibility = View.GONE
                 misNegociosMiPerfil.visibility = View.VISIBLE
                 listarMisNegocios(usuario)
             }else{
                 misNegociosMiPerfil.visibility = View.GONE
                 textView4.visibility = View.VISIBLE
             }

         }
        //---------------------------------------------------------- hisyroeial de compras
        verHistorialCompras.setOnClickListener {
            var miHistorial = Intent(activity, HistrorialCompras::class.java)
            miHistorial.putExtra("histrialVentas", usuario)
            startActivity(miHistorial)
        }
        //---------------------------------------------------------- historial de ventas
        verHistorialVentas.setOnClickListener{
            var miHistorial = Intent(activity, Historial::class.java)
                miHistorial.putExtra("histrialVentas", usuario)
            startActivity(miHistorial)
        }

        //--------------------------------------------------- ir al apartado de agregar negocios.

        formularioAgregarNegocio.setOnClickListener {
              var formularioNuevoNegocio = Intent(requireActivity(), AgregarNegocioNuevo::class.java)
                 formularioNuevoNegocio.putExtra("correoVendedor", usuario?.CorreoUsuario)
            startActivity(formularioNuevoNegocio)
        }

        //-------------------------------------------------------------- Cerrar Sesión
        cerrarSesión.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireActivity())
            dialogBuilder.setMessage("¿Desea cerrar sesión?")
                .setCancelable(false)
                .setPositiveButton("Cerrar sesión", DialogInterface.OnClickListener {
                        dialog, id ->
                    val sesion = activity
                    sesion?.finish()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            val alert = dialogBuilder.create()
            alert.setTitle("Aviso")
            alert.show()
        }
    }

    fun listarMisNegocios(usuario: DatosUsuario?){
        val queue = Volley.newRequestQueue(activity)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/listar.php?comando=NegociosPerfil" +
                "&correo=${usuario?.CorreoUsuario}"


        val respuestaString = StringRequest(
            Request.Method.GET, url, { response ->
                //-----------------------------Obtención de un array json con los datos consultados
                    var respuesta = response.toString()
                    val jsonObj: JSONObject = JSONObject(respuesta)
                    val jsonArray: JSONArray = jsonObj.getJSONArray("misNegocios")
                    if (jsonArray.length() == 0) {
                        Aviso("Aviso", "no se encuentran negocios")
                    } else {
                        var indice = 0
                        //-----------------------------Almacenamiento del array en variable, y busqueda en atributos del objeto
                        misMegociosDelPerfil.clear()
                        for (i in 0 until jsonArray.length()) {
                              var elNegocio = DatosNegocio()
                            var datos = JSONObject(jsonArray.get(indice).toString())

                            elNegocio.idnegocio = datos.getString("idnegocio").toString()
                            elNegocio.CorreoUsuario = datos.getString("CorreoUsuario").toString()
                            elNegocio.NombreNegocio = datos.getString("NombreNegocio").toString()
                            elNegocio.LogoNegocio = datos.getString("LogoNegocio").toString()
                            elNegocio.Efectivo = datos.getString("Efectivo").toString()
                            elNegocio.Tarjeta = datos.getString("Tarjeta").toString()
                            elNegocio.PagoOxxo = datos.getString("PagoOxxo").toString()
                            elNegocio.Oferta_producto = datos.getString("Oferta_producto").toString()
                            elNegocio.Oferta_servicio = datos.getString("Oferta_servicio").toString()
                            elNegocio.CategoriaNegocio = datos.getString("CategoriaNegocio").toString()
                            elNegocio.MunicipioNegocio = datos.getString("MunicipioNegocio").toString()
                            elNegocio.Colonianegocio = datos.getString("ColoniaNegocio").toString()
                            elNegocio.DireccionNegocio = datos.getString("DireccionNegocio").toString()

                            misMegociosDelPerfil.add(elNegocio)
                            indice++
                        }

                        //armar adapter
                        listadoMisNegocios.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                        var miAdaptador = AdaptadorNegociosDeMiPerfil(misMegociosDelPerfil)
                        listadoMisNegocios.adapter = miAdaptador

                       //Metodo clic listener
                        miAdaptador.setOnItemClickListener(object :
                            AdaptadorNegociosDeMiPerfil.onItemClickListener{
                            override fun onItemClick(position: Int) {
                               Toast.makeText(requireActivity(), "Sleecionado: " +
                                       "${misMegociosDelPerfil.get(position).NombreNegocio}",
                                       Toast.LENGTH_SHORT).show()

                                val administracionNegocio = Intent(requireActivity(), AdministrarNegocio::class.java)
                                     administracionNegocio.putExtra("administracionNegocio", misMegociosDelPerfil.get(position))
                                     administracionNegocio.putExtra("usuarioDatos", usuario)
                                     startActivity(administracionNegocio)
                            }
                        })

                    }
            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }

    fun Aviso(Titulo:String, Mensaje:String)
    {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setMessage(Mensaje)
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle(Titulo)
        alert.show()
    }
}