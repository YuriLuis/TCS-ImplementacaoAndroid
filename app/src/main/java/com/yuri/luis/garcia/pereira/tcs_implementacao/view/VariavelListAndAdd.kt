package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.adapter.AdapterVariavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.util.RecyclerItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class VariavelListAndAdd : Fragment() {

    private lateinit var adapter : AdapterVariavel
    private lateinit var recyclerViewVariavel: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list_and_add_variavel, container, false)

        view.findViewById<FloatingActionButton>(R.id.btnFlutuanteAddVariavel).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_variavelListAndAdd_to_varivalFragment)
        }

        initcomponents(view)
        atualizaRecyclerViewVariaveis()
        eventoClickRecyclerView()
        return view
    }

    private fun atualizaRecyclerViewVariaveis() {
        var call = RetrofitInitializer().variavelService().findAllVariaveis()
        call.enqueue(object : Callback<List<Variavel>> {
            override fun onFailure(call: Call<List<Variavel>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<Variavel>>, response: Response<List<Variavel>>) {
                var variavel : List<Variavel> = response.body()!!
                configuraRecyclerViewVariaveis(variavel as MutableList<Variavel>)
                adapter.notifyDataSetChanged()
            }

        })
    }

    private fun configuraRecyclerViewVariaveis(variaveis: MutableList<Variavel>) {
        val layout = LinearLayoutManager(context)
        this.adapter = AdapterVariavel(variaveis)
        recyclerViewVariavel.adapter = adapter
        recyclerViewVariavel.layoutManager = layout
        recyclerViewVariavel.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        recyclerViewVariavel.setHasFixedSize(true)

    }

    private fun eventoClickRecyclerView(){
        recyclerViewVariavel.addOnItemTouchListener(RecyclerItemClickListener(
            context,
            recyclerViewVariavel,
            object : RecyclerItemClickListener.OnItemClickListener{
                override fun onLongItemClick(view: View?, position: Int) {
                    Toast.makeText(context,"Click Longo", Toast.LENGTH_LONG).show()
                }

                override fun onItemClick(view: View?, position: Int) {
                    Navigation.findNavController(view!!).navigate(R.id.action_variavelListAndAdd_to_varivalFragment)

                }

                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Toast.makeText(context,"Click normal2", Toast.LENGTH_LONG).show()
                }

            }
        ))
    }

    private fun initcomponents(view : View){
        recyclerViewVariavel = view.findViewById<RecyclerView>(R.id.recyclerViewVariavel)
    }

}
