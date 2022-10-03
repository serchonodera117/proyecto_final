package com.example.proyecto_final

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.Adaptadores.AdaptadorCategoriasRegistro
import com.example.proyecto_final.ObjetosParcelables.CategoriaNegocio
import kotlinx.android.synthetic.main.activity_agregar_negocio_nuevo.*
import kotlinx.android.synthetic.main.activity_registro_vendedor.*
import org.json.JSONArray
import org.json.JSONObject

class AgregarNegocioNuevo : AppCompatActivity() {
    var listaCategoria = ArrayList<CategoriaNegocio>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_negocio_nuevo)
        val barra = supportActionBar
           barra?.hide()
        Volver_miPerfil.setOnClickListener { finish() }

        //------------------------------------------------------------------------ Recibir parámetros
          var bunle = intent.extras
        var miCorreo = bunle?.getString("correoVendedor")
        //------------------------------------------------------------------------ cargar lista de categorías
        listarCategoria(listaCategoria)

        //------------------------------------------------------------------------Cargar Spiner de municipios
        var municipios = arrayOf("Colima", "Villa de Alvarez")
        var adaptador = ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, municipios)
       nuevoMunicipioNegocio.adapter = adaptador


        //------------------------------------------------------------------------Aparecer y desaparecercategorías
        nuevocontenedorCategoria.visibility = View.GONE
        nuevoAgregarCategoria.setOnClickListener {
            nuevocontenedorCategoria.visibility = View.VISIBLE
        }

        //---------------------------------------------------------------------------Añadir categorías
        nuevoGuardarCategoria.setOnClickListener {
            if(!nuevourlImagenCategoria.text.isEmpty() && !nuevoNombreCategoria.text.isEmpty()){

                RegistrarCategoría()
                 listarCategoria(listaCategoria)

                nuevoNombreCategoria.setText("")
                nuevourlImagenCategoria.setText("")
                nuevocontenedorCategoria.visibility = View.GONE
            }else{
                Toast.makeText(this, "no deben de estar vacíos ni la imagen ni la caja", Toast.LENGTH_SHORT).show()
            }
        }

        //---------------------------------------------------------------------------- registrar el negocio
        nuevoRegistrarNegocio.setOnClickListener {
            if( !nuevoNombreNegocio.text.isEmpty() && nuevoCategoriaSeleccionada.text != "- - -"){
               RegistrarNegocio(miCorreo.toString())
            }else{
                Toast.makeText(this, "no deben de estar vacíos ni la imagen ni la caja", Toast.LENGTH_SHORT).show()
            }
        }

        nuevoCerrarCategoria.setOnClickListener { nuevocontenedorCategoria.visibility = View.GONE }

    }

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
                        listaCategoria.clear()
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
                        nuevoCategoriasExistentes.layoutManager = LinearLayoutManager(this)
                        val miAdaptador = AdaptadorCategoriasRegistro(categorias)
                        nuevoCategoriasExistentes.adapter = miAdaptador


                        //--------------------------------------------Asignación del clic listener
                        miAdaptador.setOnItemClickListener(object :
                            AdaptadorCategoriasRegistro.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                nuevoCategoriaSeleccionada.setText(categorias.get(position).NombreCategoria)
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


    fun RegistrarCategoría(){

        var queue = Volley.newRequestQueue(this)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/agregar_categoria.php?codigo=Agregar&NombreCategoria=${nuevoNombreCategoria.text}"+
                "&ImagenCategoria=${nuevourlImagenCategoria.text}"

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

    fun RegistrarNegocio(correo:String){
        var pago_tarjeta:String = "No aplica"
        var pago_oxxo:String = "No aplica"
        var pago_efectivo:String = "No aplica"
        var oferta_servicio = "No aplica"
        var oferta_producto = "No aplica"
        if(nuevoTarjetaSelect.isChecked){pago_tarjeta = "Pago con tarjeta"}
        if(nuevoOxxoSelect.isChecked){pago_oxxo = "Pago en oxxo"}
        if(nuevoEfectivoSelect.isChecked){pago_efectivo = "Pago en efectivo"}
        if(nuevoOfertaServicio.isChecked){oferta_servicio = "Venta de servicios"}
        if(nuevoOfertaProducto.isChecked){oferta_producto = "Venta de productos"}


        var queue = Volley.newRequestQueue(this)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/registro_usuarios.php?TipoUsuario=Negocio"+
                "&NombreNegocio=${nuevoNombreNegocio.text}&LogoNegocio=${nuevourlImagenNegocio.text}&Efectivo=${pago_efectivo}&Tarjeta=${pago_tarjeta}&PagoOxxo=${pago_oxxo}" +
                "&OfertaProducto=${oferta_producto}&OfertaServicio=${oferta_servicio}&CategoriaNegocio=${nuevoCategoriaSeleccionada.text}&MunicipioNegocio=${nuevoMunicipioNegocio.selectedItem.toString()}" +
                "&ColoniaNegocio=${nuevoColoniaNegocio.text}&DireccionNegocio=${nuevoDireccionNegocio.text}&CorreoUsuario=${correo}"

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
                            dialog.cancel()
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
}