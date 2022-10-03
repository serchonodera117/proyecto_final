package com.example.proyecto_final.Fragmentos

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.ObjetosParcelables.DatosNegocio
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import com.example.proyecto_final.R
import kotlinx.android.synthetic.main.activity_registro_cliente.*
import kotlinx.android.synthetic.main.fragment_agregar_producto_f.*
import kotlinx.android.synthetic.main.fragment_editar_borrar_productos_f.*


class AgregarProducto_f : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agregar_producto_f, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //------------------------------------------------------------------- Recibir datos
        var argumentos = arguments
        var infoNegocio = argumentos?.getParcelable<DatosNegocio>("id_nombreNegocio")
        var usuario = argumentos?.getParcelable<DatosUsuario>("llaveCorreo")


        //------------------------------------------------------------------------Cargar Spiner de los tipos de oferta
        var ofertaTipo = arrayOf("Venta de un producto", "Venta de un servicio")
        var adaptador = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_gallery_item, ofertaTipo)
            spinnerOferta.adapter = adaptador


        //---------------------------------------------------------------------- ocultar cantidad cuando es servicio
        spinnerOferta.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 1){
                    ExistenciaProducto.setText("1")
                    ExistenciaProducto.visibility = View.GONE
                }else {ExistenciaProducto.visibility = View.VISIBLE}
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        //------------------------------------------------------------------------RegistrarProducto.
        RegistrarProducto.setOnClickListener {
            if(!NombreProducto.text.isEmpty() && !ExistenciaProducto.text.isEmpty() && !SeccionProducto.text.isEmpty() &&
              !ImagenProducto.text.isEmpty() && !PrecioProducto.text.isEmpty()){
            RegistrarProducto(infoNegocio, usuario)
            }else{Toast.makeText(requireActivity(), "Necesita llenar todos los campos",  Toast.LENGTH_SHORT).show()}
        }

    }


    fun RegistrarProducto(infoNegocio: DatosNegocio?, usuario: DatosUsuario?)
    {
            var queue = Volley.newRequestQueue(activity)
            val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/crud_productos.php?comando=Agregar"+
                    "&idnegocio=${infoNegocio?.idnegocio}&correoVendedor=${usuario?.CorreoUsuario}&NombreNegocio=${infoNegocio?.NombreNegocio}&NombreProducto=${NombreProducto.text}" +
                    "&SeccionProducto=${SeccionProducto.text}&ExistenciaProducto=${ExistenciaProducto.text.toString().toInt()}" +
                    "&TipoOferta=${spinnerOferta.selectedItem.toString()}&ImagenProducto=${ImagenProducto.text}&Precio=${PrecioProducto.text}" +
                    "&PrecioEnvio=${PrecioEnvio.text}&DescripcionProducto=${DescripcionProducto.text}"

            val stringReq = StringRequest(
                Request.Method.GET, url,
                { response ->
                    var strResp = response.toString()
                    if (strResp == "") {
                        Aviso("Producto","No se pudo registrar!")

                    } else {     //mensaje de respuesta al registrar

                        val dialogBuilder = AlertDialog.Builder(requireActivity())
                        dialogBuilder.setMessage(strResp)
                            .setCancelable(false)
                            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })

                        val alert = dialogBuilder.create()
                        alert.setTitle("Registro de Productos")
                        alert.show()

                        NombreProducto.setText("")
                        SeccionProducto.setText("")
                        DescripcionProducto.setText("")
                        ImagenProducto.setText("")
                        ExistenciaProducto.setText("0")
                        PrecioEnvio.setText("0")
                        PrecioProducto.setText("0")

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