package com.yuri.luis.garcia.pereira.tcs_implementacao.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.RegraItem

class AdapterRegraItem(private val items: MutableList<RegraItem>) :
    RecyclerView.Adapter<AdapterRegraItem.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.regras_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView = itemView.findViewById<TextView>(R.id.textViewRegra)

        fun bind(item: RegraItem) {
            tvItem.text = item.variavel?.nome + " " + item.condicional + " " + item.variavelValor?.valor

        }
    }

}