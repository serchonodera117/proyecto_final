package com.example.proyecto_final.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_final.ObjetosParcelables.Productos
import com.example.proyecto_final.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.estilo_lista_mis_productos.view.*

class AdaptadorProductos(val Productos: ArrayList<Productos>):
 RecyclerView.Adapter<AdaptadorProductos.ViewHolder>(), Filterable{

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.estilo_lista_mis_productos, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun getItemCount(): Int = Productos.size


    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        return holder.render(Productos.get(i))
    }


    inner class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        fun render(misProductos: Productos) {

            Picasso.get().load(misProductos?.ImagenProducto).into(itemView.consultaMiImagenProducto)
            itemView.consultaNombreMiNegocio.text = misProductos.NombreNegocio
            itemView.consultaMiProducto.text = misProductos.Nombreproducto
            itemView.consultaMiPrecio.text = "$ " + misProductos.Precio
        }
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }


    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

}