package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.adapter.AdapterVariavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaVariavelAndAddActivity : AppCompatActivity() {
    private lateinit var adapter: AdapterVariavel
    private lateinit var recyclerViewVariavel: RecyclerView
    private var listaVariaveis: ArrayList<Variavel> =
        mutableListOf<Variavel>() as ArrayList<Variavel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_variavel_and_add)

        initcomponents()
    }

    override fun onStart() {
        super.onStart()
        carregaDadosApiVariavel()
        atualizaRecyclerViewVariaveis()
    }

    fun eventoClickFloatActionButtonAddVariavel(view: View){
        startActivity(Intent(this, VariavelActivity::class.java))
    }

    private fun atualizaRecyclerViewVariaveis() {
        var call = RetrofitInitializer().variavelService().findAllVariaveis()
        call.enqueue(object : Callback<List<Variavel>> {
            override fun onFailure(call: Call<List<Variavel>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<List<Variavel>>,
                response: Response<List<Variavel>>
            ) {

                if (response.isSuccessful) {
                    var variavel: List<Variavel> = response.body()!!
                    configuraAdapter(variavel)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun carregaDadosApiVariavel() {
        var call: Call<List<Variavel>> = RetrofitInitializer()
            .variavelService().findAllVariaveis()
        call.enqueue(object : Callback<List<Variavel>> {
            override fun onResponse(
                call: Call<List<Variavel>>,
                response: Response<List<Variavel>>
            ) {
                if (response.isSuccessful) {
                    var variaveis: List<Variavel> = response.body()!!
                    variaveis.forEach { variavel ->
                        listaVariaveis.add(variavel)
                    }
                }
            }

            override fun onFailure(call: Call<List<Variavel>>, t: Throwable) {
                Log.d("Resultado", "Falhou")
            }
        })
    }

    private fun configuraRecyclerViewVariaveis(adapter: AdapterVariavel) {
        val layout = LinearLayoutManager(this)
        recyclerViewVariavel.adapter = adapter
        recyclerViewVariavel.layoutManager = layout
        recyclerViewVariavel.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        recyclerViewVariavel.setHasFixedSize(true)
        adapter.onItemClick = { variavel ->
            startActivity(
                Intent(this, VariavelActivity::class.java)
                    .putExtra("variavel", variavel)
            )
        }

    }

    private fun configuraAdapter(list: List<Variavel>) {
        adapter = AdapterVariavel(list as MutableList<Variavel>)
        configuraRecyclerViewVariaveis(adapter)
    }

    private fun initcomponents() {
        recyclerViewVariavel = findViewById<RecyclerView>(R.id.recyclerViewVariavel)
        atualizaRecyclerViewVariaveis()
    }
}
