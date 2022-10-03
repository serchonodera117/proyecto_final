package com.example.proyecto_final.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_final.ObjetosParcelables.DatosCometario
import com.example.proyecto_final.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.estilo_comentarios.view.*

class AdaptadorComentarios(var misComentarios: ArrayList<DatosCometario>):
 RecyclerView.Adapter<AdaptadorComentarios.ViewHolder>(), Filterable{

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.estilo_comentarios, parent, false)//-------------------------------Le falta el estilo ::::::::::::::::::::
        return ViewHolder(v, mListener)
    }

    override fun getItemCount(): Int = misComentarios.size


    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        return holder.render(misComentarios.get(i))
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        fun render(miComentario: DatosCometario) {
            Picasso.get().load(miComentario.Imagen_Usuario).into(itemView.comentario_ImagenUsuario)
            itemView.comentario_NombreUsuario.text = miComentario.Nombre_Usuario
            itemView.comentario_Fecha.text = miComentario.Fecha_Comentario
            itemView.comentario_NameRespuesta.text = miComentario.Nombre_Usuario_Respuesta
            itemView.comentario_Contenido.text = miComentario.Contenido

            if(miComentario.Tipo_Comentario == "Comentario"){itemView.comentario_IdentifyRespuesta.visibility = View.GONE}

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

