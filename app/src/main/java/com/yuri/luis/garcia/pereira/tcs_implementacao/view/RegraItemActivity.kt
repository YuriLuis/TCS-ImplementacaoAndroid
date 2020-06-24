package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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

    private lateinit var variaveis: List<Variavel>

    private val CAMPO_OBRIGATORIO = "Campo Obrigatório"
    private var novoItem = false

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

        preencheVariaveis()

        spinnerVariavel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                preencheValoresVariavel()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        verificaSeRegraEhEditadaOuNova()
    }

    private fun preencheCampos() {
        spinnerConectivo.setSelection(-1)
        spinnerVariavel.setSelection(-1)
        spinnerCondicional.setSelection(-1)
        spinnerValor.setSelection(-1)
        if ((regraItem != null) && (!novoItem)) {
            spinnerConectivo.setSelection(regraItem.conectivo)
            spinnerCondicional.setSelection(getIndexCondicional(regraItem.condicional))

            val indexVariavel = this.variaveis.indexOf(regraItem.variavel!!)
            spinnerVariavel.setSelection(indexVariavel)
        }
    }

    private fun setaValores() {
        if ((regraItem != null) && (!novoItem)) {
            val indexValor = regraItem.variavel!!.valores.indexOf(regraItem.variavelValor!!)
            spinnerValor.setSelection(indexValor)
        }
    }

    private fun configAdapterVariavel(variaveis: List<Variavel>): ArrayAdapter<Variavel> {
        var adapter: ArrayAdapter<Variavel> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, variaveis)
        return adapter
    }

    private fun configAdapterValoresVariavel(valores: List<VariavelValor>): ArrayAdapter<VariavelValor> {
        var adapter: ArrayAdapter<VariavelValor> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, valores)
        return adapter
    }

    private fun preencheVariaveis() {
        var call = RetrofitInitializer().variavelService().findAllVariaveis()
        call.enqueue(object : Callback<List<Variavel>> {
            override fun onFailure(call: Call<List<Variavel>>, t: Throwable) { }

            override fun onResponse(call: Call<List<Variavel>>,response: Response<List<Variavel>>) {
                if (response.isSuccessful) {
                    variaveis = response.body()!!
                    spinnerVariavel.adapter = variaveis?.let { configAdapterVariavel(it) }
                    preencheCampos()
                }
            }
        })
    }

    private fun preencheValoresVariavel() {
        if (spinnerVariavel.selectedItemId > -1) {
            val variavel =
                spinnerVariavel.getItemAtPosition(spinnerVariavel.selectedItemId.toInt()) as Variavel
            spinnerValor.adapter = variavel?.let {
                configAdapterValoresVariavel(variavel.valores)
            }
            setaValores()
        }
    }

    private fun getCondicional(): String {
        when (spinnerCondicional.selectedItemId.toInt()) {
            0 -> return "="
            1 -> return "<>"
            2 -> return ">"
            3 -> return ">="
            4 -> return "<"
            5 -> return "<="
        }
        return ""
    }

    private fun getIndexCondicional(condicional: String): Int {
        var str: Int = -1
        if (condicional.equals("=")) {
            str = 0
        }
        if (condicional.equals("<>")) {
            str = 1
        }
        if (condicional.equals(">")) {
            str = 2
        }
        if (condicional.equals(">=")) {
            str = 3
        }
        if (condicional.equals("<")) {
            str = 4
        }
        if (condicional.equals("<=")) {
            str = 5
        }
        return str
    }

    private fun recuperaDadosIntentRegra() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            regra = bundle.getSerializable("regra") as Regra
            regraItem = bundle.getSerializable("regraItem") as RegraItem
            novoItem = bundle.getBoolean("novo")
        }
    }

    private fun updateRegraItem() {
        var postRegraItem = RegraItem(
            regraItem.idRegraItem,
            spinnerConectivo.selectedItemId.toInt(),
            spinnerVariavel.getItemAtPosition(spinnerVariavel.selectedItemId.toInt()) as Variavel,
            getCondicional(),
            spinnerValor.getItemAtPosition(spinnerValor.selectedItemId.toInt()) as VariavelValor,
            regra
        )
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
                if (response.isSuccessful) {
                    regraItem = response.body()!!
                    recarregaRegraEVoltaActivityAnterior()
                }
            }
        })
    }

    private fun postRegraItem() {
        var postRegraItem = RegraItem(
            null,
            spinnerConectivo.selectedItemId.toInt(),
            spinnerVariavel.getItemAtPosition(spinnerVariavel.selectedItemId.toInt()) as Variavel,
            getCondicional(),
            spinnerValor.getItemAtPosition(spinnerValor.selectedItemId.toInt()) as VariavelValor,
            regra
        )
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
                if (response.isSuccessful) {
                    regra.itens?.add(response.body()!!)
                    recarregaRegraEVoltaActivityAnterior()
                }
            }
        })
    }

    private fun verificaSeRegraEhEditadaOuNova() {
        if (novoItem) {
            botaoExlcuir.visibility = View.INVISIBLE
            botaoCancelar.visibility = View.VISIBLE
            //NOVO
        } else {
            botaoExlcuir.visibility = View.VISIBLE
            botaoCancelar.visibility = View.VISIBLE
            //EDITAR
            botaoAdicionarRegraItem.text = getString(R.string.editar)
        }
    }

    private fun deleteRegraItem() {
        var call = RetrofitInitializer().Service().deleteRegraItem(regra.idRegra!!, regraItem)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("CHRISTIAN", "$t.message")
                Toast.makeText(
                    this@RegraItemActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("CHRISTIAN",response.toString())
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@RegraItemActivity,
                        "Excluído com sucesso",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        })
    }

    private fun voltaActivityAnterior() {
        this.intent.putExtra("regra", regra)
        this.setResult(Activity.RESULT_OK, intent)
        this.finish()
    }

    private fun recarregaRegraEVoltaActivityAnterior() {
        var call = RetrofitInitializer().Service().RegrafindById(regra.idRegra!!)
        call.enqueue(object : Callback<Regra> {
            override fun onFailure(call: Call<Regra>, t: Throwable) {
                Toast.makeText(
                    this@RegraItemActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Regra>, response: Response<Regra>) {
                if (response.isSuccessful) {
                    regra = response.body()!!
                    voltaActivityAnterior()
                }
            }
        })
    }

    private fun eventoBotaoAdicionar() {
        botaoAdicionarRegraItem.setOnClickListener {
            if (regraItem.idRegraItem == null) {
                postRegraItem()
            } else {
                updateRegraItem()
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