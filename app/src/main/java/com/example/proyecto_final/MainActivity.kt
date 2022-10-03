package com.example.proyecto_final

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registro_cliente.*
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        //------------------------------------------------------------------------Lanzar modal que te envía a registro cliente o vendedor
      Registrarme.setOnClickListener{
          val dialogBuilder = AlertDialog.Builder(this)
          dialogBuilder.setMessage("Seleccione el tipo de usuario que desea registrar. . .")
              .setCancelable(true)
              .setPositiveButton("Cliente", DialogInterface.OnClickListener {
                      dialog, id ->
                  val intent = Intent(this, RegistroCliente::class.java)
                  startActivity(intent)

                  dialog.cancel()
              }).setNegativeButton("Vendedor", DialogInterface.OnClickListener{
                  dialog, id ->
                 val intent = Intent(this, RegistroVendedor::class.java)
                  startActivity(intent)
                  dialog.cancel()
              })
          val alert = dialogBuilder.create()
          alert.setTitle("Registro de usuario")
          alert.show()
      }

        //-------------------------------------------------------------------Obtener los datos almacenados por el usuario en caso de darle en recordar
        val preferencias = getSharedPreferences("info", Context.MODE_PRIVATE)
        CorreoUsuario.setText(preferencias.getString("usuario", ""))
        ContrasenaUsuario.setText(preferencias.getString("contraseña", ""))

        if(preferencias.getString("usuario", "")!= ""){Recordar.isChecked = true}
        else{Recordar.isChecked = false}


        Ingresar.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val url: String = "https://registrosappinventor.000webhostapp.com/proyecto/loginproyecto.php?" +
                    "correo=${CorreoUsuario.text.toString()}&contrasena=${ContrasenaUsuario.text.toString()}"

            val respuestaString = StringRequest(
                Request.Method.GET, url,
                { response ->
                    //-----------------------------Obtención de un array json con los datos consultados
                    var respuesta = response.toString()
                    val jsonObj: JSONObject = JSONObject(respuesta)
                    val jsonArray: JSONArray = jsonObj.getJSONArray("usuario")

                    if(jsonArray.length()==0) {
                        Aviso("Usuarios", "el usuario ${CorreoUsuario.text.toString()} no se encuentra registrado ")
                    }
                    else{
                        //-----------------------------Modificacion/almacenado del usuario y contraseña(recordar)
                        if(Recordar.isChecked) {
                            val editor = preferencias.edit()
                            editor.putString("usuario", CorreoUsuario.text.toString())
                            editor.putString("contraseña", ContrasenaUsuario.text.toString())
                            editor.commit()
                        }else{
                            val editor = preferencias.edit()
                            editor.putString("usuario", "")
                            editor.putString("contraseña", "")
                            editor.commit()
                        }

                        //-----------------------------Almacenamiento del array en variable, y busqueda en atributos del objeto
                        var datos = JSONObject(jsonArray.get(0).toString())
                        var usuario = DatosUsuario()

                        usuario.idusuario = datos.getString("idusuario").toString()
                        usuario.NombreUsuario = datos.getString("NombreUsuario").toString()
                        usuario.FotoUsuario = datos.getString("FotoUsuario").toString()
                        usuario.MunicipioUsuario = datos.getString("MunicipioUsuario").toString()
                        usuario.DomicilioUsuario = datos.getString("DomicilioUsuario").toString()
                        usuario.ColoniaUsuario = datos.getString("ColoniaUsuario").toString()
                        usuario.CorreoUsuario = datos.getString("CorreoUsuario").toString()
                        usuario.ContrasenaUsuario = datos.getString("ContrasenaUsuario").toString()
                        usuario.TelefonoUsuario  = datos.getString("TelefonoUsuario").toString()
                        usuario.TipoDeUsuario = datos.getString("TipoDeUsuario").toString()
                        usuario.VerificacionUsuario = datos.getString("VerificacionUsuario").toString()


                         val login = Intent(this,  Principal::class.java)
                         login.putExtra("datosUsuario", usuario)
                         startActivity(login)
                        //pasar correoVendedor(llave para consultar) al fragment mis clientes
                    }
                },
                {
                    Aviso("Red", "No se pudo conectar a la base")
                })
            queue.add(respuestaString)
        }

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


