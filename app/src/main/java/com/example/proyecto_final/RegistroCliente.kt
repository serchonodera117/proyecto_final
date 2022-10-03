package com.example.proyecto_final

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registro_cliente.*

class RegistroCliente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_cliente)
        val a = supportActionBar
        a?.hide()

            //------------------------------------------------------------------------Cargar Spiner de municipios
        var municipios = arrayOf("Colima", "Villa de Alvarez")
        var adaptador = ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, municipios)
        MunicipioCliente.adapter = adaptador

          //------------------------------------------------------------------------Abrir camara
           /* buscarImagenCLiente.setOnClickListener {                             canceldo
             val intent = Intent(Intent.ACTION_PICK)
              intent.type = "image/*"
              startActivityForResult(intent, 100)



               //------------------------------------------convertir foto a bitmap
        var btmap = ImagenCliente.drawable as BitmapDrawable
          var fotoUsuario = btmap.bitmap
          } */
           */



        Volver_c.setOnClickListener { finish() }
        RegistrarCliente.setOnClickListener {
         if(!NombreCliente.text.isEmpty() || !CorreoUsuario.text.isEmpty() || !ContrasenaUsuario.text.isEmpty()){
            registrarCliente()
         }else{
             Toast.makeText(this, "Se neceita tener todos los campos completos", Toast.LENGTH_SHORT).show()
         }
        }
    }

    fun registrarCliente(){
        var queue = Volley.newRequestQueue(this)
        val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/registro_usuarios.php?TipoUsuario=Cliente"+
                 "&NombreUsuario=${NombreCliente.text}&FotoUsuario=${urlImagenCliente.text}&MunicipioUsuario=${MunicipioCliente.selectedItem.toString()}" +
                "&ColoniaUsuario=${ColoniaCliente.text}&DomicilioUsuario=${DomicilioCliente.text}&CorreoUsuario=${CorreoCliente.text}" +
                "&ContrasenaUsuario=${ContrasenaCliente.text}&TelefonoUsuario=${TelefonoCliente.text}&VerificacionUsuario=${1}"

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



   /*                                                    ---------------------------------sobre escritura de metodo para obtención de fotos
    *override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK){
            ImagenCliente.setImageURI(data?.data)

        }
    }*/

}