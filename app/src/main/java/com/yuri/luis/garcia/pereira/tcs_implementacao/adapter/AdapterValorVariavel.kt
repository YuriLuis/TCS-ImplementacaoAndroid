package com.yuri.luis.garcia.pereira.tcs_implementacao.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.ActivityNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor

class AdapterValorVariavel(private val variaveis: MutableList<VariavelValor>) :
    RecyclerView.Adapter<AdapterValorVariavel.MyViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    var onItemClick: ((VariavelValor) -> Unit)? = null
    private lateinit var valor: VariavelValor

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
        this.valor = variaveis[position]

    }

    fun getteste(): VariavelValor {
        return valor
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView = itemView.findViewById<TextView>(R.id.textViewVariavel)
        var tvIdValor: TextView = itemView.findViewById(R.id.textView4)

        init {
            tvItem.setOnClickListener {
                onItemClick?.invoke(variaveis[adapterPosition])

            }
        }

        fun bind(variavelValor: VariavelValor) {
            tvItem.text = variavelValor.valor
        }
    }

}