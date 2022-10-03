package com.example.proyecto_final

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.Adaptadores.AdaptadorCategoriasRegistro
import com.example.proyecto_final.Adaptadores.AdaptadorComentarios
import com.example.proyecto_final.ObjetosParcelables.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_producto_seleccionado_cliente.*
import kotlinx.android.synthetic.main.activity_producto_seleccionado_cliente.view.*
import kotlinx.android.synthetic.main.activity_registro_cliente.*
import kotlinx.android.synthetic.main.activity_registro_vendedor.*
import kotlinx.android.synthetic.main.fragment_agregar_producto_f.*
import kotlinx.android.synthetic.main.modal_respuesta_comentario.*
import kotlinx.android.synthetic.main.modal_respuesta_comentario.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class productoSeleccionadoCliente : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto_seleccionado_cliente)

        var barra = supportActionBar
           barra?.hide()

        cerrarModal.visibility = View.GONE
        cajaLosComentarios.visibility = View.GONE
        //------------------------------------------------------------------------------recibir información
        var bunle = intent.extras
        var elProducto = bunle?.getParcelable<Productos>("productoSeleccioandoCliente")
        var elUsuario = bunle?.getParcelable<DatosUsuario>("usuarioVenta")

        //----------------------------------------------------------------------- desplegar comentarios
        var visibilidadComentarios = false
        desplegarComentarios.setBackgroundResource(R.drawable.icono_comentario)

        desplegarComentarios.setOnClickListener{
            visibilidadComentarios = !visibilidadComentarios
            if (visibilidadComentarios){
                 cajaLosComentarios.visibility = View.VISIBLE
                 desplegarComentarios.setBackgroundResource(R.drawable.icono_cerrar_comentarios)
                ListarComentarios(elProducto, elUsuario)

            }
            if(!visibilidadComentarios){
                cajaLosComentarios.visibility = View.GONE
                desplegarComentarios.setBackgroundResource(R.drawable.icono_comentario)
            }
        }
        //------------------------------------------------------------------------- asignar datos a la interfaz
        Picasso.get().load(elProducto?.ImagenProducto).into(imagenProductoConsultado)
        consultaCNombreProducto.setText(elProducto?.Nombreproducto)
        consultaCnombreNegocio.setText(elProducto?.Nombreproducto)
        consultaCTipoOferta.setText(elProducto?.TipoOferta)
        consultaCnombreNegocio.setText(elProducto?.NombreNegocio)
        consultaCSeccion.setText(elProducto?.SeccionProducto)
        consultaCExistencias.setText(elProducto?.ExistenciaProducto)

        consultaCPrecioEnvio.setText("Precio de envío: $ "+ elProducto?.PrecioEnvio)

        //-------------------------------------------------------------------- asignación de las votaciones
        llamarVotacion(elProducto)


        if(elProducto?.TipoOferta == "Venta de un servicio" )
        {
            cantidadProducto_variable.visibility = View.GONE
            disponibilidadProducto.visibility = View.GONE
        }

               //-------- algoritmo de incremento decremento de cantidad
        consultaCPrecio.setText("$ "+ elProducto?.Precio)
        var cantidad = CantidadAVender.text.toString().toInt()
        var precio = elProducto?.Precio?.toFloat()
        var precioEnvio = elProducto?.PrecioEnvio?.toFloat()
        var precioFinal = (cantidad * precio!!).toFloat()
        var existencias = elProducto?.ExistenciaProducto?.toInt()
        var cantidadActualizable = consultaCExistencias.text.toString().toInt()

        decremento.setOnClickListener{
            if(cantidad > 1) {
                cantidad--
                cantidadActualizable++
                CantidadAVender.setText(cantidad.toString())
                precioFinal = (cantidad * precio!!).toFloat()
                consultaCPrecio.setText("$ "+ precioFinal)
                consultaCExistencias.setText(cantidadActualizable.toString())
            }
        }
        incremento.setOnClickListener {
            if(cantidad < existencias!!){
            cantidad++
            cantidadActualizable--
            CantidadAVender.setText(cantidad.toString())
            precioFinal = (cantidad * precio!!).toFloat()
            consultaCPrecio.setText("$ "+ precioFinal)
            consultaCExistencias.setText(cantidadActualizable.toString())
            }
        }

        comprarProductoServicio.setOnClickListener {
            if(elProducto?.TipoOferta != "Venta de un servicio"){
                if(CantidadAVender.text.isNotEmpty()  && CantidadAVender.text.toString() != "0"){
                                                                //asignar el precio final
                      var cantidadUsuario = CantidadAVender.text.toString().toInt()
                      var pF = (precio!! * cantidadUsuario)

                      if(cantidadUsuario < existencias!!) {
                                                            //modificar las existencias y  tratarlas como la cantidad compradas
                        elProducto?.ExistenciaProducto = cantidadUsuario.toString()
                        elProducto?.Precio = (pF + precioEnvio!!).toString()
                                                            //disminuir existencias y al comprar... actializar esa reducción.
                        var a =  (cantidadActualizable - cantidadUsuario).toInt()
                        ConfirmarCompra(elUsuario,elProducto, a)
                     }else{
                Toast.makeText(this, "La catidad seleccionada no coincide con la cantidad disponible.", Toast.LENGTH_SHORT).show()
                  }
            }
           }else{ConfirmarCompra(elUsuario,elProducto, 1)}
        }

        //---------------------------------------------------------------------------- Agregar al carro
        agregarAlCarrito.setOnClickListener {
              var cantidadSolicitada =   CantidadAVender.text.toString().toInt()
              elProducto?.Precio = (precioFinal + precioEnvio!!).toString()
              mandaAlCarro(elUsuario, elProducto, cantidadSolicitada)
        }

        simplementeVolver.setOnClickListener { finish() }

        //--------------------------------------------------------------------------------------------------------------Publicar comentario
        comentarComentario.setOnClickListener {
            if(cajaComentario.text.isNotEmpty())EscribirComentario(elProducto, elUsuario, "comentar")
        }
    }
     fun mandaAlCarro(usuario: DatosUsuario?, producto: Productos?, cantidadSolicitada:Int){
         var queue = Volley.newRequestQueue(this)
         val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/carrito_compras.php?comando=agregarAlCarro"+
                 "&correoCliente=${usuario?.CorreoUsuario}&idproducto=${producto?.idproducto}&CantidadSolicitada=${cantidadSolicitada}"+
                 "&idnegocio=${producto?.idnegocio}&correoVendedor=${producto?.correoVendedor}&NombreNegocio=${producto?.NombreNegocio}&NombreProducto=${producto?.Nombreproducto}" +
                 "&SeccionProducto=${producto?.SeccionProducto}&ExistenciaProducto=${consultaCExistencias.text}" +
                 "&TipoOferta=${producto?.TipoOferta}&ImagenProducto=${producto?.ImagenProducto}&Precio=${producto?.Precio}" +
                 "&PrecioEnvio=${producto?.PrecioEnvio}&DescripcionProducto=${producto?.DescripcionProducto}"

         val stringReq = StringRequest(
             Request.Method.GET, url,
             { response ->
                 var strResp = response.toString()
                 if (strResp == "") {
                     Aviso("Producto","No se pudo registrar!")

                 } else {     //mensaje de respuesta al registrar

                     val dialogBuilder = AlertDialog.Builder(this)
                     dialogBuilder.setMessage(strResp)
                         .setCancelable(false)
                         .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                             dialog.cancel()
                         })

                     val alert = dialogBuilder.create()
                     alert.setTitle("Registro de Productos")
                     alert.show()
                 }
             },

             {
                 Aviso("Red","Error de conexión")

             })
         queue.add(stringReq)
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

    fun ConfirmarCompra(usuario: DatosUsuario?, productos: Productos?, resta: Int){
        val confirmarCompraxd = Intent(this, RealizarPedidos::class.java)
        confirmarCompraxd.putExtra("confirmarCompraUsuario", usuario)
        confirmarCompraxd.putExtra("confirmarCompraProducto", productos)
        confirmarCompraxd.putExtra("actualizarCantidad", resta)
        startActivity(confirmarCompraxd)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun EscribirComentario(productos: Productos?, usuario: DatosUsuario?, comando:String){
          var dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
          var fecha = LocalDateTime.now()
          var f = dtf.format(fecha).toString()

        var queue = Volley.newRequestQueue(this)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/comentarios.php?comando=comentar"+
                "&idproducto=${productos?.idproducto}&TipoComentario=Comentario&NombreUsuario=${usuario?.NombreUsuario}"+
                "&ImagenUsuario=${usuario?.FotoUsuario}&Contenido=${cajaComentario.text}&FechaComentario=${f}"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Usuarios","No se pudo registrar!")

                } else {     //mensaje de respuesta al registrar
                    cajaComentario.setText("")
                    ListarComentarios(productos, usuario)
                }
            },

            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
    }

    fun ListarComentarios(productos: Productos?, usuario: DatosUsuario?) {
        val queue = Volley.newRequestQueue(this)
        val url: String =
            "https://registrosappinventor.000webhostapp.com/proyecto/comentarios.php?comando=ListarComentarios"+
                    "&idproducto=${productos?.idproducto}"

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
                        comentarios_ver.layoutManager = LinearLayoutManager(this)
                        val miAdaptador = AdaptadorComentarios(comentarioProducto)
                        comentarios_ver.adapter = miAdaptador


                        //--------------------------------------------Asignación del clic listener
                        miAdaptador.setOnItemClickListener(object :
                            AdaptadorComentarios.onItemClickListener {
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onItemClick(position: Int) {
                                desplegarModal(comentarioProducto.get(position), usuario)
                            }
                        })
                    }
            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }

    @RequiresApi(Build.VERSION_CODES.O)                                         //-----------------------abrir modal de la respuesta
    fun desplegarModal(comentarioRespoder: DatosCometario, usuario: DatosUsuario?) {
        var modal = AlertDialog.Builder(this)
        modal.setCancelable(true)

        var vista = layoutInflater.inflate(R.layout.modal_respuesta_comentario, null)
        ListarRespuesta(comentarioRespoder, vista)

        vista.nombreComentarioRespuesta.text = comentarioRespoder.Nombre_Usuario
        vista.nombreAlRespodido.text = comentarioRespoder.Nombre_Usuario


        modal.setView(vista)
        var dialog = modal.create()
        dialog.show()

        vista.comentarRespuesta.setOnClickListener {
            llenarRespuestas(comentarioRespoder, usuario, vista.cajaRespuesta.text.toString(), vista)
            vista.cajaRespuesta.setText("")
        }

        vista.cerrarRespuestas.setOnClickListener { dialog.hide() }
    }

    @RequiresApi(Build.VERSION_CODES.O)                                                                //------------------- enviar comentario
    fun llenarRespuestas(comentarioRespoder: DatosCometario, usuario: DatosUsuario?, contenido: String, vista: View){
            var dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            var fecha = LocalDateTime.now()
            var f = dtf.format(fecha).toString()

            var queue = Volley.newRequestQueue(this)
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
                                                               //----------------------------------------------------listar las respuestas
    fun ListarRespuesta(comentarioRespoder: DatosCometario, vista: View) {
        val queue = Volley.newRequestQueue(this)
        val url: String =
            "https://registrosappinventor.000webhostapp.com/proyecto/comentarios.php?comando=ListarRespuestas"+
                    "&idcomentarioResponder=${comentarioRespoder.id_comentario}"

        val respuestaString = StringRequest(
            Request.Method.GET, url, { response ->
                //-----------------------------Obtención de un array json con los datos consultados
                Log.i("response", response.toString())

                var respuesta = response.toString()
                val jsonObj: JSONObject = JSONObject(respuesta)
                val jsonArray: JSONArray = jsonObj.getJSONArray("respuestas")
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

                    vista.recyclerRespuestas.layoutManager = LinearLayoutManager(this)
                    val miAdaptador = AdaptadorComentarios(comentarioProducto)
                    vista.recyclerRespuestas.adapter = miAdaptador


                    //--------------------------------------------Asignación del clic listener
                    miAdaptador.setOnItemClickListener(object :
                        AdaptadorComentarios.onItemClickListener {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onItemClick(position: Int) {
                            vista.nombreAlRespodido.text =  comentarioProducto.get(position).Nombre_Usuario
                        }
                    })
                }
            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }

    fun llamarVotacion(productos: Productos?)
    {
        val queue = Volley.newRequestQueue(this)
        var url: String = "https://registrosappinventor.000webhostapp.com/proyecto/votaciones.php?comando=promedioProducto"+
                "&idproducto=${productos?.idproducto}"

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
                   0 -> {e1.setBackgroundResource(R.drawable.icono_estrella_vacia)
                         e2.setBackgroundResource(R.drawable.icono_estrella_vacia)
                         e3.setBackgroundResource(R.drawable.icono_estrella_vacia)
                         e4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                         e5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

                    1 -> {e1.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e2.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        e3.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        e4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        e5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

                    2-> {e1.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e2.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e3.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        e4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        e5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

                    3-> {e1.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e2.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e3.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e4.setBackgroundResource(R.drawable.icono_estrella_vacia)
                        e5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

                    4-> {e1.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e2.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e3.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e4.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e5.setBackgroundResource(R.drawable.icono_estrella_vacia)}

                    5->{e1.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e2.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e3.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e4.setBackgroundResource(R.drawable.icono_estrella_votada)
                        e5.setBackgroundResource(R.drawable.icono_estrella_votada)}
                }

            },
            {
                Aviso("Red", "No se pudo conectar a la base")
            })
        queue.add(respuestaString)
    }

}