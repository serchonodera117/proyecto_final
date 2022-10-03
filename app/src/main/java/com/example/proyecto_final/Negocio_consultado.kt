package com.example.proyecto_final

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.Adaptadores.AdaptadorProductos
import com.example.proyecto_final.Fragmentos.EditarBorrarProductos_f
import com.example.proyecto_final.ObjetosParcelables.DatosNegocio
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.example.proyecto_final.ObjetosParcelables.Productos
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_administrar_negocio.*
import kotlinx.android.synthetic.main.activity_negocio_consultado.*
import kotlinx.android.synthetic.main.fragment_mi_lista_productos_f.*
import org.json.JSONArray
import org.json.JSONObject

class Negocio_consultado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_negocio_consultado)

        var barra = supportActionBar
            barra?.hide()

        //------------------------------------------------------------------ recibimiento de informacion
        var bunle = intent.extras
        var infoUsuario = bunle?.getParcelable<DatosUsuario>("usuarioDatos")
        var infoNegocio = bunle?.getParcelable<DatosNegocio>("negocioConsultado")

        //------------------------------------------------------------------ Acoplamiento de los datos en la interfaz
           Picasso.get().load(infoNegocio?.LogoNegocio).into(verImagenNegocio)
        verNombreNegocio.setText(infoNegocio?.NombreNegocio)
        verNombreMunicipio.setText(infoNegocio?.MunicipioNegocio)
        verDireccionNegocio.setText(infoNegocio?.DireccionNegocio)

        var formasPago = ""
        var tipoOfertas = ""

        if(infoNegocio?.PagoOxxo != "No aplica"){formasPago += "-${infoNegocio?.PagoOxxo}\n"}
        if(infoNegocio?.Efectivo != "No aplica"){formasPago += "-${infoNegocio?.Efectivo}\n"}
        if(infoNegocio?.Tarjeta != "No aplica"){formasPago += "-${infoNegocio?.Tarjeta}\n"}

        verFormasDePago.setText(formasPago)

        if(infoNegocio?.Oferta_producto != "No aplica"){tipoOfertas += "-${infoNegocio?.Oferta_producto}\n"}
        if(infoNegocio?.Oferta_servicio != "No aplica"){tipoOfertas += "-${infoNegocio?.Oferta_servicio}\n"}

        verTiposDeOferta.setText(tipoOfertas)

        Volver_A_home.setOnClickListener{finish()}

        //---------------------------------------------------------------------- Listar los productos del negocio consultado
        listarProductos(infoNegocio)
    }

    fun listarProductos(negocio: DatosNegocio?)
    {
        val queue = Volley.newRequestQueue(this)
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

                        productosListados.add(producto)
                        }
                        indice++
                    }
                    // Ordenado alfabeticamente
                    productosListados.sortBy { it.Nombreproducto }

                    orfetasNegocioConsultado.layoutManager = GridLayoutManager(this, 2)
                    var miAdaptador = AdaptadorProductos(productosListados)
                    orfetasNegocioConsultado.adapter = miAdaptador



                    //--------------------------------------------Asignación del clic listener
                    //----abrir la pagina de edición.
                    miAdaptador.setOnItemClickListener(object :
                        AdaptadorProductos.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            miCambio(productosListados.get(position))
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


    fun miCambio (elemento: Productos) {
        Toast.makeText(this, "seleccionado: ${elemento.Nombreproducto}", Toast.LENGTH_SHORT).show()

        var selectedProducto = Intent(this, productoSeleccionadoCliente::class.java)
           selectedProducto.putExtra("productoSeleccioandoCliente", elemento)
        startActivity(selectedProducto)
    }
}