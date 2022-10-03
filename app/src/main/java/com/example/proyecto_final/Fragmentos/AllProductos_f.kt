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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.Adaptadores.AdaptadorProductos
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.example.proyecto_final.ObjetosParcelables.Productos
import com.example.proyecto_final.R
import com.example.proyecto_final.productoSeleccionadoCliente
import kotlinx.android.synthetic.main.activity_administrar_negocio.*
import kotlinx.android.synthetic.main.fragment_all_productos_f.*
import kotlinx.android.synthetic.main.fragment_mi_lista_productos_f.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class AllProductos_f : Fragment() {
var todosLosProductos = ArrayList<Productos>()
    var productosBuscados = ArrayList<Productos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_productos_f, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //----------------------------------------------------------------- listar
        listarProductos()

        //-------------------------------------------------------------------------------------buscar
        buscadorProductos.setOnQueryTextListener(object:
            androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               if(newText!!.isNotEmpty()){
                   productosBuscados.clear()
                   val busqueda = newText.toLowerCase(Locale.getDefault())

                   todosLosProductos.forEach{
                       if(it.Nombreproducto.toLowerCase(Locale.getDefault()).contains(busqueda)||
                               it.NombreNegocio.toLowerCase(Locale.getDefault()).contains(busqueda) ||
                               it.SeccionProducto.toLowerCase(Locale.getDefault()).contains(busqueda)){
                           productosBuscados.add(it)
                       }
                       arregloDelAdapter(productosBuscados)
                   }
               }else {arregloDelAdapter(todosLosProductos)}
                return true
            }

        })

    }

    fun listarProductos(){

        val queue = Volley.newRequestQueue(activity)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/listar.php?comando=ListarTodo"

        val respuestaString = StringRequest(
            Request.Method.GET, url, { response ->
                //-----------------------------Obtención de un array json con los datos consultados
                Log.i("response",response.toString())

                var respuesta = response.toString()
                val jsonObj: JSONObject = JSONObject(respuesta)
                val jsonArray: JSONArray = jsonObj.getJSONArray("todasOfertas")
                if (jsonArray.length() == 0) {
                    Aviso("Aviso", "no se encontraron productos registrados")
                } else {
                   todosLosProductos.clear()
                    var indice = 0

                    //-----------------------------Almacenamiento del array en variable, y busqueda en atributos del objeto
                    for (i in 0 until jsonArray.length()) {
                        var producto = Productos()
                        var datos = JSONObject(jsonArray.get(indice).toString())

                        var v =  datos.getString("Visibilidad").toString()

                        if(v == "Visible"){
                        producto.idproducto = datos.getString("idproducto").toString()
                        producto.correoVendedor = datos.getString("correoVendedor").toString()
                        producto.idnegocio = datos.getString("idnegocio").toString()
                        producto.NombreNegocio = datos.getString("NombreNegocio").toString()
                        producto.Nombreproducto = datos.getString("NombreProducto").toString()
                        producto.SeccionProducto = datos.getString("SeccionProducto").toString()
                        producto.ExistenciaProducto = datos.getString("ExistenciaProducto").toString()
                        producto.DescripcionProducto = datos.getString("DescripcionProducto").toString()
                        producto.TipoOferta = datos.getString("TipoOferta").toString()
                        producto.ImagenProducto = datos.getString("ImagenProducto").toString()
                        producto.Precio = datos.getString("Precio").toString()
                        producto.PrecioEnvio = datos.getString("PrecioEnvio").toString()
                        producto.Visibilidad = datos.getString("Visibilidad").toString()

                        todosLosProductos.add(producto)
                        }
                        indice++
                    }
                    // Ordenado alfabeticamente
                        todosLosProductos.reverse()
                                     //----------------funcion para acomodar el adaptador
                   arregloDelAdapter(todosLosProductos)

                }
            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }

    fun arregloDelAdapter(lista: ArrayList<Productos>) {
        productosTodos.layoutManager = GridLayoutManager(activity, 2)
        var miAdaptador = AdaptadorProductos(lista)
        productosTodos.adapter = miAdaptador


        //--------------------------------------------Asignación del clic listener
        //----abrir la pagina de edición.
        miAdaptador.setOnItemClickListener(object :
            AdaptadorProductos.onItemClickListener{
            override fun onItemClick(position: Int) {
                miCambio(lista.get(position))
            }
        })
    }
    fun miCambio (elemento: Productos) {
        Toast.makeText(requireActivity(), "seleccionado: ${elemento.Nombreproducto}", Toast.LENGTH_SHORT).show()

        //----------------------------------------------------------------- recibir información
        var usuario = arguments?.getParcelable<DatosUsuario>("infoPerfil")

        var selectedProducto = Intent(requireActivity(), productoSeleccionadoCliente::class.java)
        selectedProducto.putExtra("productoSeleccioandoCliente", elemento)
        selectedProducto.putExtra("usuarioVenta", usuario)
        startActivity(selectedProducto)
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