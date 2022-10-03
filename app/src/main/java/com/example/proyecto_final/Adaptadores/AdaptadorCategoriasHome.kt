package com.example.proyecto_final.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_final.ObjetosParcelables.CategoriaNegocio
import com.example.proyecto_final.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.estilo_categorias_home.view.*
import kotlinx.android.synthetic.main.estilo_categorias_registro.view.*

class AdaptadorCategoriasHome (var categorias: ArrayList<CategoriaNegocio>):
    RecyclerView.Adapter<AdaptadorCategoriasHome.ViewHolder>(), Filterable {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.estilo_categorias_home, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun getItemCount(): Int = categorias.size


    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        return holder.render(categorias.get(i))
    }


    inner class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        fun render(miCategoria: CategoriaNegocio) {

            Picasso.get().load(miCategoria.ImagenCategoria).into(itemView.imagenCategoriaHome)
            itemView.NombreCategoriaHome.text = miCategoria.NombreCategoria
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
