package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Regra
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regra)

        findAllRegras()
        findAllPerguntas()
        salvaRegra()
    }

    private fun findAllRegras() {
        var call = RetrofitInitializer().service().findAllRegra()
        call.enqueue(object : Callback<List<Regra>> {
            override fun onFailure(call: Call<List<Regra>>, t: Throwable) {
                Toast.makeText(
                    this@RegraActivity,
                    "Get REGRA Falhou!", Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<List<Regra>>, response: Response<List<Regra>>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@RegraActivity,
                        "Get REGRA OK!", Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun findAllPerguntas() {
        var call = RetrofitInitializer().service().findAllPerguntas()
        call.enqueue(object : Callback<List<Regra>> {
            override fun onFailure(call: Call<List<Regra>>, t: Throwable) {
                Toast.makeText(
                    this@RegraActivity,
                    "Get PERGUNTAS Falhou!", Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<List<Regra>>, response: Response<List<Regra>>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@RegraActivity,
                        "Get PERGUNTA OK!", Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun salvaRegra() {
        val regra = Regra(null, "testando Denovo", "2020-05-31", null, null)
        val call = RetrofitInitializer().service().postRegra(regra)
        call.enqueue(object : Callback<Regra>{
            override fun onFailure(call: Call<Regra>, t: Throwable) {
                Toast.makeText(
                    this@RegraActivity,
                    "POST REGRA FALHOU!", Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Regra>, response: Response<Regra>) {
                if (response.isSuccessful){
                    Toast.makeText(
                        this@RegraActivity,
                        "POST REGRA OK!", Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}