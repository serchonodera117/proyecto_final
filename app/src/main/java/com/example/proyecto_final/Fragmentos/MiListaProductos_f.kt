package com.example.proyecto_final.Fragmentos

import android.content.DialogInterface
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.Adaptadores.AdaptadorCategoriasRegistro
import com.example.proyecto_final.Adaptadores.AdaptadorProductos
import com.example.proyecto_final.ObjetosParcelables.CategoriaNegocio
import com.example.proyecto_final.ObjetosParcelables.DatosNegocio
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.example.proyecto_final.ObjetosParcelables.Productos
import com.example.proyecto_final.R
import kotlinx.android.synthetic.main.activity_administrar_negocio.*
import kotlinx.android.synthetic.main.activity_registro_vendedor.*
import kotlinx.android.synthetic.main.fragment_mi_lista_productos_f.*
import org.json.JSONArray
import org.json.JSONObject


class MiListaProductos_f : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mi_lista_productos_f, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //------------------------------------------------------------------- recibimiento de datos
        var infoNegocio = arguments?.getParcelable<DatosNegocio>("negocioLisraProducto")
        var infoUsuario = arguments?.getParcelable<DatosUsuario>("usuarioVerCOm")

        listarMisProductos(infoNegocio, infoUsuario)
    }

    fun listarMisProductos(negocio: DatosNegocio?, usuario: DatosUsuario?)
    {
            val queue = Volley.newRequestQueue(activity)
            val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/crud_productos.php?comando=Listar"+
                    "&idnegocio=${negocio?.idnegocio}"

            val respuestaString = StringRequest(
                Request.Method.GET, url, { response ->
                    //-----------------------------Obtención de un array json con los datos consultados
                    Log.i("response",response.toString())

                        var respuesta = response.toString()
                        val jsonObj: JSONObject = JSONObject(respuesta)
                        val jsonArray: JSONArray = jsonObj.getJSONArray("misProductos")
                        if (jsonArray.length() == 0) {
                            Aviso("Aviso", "${negocio?.NombreNegocio} no cuenta con productos registrados")
                        } else {
                            var productosListados = ArrayList<Productos>()
                            var indice = 0

                            //-----------------------------Almacenamiento del array en variable, y busqueda en atributos del objeto
                            for (i in 0 until jsonArray.length()) {
                                var producto = Productos()
                                var datos = JSONObject(jsonArray.get(indice).toString())

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

                                productosListados.add(producto)
                                indice++
                            }
                                                                  // Ordenado alfabeticamente
                            productosListados.sortBy { it.Nombreproducto }

                              misProductos.layoutManager = GridLayoutManager(activity, 2)
                              var miAdaptador = AdaptadorProductos(productosListados)
                              misProductos.adapter = miAdaptador


                            //--------------------------------------------Asignación del clic listener
                                                             //----abrir la pagina de edición.
                            miAdaptador.setOnItemClickListener(object :
                                AdaptadorProductos.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    Toast.makeText(requireActivity(), "Seleccionado: ${productosListados.get(position).Nombreproducto}",
                                    Toast.LENGTH_SHORT).show()

                                    activity?.textoRecargar?.visibility = View.VISIBLE
                                    activity?.recargarLista?.visibility = View.VISIBLE
                                    activity?.formularioAgregarProducto?.visibility = View.GONE

                                    val edicion = EditarBorrarProductos_f()
                                    val transaccion = fragmentManager?.beginTransaction()

                                    var datos = Bundle()
                                        datos.putParcelable("productoEditar", productosListados.get(position))
                                        datos.putParcelable("usuarioEditProduct", usuario)
                                        edicion.arguments = datos
                                    transaccion?.replace(R.id.contenedorListaProductos, edicion)
                                    transaccion?.commit()
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