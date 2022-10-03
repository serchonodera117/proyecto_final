package com.example.proyecto_final.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_final.ObjetosParcelables.DatosNegocio
import com.example.proyecto_final.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.estilo_negocios_de_mi_perfil.view.*

class AdaptadorNegociosDeMiPerfil(val negociosPerfil: ArrayList<DatosNegocio>):
RecyclerView.Adapter<AdaptadorNegociosDeMiPerfil.ViewHolder>(), Filterable{

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.estilo_negocios_de_mi_perfil, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun getItemCount(): Int = negociosPerfil.size


    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        return holder.render(negociosPerfil.get(i))
    }


    inner class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        fun render(miNegocio: DatosNegocio) {

            Picasso.get().load(miNegocio.LogoNegocio).into(itemView.imagenMiNegocioPerfil)
            itemView.nombreMiNegocioPerfil.text = miNegocio.NombreNegocio.toString()
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

