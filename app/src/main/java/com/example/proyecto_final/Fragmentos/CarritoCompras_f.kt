package com.example.proyecto_final.Fragmentos

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.Adaptadores.AdaptadorCarritoCompras
import com.example.proyecto_final.Adaptadores.AdaptadorComentarios
import com.example.proyecto_final.ObjetosParcelables.DatosCarrito
import com.example.proyecto_final.ObjetosParcelables.DatosCometario
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.example.proyecto_final.ObjetosParcelables.Productos
import com.example.proyecto_final.R
import com.example.proyecto_final.RealizarPedidos
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_administrar_negocio.*
import kotlinx.android.synthetic.main.activity_producto_seleccionado_cliente.*
import kotlinx.android.synthetic.main.activity_producto_seleccionado_cliente.view.*
import kotlinx.android.synthetic.main.fragment_carrito_compras_f.*
import kotlinx.android.synthetic.main.fragment_editar_borrar_productos_f.*
import kotlinx.android.synthetic.main.modal_respuesta_comentario.*
import kotlinx.android.synthetic.main.modal_respuesta_comentario.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CarritoCompras_f : Fragment() {
    var elementosCarrito = ArrayList<DatosCarrito>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carrito_compras_f, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //------------------------------------------------------------------- recibir la información del usuario
        var usuario = arguments?.getParcelable<DatosUsuario>("infoPerfil")

        listarElementosCarrito(usuario)


        VaciarCarrito.setOnClickListener {                       //desplegar un modal para vaciar el carrito.
            val dialogBuilder = AlertDialog.Builder(requireActivity())
            dialogBuilder.setMessage("¿Desea eliminar todos los elementos de su carrito?")
                .setCancelable(false)
                .setPositiveButton("No", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                }).setNegativeButton("Si", DialogInterface.OnClickListener{
                    dialog, id ->
                    VaciarElCarro(usuario)
                    dialog.cancel()
                })


            val alert = dialogBuilder.create()
            alert.setTitle("Aviso!")
            alert.show()
        }
    }
    fun listarElementosCarrito(usuario: DatosUsuario? ) {
        elementosCarrito.clear()
        val queue = Volley.newRequestQueue(activity)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/carrito_compras.php?comando=ElementosCarrito"+
                "&correo=${usuario?.CorreoUsuario}"

        val respuestaString = StringRequest(
            Request.Method.GET, url, { response ->
                //-----------------------------Obtención de un array json con los datos consultados
                Log.i("response", response.toString())

                var respuesta = response.toString()
                val jsonObj: JSONObject = JSONObject(respuesta)
                val jsonArray: JSONArray = jsonObj.getJSONArray("miCarrito")

                //------------------------------------- condición de cuando no existan  productos
                if (jsonArray.length() == 0) {
                    elementosCarrito.clear()
                    listaCarrito.layoutManager = LinearLayoutManager(activity)
                    var miAdapter = AdaptadorCarritoCompras(elementosCarrito)
                    listaCarrito.adapter = miAdapter
                    avisoCantidadElementos.setText("Numero de elementos: "+ elementosCarrito.size)
                    VaciarCarrito.visibility = View.GONE
                    Aviso("Aviso", "no se encontraron elementos en su carrito de compras")
                } else {

                    var indice = 0
                    //-----------------------------Almacenamiento del array en variable, y busqueda en atributos del objeto
                    for (i in 0 until jsonArray.length()) {
                        var elemento = DatosCarrito()
                        var datos = JSONObject(jsonArray.get(indice).toString())

                        elemento.idproductocarrito = datos.getString("idproductocarrito").toString()
                        elemento.correo_Cliente = datos.getString("correoCliente").toString()
                        elemento.id_producto = datos.getString("idproducto").toString()
                        elemento.Cantidad_Solicitada = datos.getString("CantidadSolicitada").toString()
                        elemento.id_negocio = datos.getString("idnegocio").toString()
                        elemento.correo_Vendedor = datos.getString("correoVendedor").toString()
                        elemento.Nombre_Negocio = datos.getString("NombreNegocio").toString()
                        elemento.Nombre_Producto = datos.getString("NombreProducto").toString()
                        elemento.Seccion_Producto = datos.getString("SeccionProducto").toString()
                        elemento.Existencia_Producto = datos.getString("ExistenciaProducto").toString()
                        elemento.Descripcion_Producto = datos.getString("DescripcionProducto").toString()
                        elemento.Tipo_Oferta = datos.getString("TipoOferta").toString()
                        elemento.Imagen_Producto = datos.getString("ImagenProducto").toString()
                        elemento.el_Precio = datos.getString("Precio").toString()
                        elemento.Precio_Envio = datos.getString("PrecioEnvio").toString()

                        elementosCarrito.add(elemento)
                        indice++
                    }
                    VaciarCarrito.visibility = View.VISIBLE

                    elementosCarrito.reverse()

                    listaCarrito.layoutManager = LinearLayoutManager(activity)
                    var miAdapter = AdaptadorCarritoCompras(elementosCarrito)
                    listaCarrito.adapter = miAdapter

                    avisoCantidadElementos.setText("Numero de elementos: "+ elementosCarrito.size)

                    //------------------------------------------Método Set ON clic listener
                    miAdapter.setOnItemClickListener(object :
                    AdaptadorCarritoCompras.onItemClickListener{
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onItemClick(position: Int) {
                           //despliegue del modal para editar y/o proceder a la compra.
                            eventos(elementosCarrito.get(position))
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

     @RequiresApi(Build.VERSION_CODES.O)
     fun eventos (elemento: DatosCarrito){
         var modal = AlertDialog.Builder(requireActivity())
         modal.setCancelable(true)
         var vista = layoutInflater.inflate(R.layout.activity_producto_seleccionado_cliente, null)

         vista.barraSuperiorModal.visibility = View.GONE

         Picasso.get().load(elemento.Imagen_Producto).into(vista.imagenProductoConsultado)
         vista.consultaCNombreProducto.text = elemento.Nombre_Producto
         vista.consultaCTipoOferta.text = elemento.Tipo_Oferta
         vista.consultaCnombreNegocio.text = elemento.Nombre_Negocio
         vista.consultaCSeccion.text = elemento.Seccion_Producto
         vista.consultaCPrecio.text = "$"+elemento.el_Precio
         vista.consultaCPrecioEnvio.text ="Precio de envío: "+ elemento.Precio_Envio
         vista.consultaCExistencias.text = elemento.Existencia_Producto
         vista.CantidadAVender.setText(elemento.Cantidad_Solicitada)


                            //------------------------------------------------- definir las estrellas
         llamarVotacion(elemento.id_producto.toInt(), vista)

                                                                                   //definir el estado del boton de los comentarios
         vista.desplegarComentarios.setBackgroundResource(R.drawable.icono_comentario)
         var visibilidadComentarios = false
         vista.cajaLosComentarios.visibility = View.GONE

         modal.setView(vista)
         var dialog = modal.create()
         dialog.show()

         var usuario = arguments?.getParcelable<DatosUsuario>("infoPerfil") //------importacion de los datos usuario

         //-------------------------------------------------------------------- incremento de la cantidad
         var cantidadVenta =  vista.CantidadAVender.text.toString().toInt()
         var existencias = elemento.Existencia_Producto.toInt()
         var precioTotal = elemento.el_Precio.toFloat()
         var precioEnvio = elemento.Precio_Envio.toFloat()
         var PrecioIndi = (precioTotal - precioEnvio)/cantidadVenta
         var pF = (PrecioIndi * cantidadVenta).toFloat()
         vista.incremento.setOnClickListener {
             if(cantidadVenta < existencias){
                 cantidadVenta++
                 pF = (PrecioIndi * cantidadVenta).toFloat()
                 vista.CantidadAVender.setText(cantidadVenta.toString())
                 vista.consultaCPrecio.setText("$"+pF)
             }
         }

                                     //------------------ disminuir la cantidad
         vista.decremento.setOnClickListener {
             if(cantidadVenta > 1){
                 cantidadVenta--
                 pF = (PrecioIndi * cantidadVenta).toFloat()
                 vista.CantidadAVender.setText(cantidadVenta.toString())
                 vista.consultaCPrecio.setText("$"+pF)
             }
         }

                                          //---------------------------------------------------------------------- desplgar comentarios
         vista.desplegarComentarios.setOnClickListener {
            visibilidadComentarios = !visibilidadComentarios
             if(visibilidadComentarios){
                 vista.desplegarComentarios.setBackgroundResource(R.drawable.icono_cerrar_comentarios)
                 vista.cajaLosComentarios.visibility = View.VISIBLE
             }
            else {
                 vista.desplegarComentarios.setBackgroundResource(R.drawable.icono_comentario)
                 vista.cajaLosComentarios.visibility = View.GONE
            }
         }
                                       //---------------------------------------------------------------------------- listar comentarios
         ListarComentarios(elemento.id_producto.toInt(), usuario, vista)
                                       //............................................................................ escribir comentario
        vista.comentarComentario.setOnClickListener {
           if(vista.cajaComentario.text.isNotEmpty()) {EscribirComentario(elemento.id_producto.toInt(), usuario, vista)}
        }

                                      //---------------------------ocultar modal y actualizar la cantidad
         vista.ocultarModal.setOnClickListener {
             elemento?.Cantidad_Solicitada = vista.CantidadAVender.text.toString()
             elemento?.el_Precio = (pF + precioEnvio).toString()

             carritoEdicionBorrado(elemento, "EditarCantidadSolicitada")
             dialog.hide()
         }
                                          //---------------------------------------Eliminación del producto
         vista.EliminarElemento.setOnClickListener {
             carritoEdicionBorrado(elemento, "BorrarElemento")
            dialog.hide()
         }
                                          //-------------------------------------- enviar la información a la pagina de venta
         vista.comprarProductoServicio.setOnClickListener {
             if(elemento?.Tipo_Oferta != "Venta de un servicio"){
                 if(vista.CantidadAVender.text.isNotEmpty()  && vista.CantidadAVender.text.toString() != "0"){

                     var cantidadUsuario = vista.CantidadAVender.text.toString().toInt()

                     pF = ((PrecioIndi * cantidadUsuario) + precioEnvio).toFloat()
                     var a = (existencias - cantidadUsuario).toInt()
                     enviarAConfirmacionVenta(usuario, elemento, a, pF, cantidadUsuario)

                     dialog.hide()
                 }
             }else{enviarAConfirmacionVenta(usuario, elemento, 1, pF, 1)}
         }
     }


    fun carritoEdicionBorrado(elemento: DatosCarrito, comando:String) {
        val queue = Volley.newRequestQueue(activity)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/carrito_compras.php?comando=${comando}"+
                "&correo=${elemento.correo_Cliente}&idproductocarrito=${elemento.idproductocarrito}&CantidadSolicitada=${elemento.Cantidad_Solicitada}" +
                "&precio=${elemento.el_Precio}&nombre=${elemento.Nombre_Producto}"


        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Producto","No se pudo editar!")

                } else {     //mensaje de respuesta al registrar

                              //filtro para no mostrar mensaje cuando solo se cambie el numero
                   if(strResp != "editado"){
                    val dialogBuilder = AlertDialog.Builder(requireActivity())
                    dialogBuilder.setMessage(strResp)
                        .setCancelable(false)
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        })

                    val alert = dialogBuilder.create()
                    alert.setTitle("Edición de Productos")
                    alert.show()
                    }

                    var usuario = arguments?.getParcelable<DatosUsuario>("infoPerfil") //------importacion de los datos usuario
                    listarElementosCarrito(usuario)
                }
            },

            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
    }

    fun VaciarElCarro(usuario: DatosUsuario?){
        val queue = Volley.newRequestQueue(activity)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/carrito_compras.php?comando=vaciarCarrito"+
                "&correo=${usuario?.CorreoUsuario}"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Producto","No se pudo editar!")

                } else {     //mensaje de respuesta al registrar

                        val dialogBuilder = AlertDialog.Builder(requireActivity())
                        dialogBuilder.setMessage(strResp)
                            .setCancelable(false)
                            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })

                        val alert = dialogBuilder.create()
                        alert.setTitle("Edición de Productos")
                        alert.show()

                    var usuario = arguments?.getParcelable<DatosUsuario>("infoPerfil") //------importacion de los datos usuario
                    listarElementosCarrito(usuario)
                }
            },
            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
    }

                                                //-------------------------------------------------- convertir el datos carrito a producto para enviarlo
    fun enviarAConfirmacionVenta(usuario: DatosUsuario?, convert: DatosCarrito, resta:Int, pF:Float, Existencia:Int){
        var miProducto = Productos()
            miProducto.idproducto = convert.id_producto
        miProducto.correoVendedor = convert.correo_Vendedor
        miProducto.idnegocio = convert.id_negocio
        miProducto.NombreNegocio = convert.Nombre_Negocio
        miProducto.Nombreproducto  = convert.Nombre_Producto
        miProducto.SeccionProducto = convert.Seccion_Producto
        miProducto.ExistenciaProducto = Existencia.toString()
        miProducto.DescripcionProducto = convert.Descripcion_Producto
        miProducto.TipoOferta = convert.Tipo_Oferta
        miProducto.ImagenProducto = convert.Imagen_Producto
        miProducto.Precio = pF.toString()
        miProducto.PrecioEnvio = miProducto.PrecioEnvio
        miProducto.Visibilidad = miProducto.Visibilidad

        val confirmarCompraxd = Intent(requireActivity(), RealizarPedidos::class.java)
          confirmarCompraxd.putExtra("confirmarCompraUsuario", usuario)
          confirmarCompraxd.putExtra("confirmarCompraProducto", miProducto)
          confirmarCompraxd.putExtra("actualizarCantidad", resta)
          startActivity(confirmarCompraxd)
    }

    fun ListarComentarios(idproducto: Int, usuario: DatosUsuario?, vista: View) {
        val queue = Volley.newRequestQueue(activity)
        val url: String =
            "https://registrosappinventor.000webhostapp.com/proyecto/comentarios.php?comando=ListarComentarios"+
                    "&idproducto=${idproducto}"

        val respuestaString = StringRequest(
            Request.Method.GET, url, { response ->
                //-----------------------------Obtención de un array json con los datos consultados
                Log.i("response", response.toString())

                var respuesta = response.toString()
                val jsonObj: JSONObject = JSONObject(respuesta)
                val jsonArray: JSONArray = jsonObj.getJSONArray("comentarios")
                if (jsonArray.length() > 0){
                    var indice = 0
                    //-----------------------------Almacenamiento del array en variable, y busqueda en atributos del objeto
                    var comentarioProducto= ArrayList<DatosCometario>()

                    for (i in 0 until jsonArray.length()) {
                        var coment = DatosCometario()
                        var datos = JSONObject(jsonArray.get(indice).toString())
                        coment.id_comentario = datos.getString("idcomentario").toString()
                        coment.id_comentario_respuesta = datos.getString("idcomentariorespuesta").toString()
                        coment.id_producto = datos.getString("idproducto").toString()
                        coment.Tipo_Comentario = datos.getString("TipoComentario").toString()
                        coment.Nombre_Usuario= datos.getString("NombreUsuario").toString()
                        coment.Nombre_Usuario_Respuesta = datos.getString("NombreUsuarioRespuesta").toString()
                        coment.Imagen_Usuario = datos.getString("ImagenUsuario").toString()
                        coment.Contenido = datos.getString("Contenido").toString()
                        coment.Fecha_Comentario = datos.getString("FechaComentario").toString()
                        comentarioProducto.add(coment)
                        indice++
                    }

                    //--------------------------------------------Llenado del adaptador y recycler
                    comentarioProducto.reverse()
                    vista.comentarios_ver.layoutManager = LinearLayoutManager(activity)
                    val miAdaptador = AdaptadorComentarios(comentarioProducto)
                    vista.comentarios_ver.adapter = miAdaptador


                    //--------------------------------------------Asignación del clic listener
                    miAdaptador.setOnItemClickListener(object :
                        AdaptadorComentarios.onItemClickListener {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onItemClick(position: Int) {
                           desplegarRespuestas(comentarioProducto.get(position), usuario, vista)
                        }
                    })
                }
            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun EscribirComentario(producto: Int, usuario: DatosUsuario?, vista: View){
        var dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        var fecha = LocalDateTime.now()
        var f = dtf.format(fecha).toString()

        var queue = Volley.newRequestQueue(activity)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/comentarios.php?comando=comentar"+
                "&idproducto=${producto}&TipoComentario=Comentario&NombreUsuario=${usuario?.NombreUsuario}"+
                "&ImagenUsuario=${usuario?.FotoUsuario}&Contenido=${vista.cajaComentario.text}&FechaComentario=${f}"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Usuarios","No se pudo registrar!")

                } else {     //mensaje de respuesta al registrar
                    vista.cajaComentario.setText("")
                    ListarComentarios(producto, usuario, vista)
                }
            },

            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
    }
                                                                      //---------------------------------------------------respuestas
                                                                      @RequiresApi(Build.VERSION_CODES.O)
                                                                      fun desplegarRespuestas(comentarioRespoder: DatosCometario, usuario: DatosUsuario?, vista: View) {
        var modal = AlertDialog.Builder(requireActivity())
        modal.setCancelable(true)

        var vista = layoutInflater.inflate(R.layout.modal_respuesta_comentario, null)
         ListarRespuesta(comentarioRespoder, vista)
        vista.comentarRespuesta.setOnClickListener {
            EscribirRespuesta(comentarioRespoder, usuario, vista.cajaRespuesta.text.toString(), vista)
        }

        vista.nombreComentarioRespuesta.text = "Respuestas de "+comentarioRespoder.Nombre_Usuario
        vista.nombreAlRespodido.text = comentarioRespoder.Nombre_Usuario


        modal.setView(vista)
        var dialog = modal.create()
        dialog.show()


        vista.cerrarRespuestas.setOnClickListener { dialog.hide() }
    }
   fun ListarRespuesta(comentarioRespoder: DatosCometario, vista: View){
        val queue = Volley.newRequestQueue(activity)
        val url: String =
            "https://registrosappinventor.000webhostapp.com/proyecto/comentarios.php?comando=ListarRespuestas" +
                    "&idcomentarioResponder=${comentarioRespoder.id_comentario}"

        val respuestaString = StringRequest(
            Request.Method.GET, url, { response ->
                //-----------------------------Obtención de un array json con los datos consultados
                Log.i("response", response.toString())

                var respuesta = response.toString()
                val jsonObj: JSONObject = JSONObject(respuesta)
                val jsonArray: JSONArray = jsonObj.getJSONArray("respuestas")
                if (jsonArray.length() > 0) {
                    var indice = 0
                    //-----------------------------Almacenamiento del array en variable, y busqueda en atributos del objeto
                    var comentarioProducto = ArrayList<DatosCometario>()

                    for (i in 0 until jsonArray.length()) {
                        var coment = DatosCometario()
                        var datos = JSONObject(jsonArray.get(indice).toString())
                        coment.id_comentario = datos.getString("idcomentario").toString()
                        coment.id_comentario_respuesta =
                            datos.getString("idcomentariorespuesta").toString()
                        coment.id_producto = datos.getString("idproducto").toString()
                        coment.Tipo_Comentario = datos.getString("TipoComentario").toString()
                        coment.Nombre_Usuario = datos.getString("NombreUsuario").toString()
                        coment.Nombre_Usuario_Respuesta =
                            datos.getString("NombreUsuarioRespuesta").toString()
                        coment.Imagen_Usuario = datos.getString("ImagenUsuario").toString()
                        coment.Contenido = datos.getString("Contenido").toString()
                        coment.Fecha_Comentario = datos.getString("FechaComentario").toString()
                        comentarioProducto.add(coment)
                        indice++
                    }

                    //--------------------------------------------Llenado del adaptador y recycler

                    vista.recyclerRespuestas.layoutManager = LinearLayoutManager(activity)
                    val miAdaptador = AdaptadorComentarios(comentarioProducto)
                    vista.recyclerRespuestas.adapter = miAdaptador


                    //--------------------------------------------Asignación del clic listener
                    miAdaptador.setOnItemClickListener(object :
                        AdaptadorComentarios.onItemClickListener {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onItemClick(position: Int) {
                            vista.nombreAlRespodido.text =
                                comentarioProducto.get(position).Nombre_Usuario
                        }
                    })
                }
            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun EscribirRespuesta(comentarioRespoder: DatosCometario, usuario: DatosUsuario?, contenido: String, vista: View){
        var dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        var fecha = LocalDateTime.now()
        var f = dtf.format(fecha).toString()

        var queue = Volley.newRequestQueue(requireActivity())
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/comentarios.php?comando=responder"+
                "&idcomentariorespuesta=${comentarioRespoder.id_comentario}"+
                "&NombreUsuarioRespuesta=${vista.nombreAlRespodido.text}&TipoComentario=Respuesta"+
                "&idproducto=0&NombreUsuario=${usuario?.NombreUsuario}"+
                "&ImagenUsuario=${usuario?.FotoUsuario}&Contenido=${contenido}&FechaComentario=${f}"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Usuarios","No se pudo registrar!")

                } else {     //mensaje de respuesta al registrar Aviso("aviso", strResp)
                    vista.cajaRespuesta.setText("")
                    ListarRespuesta(comentarioRespoder, vista)
                }
            },
            { Aviso("Red","Error de conexión")
            })
        queue.add(stringReq)
    }


    fun llamarVotacion(idproducto: Int, vista: View)
    {
        val queue = Volley.newRequestQueue(activity)
        var url: String = "https://registrosappinventor.000webhostapp.com/proyecto/votaciones.php?comando=promedioProducto"+
                "&idproducto=${idproducto}"

        val respuestaString = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonObj: JSONObject = JSONObject(response.toString())
                val jsonArray: JSONArray = jsonObj.getJSONArray("promedioP")

                var promedio = ArrayList<Float>()
                var indice = 0
                var prom = 0
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        var datos = JSONObject(jsonArray.get(indice).toString())
                        promedio.add(datos.getString("promedio").toFloat())

                        indice++
                    }
                    var a = Math.round(promedio.average())
                    prom = a.toInt()
                }

                when(prom){
                    0 -> {vista.e1.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        vista.e2.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        vista.e3.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        vista.e4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        vista.e5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

                    1 -> {vista.e1.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e2.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        vista.e3.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        vista.e4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        vista.e5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

                    2-> {vista.e1.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e2.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e3.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        vista.e4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        vista.e5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

                    3-> {vista.e1.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e2.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e3.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        vista.e5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

                    4-> {vista.e1.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e2.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e3.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e4.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

                    5->{vista.e1.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e2.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e3.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e4.setBackgroundResource(R.drawable.icono_estrella_votada)
                        vista.e5.setBackgroundResource(R.drawable.icono_estrella_votada)}
                }

            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }
}