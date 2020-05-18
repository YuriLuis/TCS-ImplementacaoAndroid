package com.yuri.luis.garcia.pereira.tcs_implementacao.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import kotlinx.android.synthetic.main.fragment_varival.view.*

class AdapterVariavel(private val variaveis: MutableList<Variavel>) :
    RecyclerView.Adapter<AdapterVariavel.MyViewHolder>() {

    var onItemClick: ((Variavel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.alunos_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.variaveis.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(variaveis[position])
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvItem: TextView = itemView.findViewById<TextView>(R.id.textViewVariavel)


        fun bind(valor: Variavel) {
            tvItem.text = valor.nome
        }
    }

}