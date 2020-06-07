package com.yuri.luis.garcia.pereira.tcs_implementacao.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.RegraItemResultado

class AdapterRegraItemResultado(private val items: MutableList<RegraItemResultado>) :
    RecyclerView.Adapter<AdapterRegraItemResultado.MyViewHolder>() {

    var index_position: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        index_position = 0
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.regras_item_resultado_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        index_position = position
        holder.bind(items[position])
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView = itemView.findViewById<TextView>(R.id.textViewRegraItemResultado)
        fun bind(item: RegraItemResultado) {
            index_position += 1
            var v: String = ""
            if (index_position == 1) {
               v = v + " ENTÃO  ("
            }
            else {
                v = v + "               ("
            }
            v = v + item.variavel?.nome + " = " + item.variavelValor?.valor +" CNF " + String.format("%.2f", item.fatorConfianca) +"%)"
            tvItem.text = v
        }
    }

}