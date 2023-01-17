package com.example.practica5.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica5.R
import com.example.practica5.databinding.ItemTareaBinding
import com.example.practica5.model.Tarea

class TareasAdapter:
    RecyclerView.Adapter<TareasAdapter.TareaViewHolder>(){
    lateinit var listaTareas: List<Tarea>
    private var onTareaClickListener:OnTareaClickListener?=null


    fun setLista(lista:List<Tarea>){
        listaTareas=lista
        //notifica al adaptador que hay cambios y tiene que redibujar el ReciclerView
        notifyDataSetChanged()
    }

    inner class TareaViewHolder(val binding: ItemTareaBinding) :
        RecyclerView.ViewHolder(binding.root){
        init {
            //inicio del click de icono borrar
            binding.ivBorrar.setOnClickListener(){
                //recuperamos la tarea de la lista
                val tarea=listaTareas.get(this.adapterPosition)
                //llamamos al evento borrar que estará definido en el fragment
                onTareaClickListener?.onTareaBorrarClick(tarea)
            }
            //inicio del click sobre el Layout(constraintlayout)
            binding.root.setOnClickListener(){
                val tarea=listaTareas.get(this.adapterPosition)
                onTareaClickListener?.onTareaClick(tarea)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        //utilizamos binding, en otro caso hay que indicar el item.xml. Para más detalles puedes verlo en la documentación
        val binding = ItemTareaBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TareaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        with(holder) {
            //cogemos la tarea a mostrar y rellenamos los campos del ViewHolder
            with(listaTareas!!.get(position)) {
                binding.tvId.text = id.toString()
                binding.tvDescripcion.text = descripcion
                binding.tvTecnico.text = tecnico
                binding.rbValoracion1.rating = valoracionCliente
                //mostramos el icono en función del estado
                binding.ivEstado1.setImageResource(
                    when (estado) {
                        0 -> R.drawable.ic_abierto
                        1 -> R.drawable.ic_encurso
                        else -> R.drawable.ic_cerrado
                    }
                )
                //cambiamos el color de fondo si la prioridad es alta
                binding.cvItem.setBackgroundResource(
                    if (prioridad == 2)//prioridad alta
                        R.color.prioridad_alta
                    else
                        Color.TRANSPARENT
                )
            }
        }
    }

    override fun getItemCount(): Int = listaTareas?.size?:0

    interface OnTareaClickListener{
        //editar tarea  que contiene el ViewHolder
        fun onTareaClick(tarea:Tarea?)
        //borrar tarea que contiene el ViewHolder
        fun onTareaBorrarClick(tarea:Tarea?)
    }


}