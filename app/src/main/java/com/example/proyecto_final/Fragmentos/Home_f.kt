package com.example.proyecto_final.Fragmentos

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.SearchView.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.Adaptadores.AdaptadorCategoriasHome
import com.example.proyecto_final.Adaptadores.AdaptadorCategoriasRegistro
import com.example.proyecto_final.Adaptadores.AdaptadorNegociosDeMiPerfil
import com.example.proyecto_final.Adaptadores.AdaptadorNegociosHome
import com.example.proyecto_final.AdministrarNegocio
import com.example.proyecto_final.Negocio_consultado
import com.example.proyecto_final.ObjetosParcelables.CategoriaNegocio
import com.example.proyecto_final.ObjetosParcelables.DatosNegocio
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.example.proyecto_final.R
import kotlinx.android.synthetic.main.activity_agregar_negocio_nuevo.*
import kotlinx.android.synthetic.main.fragment_home_f.*
import kotlinx.android.synthetic.main.fragment_perfil_f.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class Home_f : Fragment() {
   var homeNegocios = ArrayList<DatosNegocio>()
    var busquedaNegocio = ArrayList<DatosNegocio>()
    var filtroCategoria = ArrayList<DatosNegocio>()
    var filtroMunicipio = ArrayList<DatosNegocio>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_f, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //------------------------------------------------------------------------ recibir datos
        var usuario = arguments?.getParcelable<DatosUsuario>("infoPerfil")

        //---------------------------------------------------------------------- Buscar en la lista de negocios.
        buscadorNegocios.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }
                                                        //------------------- toda esta jalada es el algoritmo de busqueda
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()){
                    busquedaNegocio.clear()
                    val busqueda = newText.toLowerCase(Locale.getDefault())
                    homeNegocios.forEach {
                        if (it.NombreNegocio.toLowerCase(Locale.getDefault()).contains(busqueda) ||
                            it.Colonianegocio.toLowerCase(Locale.getDefault()).contains(busqueda)) {
                            busquedaNegocio.add(it)
                        }
                    }
                    //armar adapter
                    principalNegociosHome.clearFocus()
                    principalNegociosHome.layoutManager = LinearLayoutManager(activity)
                    var miAdaptador = AdaptadorNegociosHome(busquedaNegocio)
                    principalNegociosHome.adapter = miAdaptador

                    //Metodo clic listener
                    miAdaptador.setOnItemClickListener(object :
                        AdaptadorNegociosHome.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            Toast.makeText(requireActivity(), "Sleecionado: " +
                                    "${busquedaNegocio.get(position).NombreNegocio}",
                                Toast.LENGTH_SHORT).show()

                            val negocioConsulta = Intent(requireActivity(), Negocio_consultado::class.java)
                            negocioConsulta.putExtra("negocioConsultado", busquedaNegocio.get(position))
                            negocioConsulta.putExtra("usuarioDatos", usuario)
                            startActivity(negocioConsulta)

                        }
                    })
                }else{
                    //armar adapter
                    principalNegociosHome.clearFocus()
                    principalNegociosHome.layoutManager = LinearLayoutManager(activity)
                    var miAdaptador = AdaptadorNegociosHome(homeNegocios)
                    principalNegociosHome.adapter = miAdaptador

                    //Metodo clic listener
                    miAdaptador.setOnItemClickListener(object :
                        AdaptadorNegociosHome.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            Toast.makeText(requireActivity(), "Sleecionado: " +
                                    "${homeNegocios.get(position).NombreNegocio}",
                                Toast.LENGTH_SHORT).show()

                            val negocioConsulta = Intent(requireActivity(), Negocio_consultado::class.java)
                            negocioConsulta.putExtra("negocioConsultado", filtroCategoria.get(position))
                            negocioConsulta.putExtra("usuarioDatos", usuario)
                            startActivity(negocioConsulta)
                } })
                }
                return true
            }
        })

        //-------------------------------------------------- Filtrado para los municipios y categorias
                                    //------- mostrar [T]odo
        mostrarALl.setOnClickListener {
            Toast.makeText(requireActivity(), "Todos los negocios", Toast.LENGTH_SHORT).show()
               principalNegociosHome.clearFocus()
               listarHome(usuario)
           }
        colima.setOnClickListener { filtrarMunicipio("Colima") }
        villaD.setOnClickListener { filtrarMunicipio("Villa de Alvarez") }

        //---------------------------------------------------- listar los negocios y categorias
       listarHome(usuario)
        CategoríasHome()
    }

    fun listarHome(usuario: DatosUsuario?) {
        val queue = Volley.newRequestQueue(activity)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/listar.php?comando=NegociosHome"


        val respuestaString = StringRequest(
            Request.Method.GET, url, { response ->
                //-----------------------------Obtención de un array json con los datos consultados
                    var respuesta = response.toString()
                    val jsonObj: JSONObject = JSONObject(respuesta)
                    val jsonArray: JSONArray = jsonObj.getJSONArray("miHome")
                    if (jsonArray.length() == 0) {
                        Aviso("Aviso", "no se encuentran negocios")
                    } else {
                        homeNegocios.clear()
                        var indice = 0
                        //-----------------------------Almacenamiento del array en variable, y busqueda en atributos del objeto.clear()
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

                            homeNegocios.add(elNegocio)
                            indice++
                        }

                        homeNegocios.sortBy { it.NombreNegocio }

                        //armar adapter
                         principalNegociosHome.layoutManager = LinearLayoutManager(activity)
                        var miAdaptador = AdaptadorNegociosHome(homeNegocios)
                        principalNegociosHome.adapter = miAdaptador

                        //Metodo clic listener
                        miAdaptador.setOnItemClickListener(object :
                            AdaptadorNegociosHome.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                Toast.makeText(requireActivity(), "Sleecionado: " +
                                        "${homeNegocios.get(position).NombreNegocio}",
                                    Toast.LENGTH_SHORT).show()

                                val negocioConsulta = Intent(requireActivity(), Negocio_consultado::class.java)
                                negocioConsulta .putExtra("negocioConsultado", homeNegocios.get(position))
                                negocioConsulta .putExtra("usuarioDatos", usuario)
                                startActivity(negocioConsulta)

                            }
                        })

                    }
            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }


                                                                                 //--------------------------------filtro por categorias
    fun CategoríasHome(){
        val queue = Volley.newRequestQueue(activity)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/agregar_categoria.php?codigo=Listar"

        val respuestaString = StringRequest(
            Request.Method.GET, url, { response ->
                //-----------------------------Obtención de un array json con los datos consultados
                Log.i("response",response.toString())

                if(response.toString() != "0 resultados"){

                    var respuesta = response.toString()
                    val jsonObj: JSONObject = JSONObject(respuesta)
                    val jsonArray: JSONArray = jsonObj.getJSONArray("categorias")
                    if (jsonArray.length() == 0) {
                        Aviso("Usuarios", "no se encuentran negocios")
                    } else {
                        var indice = 0


                        //-----------------------------Almacenamiento del array en variable, y busqueda en atributos del objeto
                        var categorias = ArrayList<CategoriaNegocio>()
                        for (i in 0 until jsonArray.length()) {
                            var categoriaa = CategoriaNegocio()
                            var datos = JSONObject(jsonArray.get(indice).toString())

                            categoriaa.idcategoria = datos.getString("idcategoria").toString()
                            categoriaa.NombreCategoria = datos.getString("NombreCategoria").toString()
                            categoriaa.ImagenCategoria = datos.getString("ImagenCategoria").toString()

                            categorias.add(categoriaa)
                            indice++
                        }

                        categorias.sortBy { it.NombreCategoria }

                        //--------------------------------------------Llenado del adaptador y recycler
                        listaCategoriasHome.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                        val miAdaptador = AdaptadorCategoriasHome(categorias)
                        listaCategoriasHome.adapter = miAdaptador


                        //--------------------------------------------Asignación del clic listener
                        miAdaptador.setOnItemClickListener(object :
                            AdaptadorCategoriasHome.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                var c = categorias.get(position).NombreCategoria


                                filtroCategoria.clear()
                                for (cat in homeNegocios){
                                    if( cat.CategoriaNegocio contentEquals c) {
                                        filtroCategoria.add(cat)
                                    }
                                }

                                if(!filtroCategoria.isEmpty()){
                                    Toast.makeText(requireActivity(),"Filtrado por: ${categorias.get(position).NombreCategoria}",
                                        Toast.LENGTH_SHORT).show()

                                 //armar adapter
                                 principalNegociosHome.clearFocus()
                                principalNegociosHome.layoutManager = LinearLayoutManager(activity)
                                var miAdaptador = AdaptadorNegociosHome(filtroCategoria)
                                principalNegociosHome.adapter = miAdaptador

                                //Metodo clic listener
                                    var usuario = arguments?.getParcelable<DatosUsuario>("infoPerfil")
                                       miAdaptador.setOnItemClickListener(object :
                                        AdaptadorNegociosHome.onItemClickListener{
                                        override fun onItemClick(position: Int) {
                                           Toast.makeText(requireActivity(), "Sleecionado: " +
                                                "${filtroCategoria.get(position).NombreNegocio}",
                                             Toast.LENGTH_SHORT).show()

                                            val negocioConsulta = Intent(requireActivity(), Negocio_consultado::class.java)
                                            negocioConsulta.putExtra("negocioConsultado", filtroCategoria.get(position))
                                            negocioConsulta.putExtra("usuarioDatos", usuario)
                                            startActivity(negocioConsulta)

                                    }
                                  })
                                }else{  Toast.makeText(requireActivity(),"no existen negocios de ${categorias.get(position).NombreCategoria}",
                                    Toast.LENGTH_SHORT).show()}


                            }
                        })

                    }
                }else{Aviso("Aviso", "no cuenta con productos registrados")}
            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }

                                     //-------------------------------------------------------- filtro  municipio
    fun filtrarMunicipio(mun: String)
    {
        filtroMunicipio.clear()
        for(col in homeNegocios){
            if(col.MunicipioNegocio contentEquals mun){
                filtroMunicipio.add(col)
            }
        }

        if(!filtroMunicipio.isEmpty()) {
            Toast.makeText(requireActivity(),"Negocios de ${mun}", Toast.LENGTH_SHORT).show()
            //armar adapter
            principalNegociosHome.clearFocus()
            principalNegociosHome.layoutManager = LinearLayoutManager(activity)
            var miAdaptador = AdaptadorNegociosHome(filtroMunicipio)
            principalNegociosHome.adapter = miAdaptador

            //Metodo clic listener
            var usuario = arguments?.getParcelable<DatosUsuario>("infoPerfil")
            miAdaptador.setOnItemClickListener(object :
                AdaptadorNegociosHome.onItemClickListener{
                override fun onItemClick(position: Int) {
                    Toast.makeText(requireActivity(), "Sleecionado: " +
                            "${filtroMunicipio.get(position).NombreNegocio}",
                        Toast.LENGTH_SHORT).show()

                    val negocioConsulta = Intent(requireActivity(), Negocio_consultado::class.java)
                    negocioConsulta.putExtra("negocioConsultado", filtroMunicipio.get(position))
                    negocioConsulta.putExtra("usuarioDatos", usuario)
                    startActivity(negocioConsulta)
                }
            })
        }else{
            Toast.makeText(requireActivity(),"De momento no hay negocios en ${mun}", Toast.LENGTH_SHORT).show()
        }
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