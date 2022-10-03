package com.example.proyecto_final

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.Adaptadores.AdaptadorHistorial
import com.example.proyecto_final.Adaptadores.AdaptadorProductos
import com.example.proyecto_final.Fragmentos.EditarBorrarProductos_f
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.example.proyecto_final.ObjetosParcelables.Pedidos
import com.example.proyecto_final.ObjetosParcelables.Productos
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_administrar_negocio.*
import kotlinx.android.synthetic.main.activity_agregar_negocio_nuevo.*
import kotlinx.android.synthetic.main.activity_historial.*
import kotlinx.android.synthetic.main.activity_registro_cliente.*
import kotlinx.android.synthetic.main.fragment_editar_borrar_productos_f.view.*
import kotlinx.android.synthetic.main.fragment_mi_lista_productos_f.*
import kotlinx.android.synthetic.main.modal_historial_detalles.view.*
import org.json.JSONArray
import org.json.JSONObject

class Historial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {                                    //------------- historial del vendedor
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)
        var bar = supportActionBar
        bar?.hide()

        //-----------------------------------------------------------obtener información
        var bunle = intent.extras
        var vendedor = bunle?.getParcelable<DatosUsuario>("histrialVentas")


        listarHistorial(vendedor, "listar")
        Volver_A_perfil.setOnClickListener { finish() }
    }

    fun listarHistorial(usuario: DatosUsuario?, directiva: String){
        val queue = Volley.newRequestQueue(this)
        var url: String = ""

        //uso de directiva para modificar el url y comunicar el api con su respectivo archivo/comando
        if(directiva == "listar"){
              url = "https://registrosappinventor.000webhostapp.com/proyecto/pedido.php?comando=listarVentaVendedor"+
                       "&correoVendedor=${usuario?.CorreoUsuario}"

           }else if(directiva == "modificar")
           {

           }
        val respuestaString = StringRequest(
            Request.Method.GET, url, { response ->
                //-----------------------------Obtención de un array json con los datos consultados
                Log.i("response",response.toString())

                var respuesta = response.toString()
                val jsonObj: JSONObject = JSONObject(respuesta)
                val jsonArray: JSONArray = jsonObj.getJSONArray("pedidosVendedor")
                if (jsonArray.length() == 0) {
                    Aviso("Aviso", "No cuenta con ventas registradas registrados")
                } else {
                    var misVentasListadas = ArrayList<Pedidos>()
                    var indice = 0

                    //-----------------------------Almacenamiento del array en variable, y busqueda en atributos del objeto
                    for (i in 0 until jsonArray.length()) {
                        var ped = Pedidos()
                        var datos = JSONObject(jsonArray.get(indice).toString())

                        ped.id_pedido = datos.getString("idpedido").toString()
                        ped.Correo_Vendedor = datos.getString("correoVendedor").toString()
                        ped.id_usuario = datos.getString("idusuario").toString()
                        ped.Nombre_Usuario = datos.getString("NombreUsuario").toString()
                        ped.Municipio_Usuario = datos.getString("MunicipioUsuario").toString()
                        ped.Direccion_Usuario = datos.getString("DireccionUsuario").toString()
                        ped.id_negocio = datos.getString("idnegocio").toString()
                        ped.Nombre_Negocio = datos.getString("NombreNegocio").toString()
                        ped.Nombre_Producto = datos.getString("NombreProducto").toString()
                        ped.Nombre_Producto = datos.getString("NombreProducto").toString()
                        ped.Descripcion_producto = datos.getString("DescripcionProducto").toString()
                        ped.Seccion_Producto = datos.getString("SeccionProducto").toString()
                        ped.Imagen_Producto = datos.getString("ImagenProducto").toString()
                        ped.Cantidad_Producto = datos.getString("CantidadProducto").toString()
                        ped.Precio_Final = datos.getString("PrecioFinal").toString()
                        ped.Tipo_Pedido  = datos.getString("TipoPedido").toString()
                        ped.Estado_Producto = datos.getString("EstadoProducto").toString()
                        ped.Estado_Servicio = datos.getString("EstadoServicio").toString()
                        ped.Fecha_Pedido = datos.getString("FechaPedido").toString()


                        misVentasListadas.add(ped)
                        indice++
                    }
                    // Ordenado al reves
                    misVentasListadas.reverse()

                    ventasListadas.layoutManager = LinearLayoutManager(this)
                    var miAdaptador = AdaptadorHistorial(misVentasListadas)
                    ventasListadas.adapter = miAdaptador


                    //--------------------------------------------Asignación del clic listener
                    //----abrir la pagina de edición.
                    miAdaptador.setOnItemClickListener(object :
                        AdaptadorHistorial.onItemClickListener{
                        override fun onItemClick(position: Int) {
                             miModal(misVentasListadas.get(position))
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
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(Mensaje)
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle(Titulo)
        alert.show()
    }


    fun miModal(detalle: Pedidos) {
        var modal = AlertDialog.Builder(this)
        modal.setCancelable(true)
        //encontrar el id/ruta
        var vista = layoutInflater.inflate(R.layout.modal_historial_detalles, null)

         //pasar vista a builder
           modal.setView(vista)
        //----------------------------------definir interfaz
        vista.detalle_NombreProducto.text = detalle?.Nombre_Producto
        vista.detalle_NombreNegocio.text = detalle?.Nombre_Negocio
        vista.detalle_NombreCliente.text = detalle?.Nombre_Usuario
        Picasso.get().load(detalle?.Imagen_Producto).into(vista.detalle_ImagenPedido)

        vista.detalle_DireccionUsuario.text = detalle?.Direccion_Usuario
        vista.detalle_MunicipioUsuario.text = detalle?.Municipio_Usuario
        vista.detalle_CantidadProducto.text = "Cantidad: " +  detalle?.Cantidad_Producto
        vista.detalle_Descripcion.text = detalle?.Descripcion_producto
        vista.detalle_FechaPedido.text = "Fecha: " + detalle?.Fecha_Pedido
        vista.detalle_SeccionProducto.text = "Sección: " + detalle?.Seccion_Producto
        vista.detalle_NombreProducto2.text = detalle?.Nombre_Producto
        vista.detalle_Total.text = "Total: $" + detalle?.Precio_Final

        vista.cancelar_compra.visibility = View.GONE


        //--------------------------------------- Adaptar spiner
        var estaosPedido: Array<String> = arrayOf()
        var indice = 0
        var elEstado: String
        if(detalle?.Tipo_Pedido == "Venta de un producto") {
            elEstado = detalle?.Estado_Producto
            estaosPedido = arrayOf("En preparación", "En camino", "Entregado")
        } else{
            elEstado = detalle?.Estado_Servicio
            estaosPedido = arrayOf("Realizándose", "Terminado")
        }

       when(elEstado){
           "En preparación" -> indice = 0
           "En camino" -> indice = 1
           "Entregado" -> indice = 2
           "Realizándose" -> indice = 0
           "Terminado" -> indice = 1
       }
                                            //-------------------------------------------------------------------------spiner
        var adaptador = ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, estaosPedido)
        vista.detalle_SprinnerEstatus.adapter = adaptador
        vista.detalle_SprinnerEstatus.setSelection(indice)

        var dialog = modal.create()
        dialog.show()

                                           //-------------------------------------------- esconder votaciones
        vista.textViewVotacion.visibility = View.GONE
        vista.puntuacion.visibility = View.GONE

        vista.cancelar_deatalles.setOnClickListener { dialog.hide() }

        if(detalle.Nombre_Negocio == "Cancelado"){
            vista.textodelestadoXd.visibility = View.GONE
          vista.detalle_SprinnerEstatus.visibility = View.GONE
            vista.detallle_cometerEdicion.visibility = View.GONE
        }
        //--------------------------------------------------editar y refrescar de paso
        vista.detallle_cometerEdicion.setOnClickListener{
            editarEstatus(detalle, vista.detalle_SprinnerEstatus.selectedItem.toString())

            var bunle = intent.extras
            var vendedor = bunle?.getParcelable<DatosUsuario>("histrialVentas")
            listarHistorial(vendedor, "listar")
            dialog.hide()
        }

    }

    fun editarEstatus(idPedidos: Pedidos?, estatus:String)
    {
        val queue = Volley.newRequestQueue(this)
        var url: String = "https://registrosappinventor.000webhostapp.com/proyecto/pedido.php?comando=EditarEstatus"+
                    "&idpedido=${idPedidos?.id_pedido}&Estatus=${estatus}"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Producto","No se pudo editar!")

                } else {     //mensaje de respuesta al registrar
                 Toast.makeText(this, "${strResp}", Toast.LENGTH_SHORT).show()
                }
            },
            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
    }
}