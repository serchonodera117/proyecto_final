package com.example.proyecto_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.proyecto_final.Fragmentos.AllProductos_f
import com.example.proyecto_final.Fragmentos.CarritoCompras_f
import com.example.proyecto_final.Fragmentos.Home_f
import com.example.proyecto_final.Fragmentos.Perfil_f
import com.example.proyecto_final.ObjetosParcelables.DatosUsuario
import kotlinx.android.synthetic.main.activity_principal.*

class Principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        val miHome = Home_f()
        val productos = AllProductos_f()
        val carritoCompras = CarritoCompras_f()
        val perfil = Perfil_f()

        val barra = supportActionBar
         barra?.hide()

        //-------------------Definición de home al iniciar seción
        cambioFragment(miHome)

        //------------------------------------------------------------------- Recibo de  Datos
                               //---Datos del usuario
        var bunle = intent.extras
        var usuario = bunle?.getParcelable<DatosUsuario>("datosUsuario")

        //------------------------------------------------------------------- Envío de  Datos
                            //envío del objeto datos usuario a fragment perfil
        val infoPerfil = Bundle()
          infoPerfil.putParcelable("infoPerfil", usuario)
          perfil.arguments = infoPerfil
          miHome.arguments = infoPerfil
          carritoCompras.arguments = infoPerfil
         productos.arguments = infoPerfil

       //--------------------------------------------------------------------- Cambio de fragments listener menu
       menuNavegable.setOnNavigationItemSelectedListener {
           when(it.itemId){
               R.id.home -> cambioFragment(miHome)
               R.id.all_productos -> cambioFragment(productos)
               R.id.carrito_compras -> cambioFragment(carritoCompras)
               R.id.miperfil -> cambioFragment(perfil)
           }
           true
       }
    }


    //-------------------------------------------- Cmbio de fragments función
    fun cambioFragment(miFragment: Fragment)
    {
        if(miFragment != null){
            val transaccion = supportFragmentManager.beginTransaction()
            transaccion.replace(R.id.contenedor, miFragment)
            transaccion.commit()
        }

    }
}