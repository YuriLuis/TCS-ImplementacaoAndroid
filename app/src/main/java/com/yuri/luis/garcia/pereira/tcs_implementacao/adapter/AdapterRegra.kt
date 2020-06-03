package com.yuri.luis.garcia.pereira.tcs_implementacao.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Regra

class AdapterRegra(private val regras: MutableList<Regra>) :
    RecyclerView.Adapter<AdapterRegra.MyViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var onItemClick: ((Regra) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.regras_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.regras.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(regras[position])
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem : TextView = itemView.findViewById<TextView>(R.id.textViewRegra)

        init {
            tvItem.setOnClickListener {
                onItemClick?.invoke(regras[adapterPosition])
            }
        }

        fun bind(regra : Regra) {
            tvItem.text = regra.nome
        }
    }

}