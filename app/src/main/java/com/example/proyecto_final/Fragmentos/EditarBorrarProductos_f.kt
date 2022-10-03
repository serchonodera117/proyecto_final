package com.example.proyecto_final.Fragmentos

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.example.proyecto_final.ObjetosParcelables.Productos
import com.example.proyecto_final.R
import com.example.proyecto_final.productoSeleccionadoCliente
import kotlinx.android.synthetic.main.fragment_agregar_producto_f.*
import kotlinx.android.synthetic.main.fragment_editar_borrar_productos_f.*
import kotlinx.android.synthetic.main.fragment_editar_borrar_productos_f.view.*

class EditarBorrarProductos_f : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_borrar_productos_f, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //------------------------------------------------------------------------Cargar Spiner de los tipos de oferta
        var ofertaTipo = arrayOf("Venta de un producto", "Venta de un servicio")
        var adaptador = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_gallery_item, ofertaTipo)
        editarspinnerOferta.adapter = adaptador

        //---------------------------------------------------------------------- ocultar cantidad cuando es servicio
        editarspinnerOferta.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 1){
                    editarExistenciaProducto.setText("1")
                    editarExistenciaProducto.visibility = View.GONE
                }else {editarExistenciaProducto.visibility = View.VISIBLE}
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        //---------------------------------------------------------------------------recibir información
         var producto = arguments?.getParcelable<Productos>("productoEditar")
         var usuario = arguments?.getParcelable<DatosUsuario>("usuarioEditProduct")

        //------------------------------------------------------------------------------------- abrir pagina del producto
        informacionProducto.setOnClickListener {
            //enviar producto y usuario a la selecciono
            var selectedProducto = Intent(requireActivity(), productoSeleccionadoCliente::class.java)
            selectedProducto.putExtra("productoSeleccioandoCliente",producto)
            selectedProducto.putExtra("usuarioVenta", usuario)
            startActivity(selectedProducto)
        }


        //---------------------------------------------------------------------------Asignación de información
        editarNombreProducto.setText(producto?.Nombreproducto)
        editarSeccionProducto.setText(producto?.SeccionProducto)
        editarDescripcionProducto.setText(producto?.DescripcionProducto)

        var posicionOferta = if(producto?.TipoOferta == "Venta de un producto") 0 else 1
        editarspinnerOferta.setSelection(posicionOferta)

        editarImagenProducto.setText(producto?.ImagenProducto)
        editarExistenciaProducto.setText(producto?.ExistenciaProducto)
        editarPrecioProducto.setText(producto?.Precio)
        editarPrecioEnvio.setText(producto?.PrecioEnvio)

        if(producto?.Visibilidad == "Visible") {
            editarVisibilidad.isChecked = true
            editarVisibilidad.setText("Visible")
        }else{
            editarVisibilidad.isChecked = false
            editarVisibilidad.setText("No visible")
        }

        editarVisibilidad.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                editarVisibilidad.setText("Visible")
                producto?.Visibilidad = "Visible"
            }
            else{
                editarVisibilidad.setText("No visible")
                producto?.Visibilidad = "No visible"
            }
        }

        //-------------------------------------------------------------------------------- editar el producto
        cometerEdicion.setOnClickListener { editarProducto(producto) }

        //------------------------------------------------------------------------------- Borrar el producto
        BorrarProducto.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireActivity())
            dialogBuilder.setMessage("Estás por borrar ${producto?.Nombreproducto}! \n ¿Desea continuar?")
                .setCancelable(true)
                .setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                    borrarMiProducto(producto)
                    dialog.cancel()
                }).setNegativeButton("Ahorita no joven", DialogInterface.OnClickListener{ dialog, id ->
                    dialog.cancel()
                })

            val alert = dialogBuilder.create()
            alert.setTitle("Aviso")
            alert.show()
        }
    }

    fun editarProducto(producto: Productos?)
    {            var queue = Volley.newRequestQueue(activity)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/crud_productos.php?comando=Editar"+
                "&idproducto=${producto?.idproducto}&NombreProducto=${editarNombreProducto.text}" +
                "&SeccionProducto=${editarSeccionProducto.text}&ExistenciaProducto=${editarExistenciaProducto.text.toString().toInt()}" +
                "&TipoOferta=${editarspinnerOferta.selectedItem}&ImagenProducto=${editarImagenProducto.text}&Precio=${editarPrecioProducto.text}" +
                "&PrecioEnvio=${editarPrecioEnvio.text}&DescripcionProducto=${editarDescripcionProducto.text}&Visibilidad=${producto?.Visibilidad}"

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

                }
            },

            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
    }

    fun borrarMiProducto(producto: Productos?){
        var queue = Volley.newRequestQueue(activity)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/crud_productos.php?comando=Eliminar"+
                "&idproducto=${producto?.idproducto}"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                if (strResp == "") {
                    Aviso("Producto","No se pudo Eliminar el producto!")

                } else {     //mensaje de respuesta al registrar

                    val dialogBuilder = AlertDialog.Builder(requireActivity())
                    dialogBuilder.setMessage(strResp)
                        .setCancelable(false)
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        })

                    val alert = dialogBuilder.create()
                    alert.setTitle("Eliminación de Productos")
                    alert.show()

                    editarNombreProducto.setText("")
                    editarSeccionProducto.setText("")
                    editarDescripcionProducto.setText("")
                    editarImagenProducto.setText("")
                    editarExistenciaProducto.setText("0")
                    editarPrecioEnvio.setText("0")
                    editarPrecioProducto.setText("0")


                    cometerEdicion.visibility = View.GONE
                    BorrarProducto.visibility = View.GONE
                }
            },

            {
                Aviso("Red","Error de conexión")

            })
        queue.add(stringReq)
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