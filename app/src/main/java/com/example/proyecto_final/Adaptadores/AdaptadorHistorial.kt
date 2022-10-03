package com.example.proyecto_final.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_final.ObjetosParcelables.Pedidos
import com.example.proyecto_final.ObjetosParcelables.Productos
import com.example.proyecto_final.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.estilo_historial.view.*
import kotlinx.android.synthetic.main.estilo_lista_mis_productos.view.*

public  class AdaptadorHistorial (val misPedidos: ArrayList<Pedidos>):
    RecyclerView.Adapter<AdaptadorHistorial.ViewHolder>(), Filterable {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.estilo_historial, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun getItemCount(): Int = misPedidos.size


    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        return holder.render(misPedidos.get(i))
    }


    inner class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        fun render(misPedidos: Pedidos) {
             Picasso.get().load(misPedidos.Imagen_Producto).into(itemView.historial_imagen)
            itemView.historial_NombreProducto.text = misPedidos?.Nombre_Producto
            itemView.historial_NombreNegocio.text = misPedidos?.Nombre_Negocio
            itemView.historial_descripcion.text = misPedidos?.Descripcion_producto
            itemView.historial_Precio.text = "Total: $"+misPedidos?.Precio_Final
            itemView.historial_fecha.text = misPedidos?.Fecha_Pedido
            itemView.historial_NombreCliente.text = misPedidos?.Nombre_Usuario

            if(misPedidos?.Tipo_Pedido == "Venta de un servicio"){
                itemView.historial_Estado.text = "Servicio: "+ misPedidos?.Estado_Servicio
            }else{
                itemView.historial_Estado.text = "Producto: "+ misPedidos?.Estado_Producto
            }

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