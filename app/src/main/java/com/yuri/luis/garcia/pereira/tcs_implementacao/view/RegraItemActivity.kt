package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Regra
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.RegraItem
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegraItemActivity : AppCompatActivity() {
    private lateinit var botaoAdicionarRegraItem: Button
    private lateinit var botaoCancelar: Button
    private lateinit var botaoExlcuir: Button

    private lateinit var regra: Regra
    private lateinit var regraItem: RegraItem

    private lateinit var spinnerCondicional: Spinner
    private lateinit var spinnerVariavel: Spinner
    private lateinit var spinnerConectivo: Spinner
    private lateinit var spinnerValor: Spinner

    private val CAMPO_OBRIGATORIO = "Campo Obrigatório"
    private var isNewRegra = false

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_regra_item)

        recuperaDadosIntentRegra()
        initComponent()
        eventoBotaoAdicionar()
        eventoBotaoCancelar()
        eventoClickBotaoExcluir()
    }

    private fun initComponent() {
        spinnerConectivo = findViewById<Spinner>(R.id.spConectivo)
        spinnerVariavel = findViewById<Spinner>(R.id.spVariavel)
        spinnerCondicional = findViewById<Spinner>(R.id.spCondicional)
        spinnerValor = findViewById<Spinner>(R.id.spValor)

        botaoAdicionarRegraItem = findViewById(R.id.btnAddItem)
        botaoCancelar = findViewById(R.id.btnCancelarItem)
        botaoExlcuir = findViewById(R.id.buttonExcluirItem)

        ArrayAdapter.createFromResource(
            this,
            R.array.lista_Conectivo,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerConectivo.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.lista_Condicional,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCondicional.adapter = adapter
        }

        verificaSeRegraEhEditadaOuNova()
    }

    private fun preencheVariaveis() {
        var call = RetrofitInitializer().variavelService().findAllVariaveis()
        call.enqueue(object : Callback<List<Variavel>> {
            override fun onFailure(call: Call<List<Variavel>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<Variavel>>,
                response: Response<List<Variavel>>
            ) {
                if (response.isSuccessful) {
                    val listaVariaveis: List<Variavel>? = response.body()

                 /*   val listaSpinner = arrayOf("Gasolina Comum", "Gasolina Aditivada", "Etanol", "Diesel")
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaSpinner)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter

                    var list = ArrayList<String>()
                    val aa = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list)

                    var list_of_items = arrayOf("Item 1", "Item 2", "Item 3")
                    //val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, [])
                        //ArrayAdapter(this, android.R.layout.simple_spinner_item, listaVariaveis)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerVariavel!!.setAdapter(adapter)*/
                }
            }
        })
    }

    private fun getCondicional(): String {
        when(spinnerCondicional.selectedItemId.toInt()){
            0 -> return "="
            1 -> return "<>"
            2 -> return ">"
            3 -> return ">="
            4 -> return "<"
            5 -> return "<="
        }
        return ""
    }

    private fun recuperaDadosIntentRegra() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            regra = bundle.getSerializable("regra") as Regra
            regraItem = bundle.getSerializable("regraItem") as RegraItem
        }
    }

    private fun updateRegraItem() {
        val postRegraItem = RegraItem(regraItem.idRegraItem , spinnerConectivo.selectedItemId.toInt(), null, getCondicional(), null, "", regra)
        val call = RetrofitInitializer().Service().postRegraItem(regra.idRegra!!, postRegraItem)

        call.enqueue(object : Callback<RegraItem> {
            override fun onFailure(call: Call<RegraItem>, t: Throwable) {
                Toast.makeText(
                    this@RegraItemActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<RegraItem>, response: Response<RegraItem>) {
            }
        })
    }

    private fun postRegraItem() {
        val postRegraItem = RegraItem(null, 0,null,"",null,"", regra)
        val call = RetrofitInitializer().Service().postRegraItem(regra.idRegra!!, postRegraItem)
        call.enqueue(object : Callback<RegraItem> {
            override fun onFailure(call: Call<RegraItem>, t: Throwable) {
                Toast.makeText(
                    this@RegraItemActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<RegraItem>, response: Response<RegraItem>) {
                Toast.makeText(
                    this@RegraItemActivity,
                    "Item salva com sucesso!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun verificaSeRegraEhEditadaOuNova() {
        /*textoItem.setText(regraItem.variavel?.nome)
        if (isCampoInvalido(textoItem.text.toString())) {
            botaoExlcuir.visibility = View.INVISIBLE
            botaoCancelar.visibility = View.INVISIBLE
            isNewRegra = true
            //NOVA_VARIAVEL
            //textoItem.setText(regraItem.variavel?.nome)
        } else {
            botaoExlcuir.visibility = View.VISIBLE
            botaoCancelar.visibility = View.VISIBLE
            //EDITAR
            botaoAdicionarRegraItem.text = getString(R.string.editar)
        }*/
    }

    private fun deleteRegraItem() {
        var call = RetrofitInitializer().Service().deleteRegraItem(regra.idRegra!!, regraItem)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@RegraItemActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(
                    this@RegraItemActivity,
                    "Excluido com sucesso",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }

    private fun eventoBotaoAdicionar() {
        botaoAdicionarRegraItem.setOnClickListener {
            if (regraItem == null) {
                postRegraItem()
                Toast.makeText(
                    this@RegraItemActivity,
                    "Sucesso ao Salvar valor variavel!",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            } else {
                updateRegraItem()
                Toast.makeText(
                    this@RegraItemActivity,
                    "Sucesso ao Atualizar Valor!!",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private fun eventoClickBotaoExcluir() {
        botaoExlcuir.setOnClickListener {
            deleteRegraItem()
            finish()
        }
    }

    private fun eventoBotaoCancelar() {
        botaoCancelar.setOnClickListener {
            finish()
        }
    }

    private fun isCampoValido(): Boolean {
       /* if (isCampoInvalido(textoItem.text.toString())) {
            textoItem.error = CAMPO_OBRIGATORIO
            return false
        }*/
        return true
    }

    private fun isCampoInvalido(texto: String): Boolean {
        return texto.isNullOrEmpty()
    }

}