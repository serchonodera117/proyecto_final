package com.example.proyecto_final

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.URLUtil.decode
import android.widget.ArrayAdapter
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.Adaptadores.AdaptadorCategoriasRegistro
import com.example.proyecto_final.ObjetosParcelables.CategoriaNegocio
import kotlinx.android.synthetic.main.activity_registro_cliente.*
import kotlinx.android.synthetic.main.activity_registro_vendedor.*
import kotlinx.android.synthetic.main.estilo_categorias_registro.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONStringer
import java.io.ByteArrayInputStream
import java.lang.Byte.decode
import java.lang.Integer.decode
import java.net.URLDecoder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class RegistroVendedor : AppCompatActivity() {
    //------------------------------------------------------------------------Lista de categorias
    var misCategorias = ArrayList<CategoriaNegocio>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_vendedor)

        var barraSuperior = supportActionBar
        barraSuperior?.hide()
        contenedorCategoria.visibility = View.GONE


        //------------------------------------------------------------------------Cargar Spiner de municipios
        var municipios = arrayOf("Colima", "Villa de Alvarez")
        var adaptador = ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, municipios)
        MunicipioVendedor.adapter = adaptador
        MunicipioNegocio.adapter = adaptador

        //------------------------------------------------------------------------Cargar recycler de categorias
        listarCategoria(misCategorias)
        //------------------------------------------------------------------------Detección de elección de imagenes
        var apertura1 = false
        var apertura2 = false


        //------------------------------------------------------------------------Hacer visible el contenedor para agregar categoria
        AgregarCategoria.setOnClickListener { contenedorCategoria.visibility = View.VISIBLE }
        CerrarCategoria.setOnClickListener { contenedorCategoria.visibility = View.GONE }

        Volver_v.setOnClickListener { finish() }


        //------------------------------------------------------------------------ Registrar categoría
         GuardarCategoria.setOnClickListener {
             if(!urlImagenCategoria.text.isEmpty() && !NombreCategoria.text.isEmpty()) {
                 RegistrarCategoría()
                 listarCategoria(misCategorias)

                 NombreCategoria.setText("")
                 urlImagenCategoria.setText("")
                 contenedorCategoria.visibility = View.GONE
             }else{
                 Toast.makeText(this, "no deben de estar vacíos ni la imagen ni la caja", Toast.LENGTH_SHORT).show()
             }
         }
        //------------------------------------------------------------------------ Registrar el usuario con su negocio
        RegistrarVendedor.setOnClickListener {
            if(!NombreVendedor.text.isEmpty() && !CorreoVendedor.text.isEmpty() && !ContrasenaVendedor.text.isEmpty() && !NombreNegocio.text.isEmpty()
                && CategoriaSeleccionada.text != "- - -"){
            RegistrarVendedor()
            }else{
                Toast.makeText(this, "Se neceita tener todos los campos completos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //------------------------------------------------------------------------ Registrar categoría en la base
    fun RegistrarCategoría(){

        var queue = Volley.newRequestQueue(this)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/agregar_categoria.php?codigo=Agregar&NombreCategoria=${NombreCategoria.text}"+
                "&ImagenCategoria=${urlImagenCategoria.text}"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Categorias","No se pudo agregar la categoria")

                } else {     //mensaje de respuesta al registrar

                    val dialogBuilder = AlertDialog.Builder(this)
                    dialogBuilder.setMessage(strResp)
                        .setCancelable(false)
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                           dialog.cancel()
                        })

                    val alert = dialogBuilder.create()
                    alert.setTitle("Registro de categoria")
                    alert.show()

                }
            },

            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
    }

    //------------------------------------------------------------------------ Listar mis categorias
    @RequiresApi(Build.VERSION_CODES.O)
    fun listarCategoria(categorias: ArrayList<CategoriaNegocio>)
    {
        val queue = Volley.newRequestQueue(this)
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
                       categorias.clear()
                        for (i in 0 until jsonArray.length()) {
                            var categoriaa = CategoriaNegocio()
                            var datos = JSONObject(jsonArray.get(indice).toString())

                            categoriaa.idcategoria = datos.getString("idcategoria").toString()
                            categoriaa.NombreCategoria = datos.getString("NombreCategoria").toString()
                             categoriaa.ImagenCategoria = datos.getString("ImagenCategoria").toString()

                            categorias.add(categoriaa)
                            indice++
                        }


                                           //--------------------------------------------Llenado del adaptador y recycler
                        CategoriasExistentes.layoutManager = LinearLayoutManager(this)
                        val miAdaptador = AdaptadorCategoriasRegistro(categorias)
                        CategoriasExistentes.adapter = miAdaptador


                    //--------------------------------------------Asignación del clic listener
                       miAdaptador.setOnItemClickListener(object :
                            AdaptadorCategoriasRegistro.onItemClickListener{
                           override fun onItemClick(position: Int) {
                             CategoriaSeleccionada.setText(categorias.get(position).NombreCategoria)
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


    fun RegistrarVendedor(){
        var pago_tarjeta:String = "No aplica"
        var pago_oxxo:String = "No aplica"
        var pago_efectivo:String = "No aplica"
        var oferta_servicio = "No aplica"
        var oferta_producto = "No aplica"
        if(TarjetaSelect.isChecked){pago_tarjeta = "Pago con tarjeta"}
        if(OxxoSelect.isChecked){pago_oxxo = "Pago en oxxo"}
        if(EfectivoSelect.isChecked){pago_efectivo = "Pago en efectivo"}
        if(OfertaServicio.isChecked){oferta_servicio = "Venta de servicios"}
        if(OfertaProducto.isChecked){oferta_producto = "Venta de productos"}


        var queue = Volley.newRequestQueue(this)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/registro_usuarios.php?TipoUsuario=Vendedor"+
                "&NombreUsuario=${NombreVendedor.text}&FotoUsuario=${urlFotoVendedor.text}&MunicipioUsuario=${MunicipioVendedor.selectedItem.toString()}" +
                "&ColoniaUsuario=${ColoniaVendedor.text}&DomicilioUsuario=${DomicilioVendedor.text}&CorreoUsuario=${CorreoVendedor.text}" +
                "&ContrasenaUsuario=${ContrasenaVendedor.text}&TelefonoUsuario=${TelefonoVendedor.text}&VerificacionUsuario=${1}"+
                "&NombreNegocio=${NombreNegocio.text}&LogoNegocio=${urlImagenNegocio.text}&Efectivo=${pago_efectivo}&Tarjeta=${pago_tarjeta}&PagoOxxo=${pago_oxxo}" +
                "&OfertaProducto=${oferta_producto}&OfertaServicio=${oferta_servicio}&CategoriaNegocio=${CategoriaSeleccionada.text}&MunicipioNegocio=${MunicipioNegocio.selectedItem.toString()}" +
                "&ColoniaNegocio=${ColoniaNegocio.text}&DireccionNegocio=${DireccionNegocio.text}"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Usuarios","No se pudo registrar!")

                } else {     //mensaje de respuesta al registrar

                    val dialogBuilder = AlertDialog.Builder(this)
                    dialogBuilder.setMessage(strResp)
                        .setCancelable(false)
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                           finish()
                        })

                    val alert = dialogBuilder.create()
                    alert.setTitle("Registro de usuarios")
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



    /*
    @RequiresApi(Build.VERSION_CODES.O)//----------decodificar string bitmap pero nunca funcionó
    fun getBitmapFromString(fotoString: String)
    {
        var charset = Charsets.UTF_8
        var options = BitmapFactory.Options()
             options.inJustDecodeBounds = false


        var decodedBytes =   Base64.decode(fotoString, Base64.DEFAULT)
        var miBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size, options)


        Toast.makeText(this, "Bytes decodificados:  ${miBitmap}", Toast.LENGTH_SHORT).show()


    }*/
}
