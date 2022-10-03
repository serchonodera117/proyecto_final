package com.example.proyecto_final

import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.ObjetosParcelables.DatosNegocio
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.example.proyecto_final.ObjetosParcelables.Pedidos
import com.example.proyecto_final.ObjetosParcelables.Productos
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_realizar_pedidos.*
import kotlinx.android.synthetic.main.fragment_agregar_producto_f.*
import kotlinx.android.synthetic.main.fragment_agregar_producto_f.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RealizarPedidos : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realizar_pedidos)
         var barra = supportActionBar
        barra?.hide()

        //----------------------------------------------------- recibir información
           var bunle = intent.extras
           var usuarioDatos = bunle?.getParcelable<DatosUsuario>("confirmarCompraUsuario")
           var productoInfo = bunle?.getParcelable<Productos>("confirmarCompraProducto")
           var cantidadAct = bunle?.getInt("actualizarCantidad")

        //-------------------------------------------------------- acoplar información
          compraNombreUsuario.setText(usuarioDatos?.NombreUsuario)
        compraMunicipioUsuario.setText(usuarioDatos?.MunicipioUsuario)
        compraDireccionusuario.setText(usuarioDatos?.DomicilioUsuario)
        compraNombreNegocio.setText("Negocio del producto: "+ productoInfo?.NombreNegocio)

        Picasso.get().load(productoInfo?.ImagenProducto).into(compraImagenProducto)
        compraNombreProducto.setText(productoInfo?.Nombreproducto)
        compraDescripcionProducto.setText(productoInfo?.DescripcionProducto)
        compraSeccionProducto.setText(productoInfo?.SeccionProducto)
        compraCantidadProducto.setText("Cantidad:"+ productoInfo?.ExistenciaProducto)
        compraPrecioFinal.setText(productoInfo?.Precio)

                   //sacar la fecha actual.
        var dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy \nHH:mm:ss")
         var fecha = LocalDateTime.now()
        var f = dtf.format(fecha).toString()

        compraFecha.setText(dtf.format(fecha))


        cometerCompra.setOnClickListener{
            comprar(usuarioDatos, productoInfo, f, cantidadAct?.toInt())
            guardarVotacion(productoInfo, usuarioDatos)
        }
        Volver_al_producto.setOnClickListener { finish() }
    }

    fun comprar(usuario: DatosUsuario?, producto: Productos?, fecha: String, updateCantidad: Int?){
        var queue = Volley.newRequestQueue(this)
        val url = "https://registrosappinventor.000webhostapp.com/proyecto/pedido.php?comando=realizarVenta&correoVendedor=${producto?.correoVendedor}"+
                "&idusuario=${usuario?.idusuario}&NombreUsuario=${usuario?.NombreUsuario}&MunicipioUsuario=${usuario?.MunicipioUsuario}&DireccionUsuario=${usuario?.DomicilioUsuario}"+
                "&idnegocio=${producto?.idnegocio}&NombreNegocio=${producto?.NombreNegocio}&idproducto=${producto?.idproducto}&NombreProducto=${producto?.Nombreproducto}"+
                "&DescripcionProducto=${producto?.DescripcionProducto}&SeccionProducto=${producto?.SeccionProducto}&ImagenProducto=${producto?.ImagenProducto}"+
                "&CantidadProducto=${producto?.ExistenciaProducto}&PrecioFinal=${producto?.Precio}&TipoPedido=${producto?.TipoOferta}&FechaPedido=${fecha}&restar=${updateCantidad}"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Producto","No se pudo realizar compra!")

                } else {     //mensaje de respuesta al registrar

                    val dialogBuilder = AlertDialog.Builder(this)
                    dialogBuilder.setMessage(strResp)
                        .setCancelable(false)
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                            finish()
                        })
                    val alert = dialogBuilder.create()
                    alert.setTitle("Aviso")
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

    fun guardarVotacion(pedido: Productos?, comprador: DatosUsuario?){
        val queue = Volley.newRequestQueue(this)

        var url: String = "https://registrosappinventor.000webhostapp.com/proyecto/votaciones.php?comando=votar"+
                "&idnegocio=${pedido?.idnegocio}&idproducto=${pedido?.idproducto}&correoPuntuador=${comprador?.CorreoUsuario}&Calificacion=0"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    //Aviso("Producto","No se pudo !")

                } else {     //mensaje de respuesta al registrar
                    //Aviso("aviso", strResp)
                }
            },
            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
    }
}