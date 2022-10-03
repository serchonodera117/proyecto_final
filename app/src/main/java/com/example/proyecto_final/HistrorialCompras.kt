package com.example.proyecto_final

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.Adaptadores.AdaptadorHistorial
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.example.proyecto_final.ObjetosParcelables.Pedidos
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_historial.*
import kotlinx.android.synthetic.main.activity_histrorial_compras.*
import kotlinx.android.synthetic.main.modal_historial_detalles.view.*
import org.json.JSONArray
import org.json.JSONObject

class HistrorialCompras : AppCompatActivity() {                                  //------------------------Historial cliente
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histrorial_compras)
        var bar = supportActionBar
        bar?.hide()
        //-----------------------------------------------------------obtener información
        var bunle = intent.extras
        var comprador = bunle?.getParcelable<DatosUsuario>("histrialVentas")

         generarHistorial(comprador)
        cerrarHistorialCompras.setOnClickListener { finish() }
    }

    fun generarHistorial(usuario: DatosUsuario?)
    {    val queue = Volley.newRequestQueue(this)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/pedido.php?comando=listarCliente"+
                "&idusuario=${usuario?.idusuario}"

        val respuestaString = StringRequest(
            Request.Method.GET, url, { response ->
                //-----------------------------Obtención de un array json con los datos consultados
                Log.i("response",response.toString())

                var respuesta = response.toString()
                val jsonObj: JSONObject = JSONObject(respuesta)
                val jsonArray: JSONArray = jsonObj.getJSONArray("pedidoscliente")
                if (jsonArray.length() == 0) {
                    Aviso("Aviso", "No cuenta con compras registradas")
                } else {
                    var misComprasListadas = ArrayList<Pedidos>()
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
                        ped.id_producto = datos.getString("idproducto").toString()
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


                        misComprasListadas.add(ped)
                        indice++
                    }
                    // Ordenado al reves
                    misComprasListadas.reverse()

                    comprasListadas_c.layoutManager = LinearLayoutManager(this)
                    var miAdaptador = AdaptadorHistorial(misComprasListadas)
                    comprasListadas_c.adapter = miAdaptador


                    //--------------------------------------------Asignación del clic listener
                    //----abrir la pagina de edición.
                    miAdaptador.setOnItemClickListener(object :
                        AdaptadorHistorial.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            eventos(misComprasListadas.get(position), usuario)
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

    fun eventos( venta: Pedidos, comprador: DatosUsuario?) {
        var modal = AlertDialog.Builder(this)
        modal.setCancelable(true)
        //encontrar el id/ruta
        var vista = layoutInflater.inflate(R.layout.modal_historial_detalles, null)

        //pasar vista a builder
        modal.setView(vista)
        //----------------------------------definir interfaz
        vista.detalle_NombreProducto.text = venta?.Nombre_Producto
        vista.detalle_NombreNegocio.text = venta?.Nombre_Negocio
        vista.detalle_NombreCliente.text = venta?.Nombre_Usuario
        Picasso.get().load(venta?.Imagen_Producto).into(vista.detalle_ImagenPedido)

        vista.detalle_DireccionUsuario.text = venta?.Direccion_Usuario
        vista.detalle_MunicipioUsuario.text = venta?.Municipio_Usuario
        vista.detalle_CantidadProducto.text = "Cantidad: " +  venta?.Cantidad_Producto
        vista.detalle_Descripcion.text = venta?.Descripcion_producto
        vista.detalle_FechaPedido.text = "Fecha: " + venta?.Fecha_Pedido
        vista.detalle_SeccionProducto.text = "Sección: " + venta?.Seccion_Producto
        vista.detalle_NombreProducto2.text = venta?.Nombre_Producto
        vista.detalle_Total.text = "Total: $" + venta?.Precio_Final

                                                          //----------------------- desaparecer los datos del vendedor (tamos en el cliente)
        vista.detalle_SprinnerEstatus.visibility = View.GONE
        vista.detallle_cometerEdicion.visibility = View.GONE
        vista.textodelestadoXd.visibility = View.GONE



                                                    //----------------------------setear las estrellas
        vista.v1.setBackgroundResource(R.drawable.icono_estrella_vacia)
        vista.v2.setBackgroundResource(R.drawable.icono_estrella_vacia)
        vista.v3.setBackgroundResource(R.drawable.icono_estrella_vacia)
        vista.v4.setBackgroundResource(R.drawable.icono_estrella_vacia)
        vista.v5.setBackgroundResource(R.drawable.icono_estrella_vacia)

        var puntos = 0


        vista.v1.setOnClickListener{puntos = 1; Votando(vista, puntos, venta, comprador)}
        vista.v2.setOnClickListener{puntos = 2; Votando(vista, puntos, venta, comprador)}
        vista.v3.setOnClickListener{puntos = 3; Votando(vista, puntos, venta, comprador)}
        vista.v4.setOnClickListener{puntos = 4; Votando(vista, puntos, venta, comprador)}
        vista.v5.setOnClickListener{puntos = 5; Votando(vista, puntos, venta, comprador)}

        if(venta.Nombre_Negocio == "Cancelado"){
            vista.textViewVotacion.visibility = View.GONE
            vista.puntuacion.visibility = View.GONE
            vista.puntuacion.visibility = View.GONE
            vista.cancelar_compra.visibility = View.GONE
        }

        //------------------------------ Llamar votacion
        llamarVotacion(comprador, venta, vista)


        var dialog = modal.create()
        dialog.show()


        vista.cancelar_deatalles.setOnClickListener {
            dialog.hide()
        }

        //-------------------------------------------------------------- cancelar compra
        vista.cancelar_compra.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("¿Desea confirmar la cancelación del pedido?")
                .setCancelable(false)
                .setPositiveButton("si", DialogInterface.OnClickListener {
                        modalConfirmacion, id -> modalConfirmacion.cancel()
                    dialog.hide()
                    cancelarCompra(venta, comprador)
                }).setNegativeButton("No", DialogInterface.OnClickListener{
                        modalConfirmacion, id -> modalConfirmacion.cancel()
                })
            val alert = dialogBuilder.create()
            alert.setTitle("Aviso")
            alert.show()

        }

    }
    fun llamarVotacion(comprador: DatosUsuario?, pedido: Pedidos, vista: View)
    {
        val queue = Volley.newRequestQueue(this)
        var url: String = "https://registrosappinventor.000webhostapp.com/proyecto/votaciones.php?comando=listarVotacion"+
                "&idpuntuacion=${pedido.id_pedido}&idproducto=${pedido.id_producto}&correo=${comprador?.CorreoUsuario}"

        val respuestaString = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonObj: JSONObject = JSONObject(response.toString())
                val jsonArray: JSONArray = jsonObj.getJSONArray("miPuntuacion")

                if (jsonArray.length() > 0) {
                    var puntos = 0

                        var datos = JSONObject(jsonArray.get(0).toString())
                            puntos = datos.getString("Calificacion").toString().toInt()
                            Votando(vista, puntos, pedido, comprador)

                }
            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }

    fun editarVotacion(pedido: Pedidos, comprador: DatosUsuario? , Calificacion: Int){
        val queue = Volley.newRequestQueue(this)

        var url: String = "https://registrosappinventor.000webhostapp.com/proyecto/votaciones.php?comando=EditarPuntuacion"+
                "&idnegocio=${pedido?.id_negocio}&idproducto=${pedido.id_producto}&correo=${comprador?.CorreoUsuario}&Calificacion=${Calificacion}"


        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    //Aviso("Producto","No se pudo editar!")

                } else {     //mensaje de respuesta al registrar
                    //Aviso("aviso", strResp)
                }
            },
            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
    }

    fun Votando(vista: View, puntos:Int, venta: Pedidos, comprador: DatosUsuario?){

        when(puntos){
            1 -> {vista.v1.setBackgroundResource(R.drawable.icono_estrella_votada)
                vista.v2.setBackgroundResource(R.drawable.icono_estrella_vacia)
                vista.v3.setBackgroundResource(R.drawable.icono_estrella_vacia)
                vista.v4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                vista.v5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

            2 -> {vista.v1.setBackgroundResource(R.drawable.icono_estrella_votada)
                  vista.v2.setBackgroundResource(R.drawable.icono_estrella_votada)
                vista.v3.setBackgroundResource(R.drawable.icono_estrella_vacia)
                vista.v4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                vista.v5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

            3 -> {vista.v1.setBackgroundResource(R.drawable.icono_estrella_votada)
                  vista.v2.setBackgroundResource(R.drawable.icono_estrella_votada)
                  vista.v3.setBackgroundResource(R.drawable.icono_estrella_votada)
                vista.v4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                vista.v5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

            4 -> {vista.v1.setBackgroundResource(R.drawable.icono_estrella_votada)
                 vista.v2.setBackgroundResource(R.drawable.icono_estrella_votada)
                 vista.v3.setBackgroundResource(R.drawable.icono_estrella_votada)
                 vista.v4.setBackgroundResource(R.drawable.icono_estrella_votada)
                vista.v5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

            5 -> {vista.v1.setBackgroundResource(R.drawable.icono_estrella_votada)
                vista.v2.setBackgroundResource(R.drawable.icono_estrella_votada)
                vista.v3.setBackgroundResource(R.drawable.icono_estrella_votada)
                vista.v4.setBackgroundResource(R.drawable.icono_estrella_votada)
                vista.v5.setBackgroundResource(R.drawable.icono_estrella_votada)}
        }
        editarVotacion(venta, comprador, puntos)
    }

    fun cancelarCompra(pedido: Pedidos, usuario: DatosUsuario?){
        val queue = Volley.newRequestQueue(this)
        var cancelled = "https://voyenuber.com/wp-content/uploads/2016/05/uber-cancelado2-300x200.jpg"


        var url: String = "https://registrosappinventor.000webhostapp.com/proyecto/CancelarVenta.php?comando=cancelarcompra&idpedido=${pedido.id_pedido}"+
                "&idproducto=${pedido.id_producto}&ImagenProducto=${cancelled}&NombreNegocio=Cancelado&correo=${usuario?.CorreoUsuario}" +
                "&CantidadCancelada=${pedido.Cantidad_Producto}"


        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Aviso","No se pudo cancelar!")

                } else {     //mensaje de respuesta al registrar
                    Aviso("Aviso", strResp)
                    generarHistorial(usuario)
                }
            },
            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
    }
}
