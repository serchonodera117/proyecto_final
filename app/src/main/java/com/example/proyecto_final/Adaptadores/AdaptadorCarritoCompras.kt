package com.example.proyecto_final.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_final.ObjetosParcelables.DatosCarrito
import com.example.proyecto_final.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.estilo_lista_carrito.view.*

class AdaptadorCarritoCompras (val miPeticion: ArrayList<DatosCarrito>):
 RecyclerView.Adapter<AdaptadorCarritoCompras.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.estilo_lista_carrito, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun getItemCount(): Int = miPeticion.size

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        return holder.render(miPeticion.get(i))
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        fun render(carrito: DatosCarrito) {

            Picasso.get().load(carrito.Imagen_Producto).into(itemView.carrito_imagen)
            itemView.carrito_NombreProducto.text = carrito.Nombre_Producto
            itemView.carrito_NombreNegocio.text = carrito.Nombre_Negocio
            itemView.carrito_cantidad.text ="Canttidad: "+ carrito.Cantidad_Solicitada
            itemView.carrito_Precio.text = "$"+ carrito.el_Precio

        }
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

}

