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
            .inflate(R.layout.regras_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView = itemView.findViewById<TextView>(R.id.textViewRegraItem)

        fun bind(item: RegraItem) {
            var v: String = ""
            if (item.conectivo == 0) {
                v = v + "SE (";
            }
            if (item.conectivo == 1) {
                v = v + "E (";
            }
            if (item.conectivo == 2) {
                v = v + "OU (";
            }
            v = v + item.variavel?.nome + " " + item.condicional + " " + item.variavelValor?.valor +")"
            tvItem.text = v

        }
    }

}