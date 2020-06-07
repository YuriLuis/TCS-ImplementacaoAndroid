package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Interface
import com.yuri.luis.garcia.pereira.tcs_implementacao.adapter.AdapterVariavelInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaInterfaceActivity : AppCompatActivity() {

    private lateinit var adapter: AdapterVariavelInterface
    private lateinit var recyclerViewInterfaces: RecyclerView
    private var listaInterfaces: ArrayList<Interface> = mutableListOf<Interface>() as ArrayList<Interface>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_interface)

        initcomponents()
    }

    override fun onStart() {
        super.onStart()
        carregaDadosApiInterfaces()
        atualizaRecyclerViewInterfaces()
    }

    private fun atualizaRecyclerViewInterfaces() {
        var call = RetrofitInitializer().variavelService().findAllInterface()
        call.enqueue(object : Callback<List<Interface>> {
            override fun onFailure(call: Call<List<Interface>>, t: Throwable) {  }
            override fun onResponse(
                call: Call<List<Interface>>,
                response: Response<List<Interface>>
            ) {
                if (response.isSuccessful) {
                    var inter: List<Interface> = response.body()!!
                    configuraAdapter(inter)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun carregaDadosApiInterfaces() {
        var call: Call<List<Interface>> = RetrofitInitializer().Service().findAllInterface()
        call.enqueue(object : Callback<List<Interface>> {
            override fun onResponse(
                call: Call<List<Interface>>,
                response: Response<List<Interface>>
            ) {
                if (response.isSuccessful) {
                    var interfaces: List<Interface> = response.body()!!
                    interfaces.forEach { inter ->
                        listaInterfaces.add(inter)
                    }
                }
            }

            override fun onFailure(call: Call<List<Interface>>, t: Throwable) {
                Log.d("Resultado", "Falhou")
            }
        })
    }

    private fun configuraRecyclerViewInterfaces(adapter: AdapterVariavelInterface) {
        val layout = LinearLayoutManager(this)
        recyclerViewInterfaces.adapter = adapter
        recyclerViewInterfaces.layoutManager = layout
        recyclerViewInterfaces.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        recyclerViewInterfaces.setHasFixedSize(true)

        adapter.onItemClick = { inter ->
            startActivity(
                Intent(this, InterfaceActivity::class.java)
                    .putExtra("objInterface", inter)
                    .putExtra("novo", false)
            )
        }
    }

    fun eventoClickFloatActionButtonAddInterface(view: View) {
        startActivity(
            Intent(this, InterfaceActivity::class.java)
                .putExtra("objInterface", Interface())
                .putExtra("novo", true) )
    }

    private fun configuraAdapter(list: List<Interface>) {
        adapter = AdapterVariavelInterface(list as MutableList<Interface>)
        configuraRecyclerViewInterfaces(adapter)
    }

    private fun initcomponents() {
        recyclerViewInterfaces = findViewById<RecyclerView>(R.id.recyclerViewInterfaces)
        atualizaRecyclerViewInterfaces()
    }
}