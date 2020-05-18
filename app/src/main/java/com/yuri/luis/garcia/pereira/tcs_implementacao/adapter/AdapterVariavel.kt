package com.yuri.luis.garcia.pereira.tcs_implementacao.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import kotlinx.android.synthetic.main.alunos_layout.view.*
import kotlinx.android.synthetic.main.fragment_varival.view.*

class AdapterVariavel(private val variaveis: MutableList<Variavel>) :
    RecyclerView.Adapter<AdapterVariavel.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.alunos_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.variaveis.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val variavel : Variavel = this.variaveis[position]
        holder.nomeVariavel.text = variavel.nome
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nomeVariavel : TextView = itemView.textViewVariavel
    }
}