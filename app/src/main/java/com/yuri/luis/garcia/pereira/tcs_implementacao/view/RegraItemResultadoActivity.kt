package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegraItemResultadoActivity : AppCompatActivity() {
    private lateinit var botaoAdicionarRegraItem: Button
    private lateinit var botaoCancelar: Button
    private lateinit var botaoExlcuir: Button

    private lateinit var regra: Regra
    private lateinit var regraItem: RegraItemResultado

    private lateinit var spinnerVariavel: Spinner
    private lateinit var spinnerValor: Spinner
    private lateinit var editTextFatorConfianca: EditText

    private lateinit var variaveis: List<Variavel>

    private val CAMPO_OBRIGATORIO = "Campo Obrigatório"
    private var novoItem = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regra_item_resultado)

        recuperaDadosIntentRegra()
        initComponent()

        eventoBotaoAdicionar()
        eventoBotaoCancelar()
        eventoClickBotaoExcluir()
    }

    private fun initComponent() {
        spinnerVariavel = findViewById<Spinner>(R.id.spVariavelResultado)
        spinnerValor = findViewById<Spinner>(R.id.spValorResultado)
        editTextFatorConfianca = findViewById<EditText>(R.id.et_fatorConfiaca)

        botaoAdicionarRegraItem = findViewById(R.id.btnAddItemResultado)
        botaoCancelar = findViewById(R.id.btnCancelarItemResultado)
        botaoExlcuir = findViewById(R.id.buttonExcluirItemResultado)

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

            override fun onNothingSelected(parent: AdapterView<*>) {  }
        }
        verificaSeRegraEhEditadaOuNova()
    }

    private fun preencheCampos() {
        spinnerVariavel.setSelection(-1)
        spinnerValor.setSelection(-1)
        editTextFatorConfianca.setText("")
        if ((regraItem != null) && (!novoItem)) {
            editTextFatorConfianca.setText(regraItem.fatorConfianca.toString())

            var index = this.variaveis.indexOf(regraItem.variavel!!)
            spinnerVariavel.setSelection(index)
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

            override fun onResponse(call: Call<List<Variavel>>, response: Response<List<Variavel>>) {
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

    private fun recuperaDadosIntentRegra() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            regra = bundle.getSerializable("regra") as Regra
            regraItem = bundle.getSerializable("regraItem") as RegraItemResultado
            novoItem = bundle.getBoolean("novo")
        }
    }

    private fun updateRegraItem() {
        var postRegraItem = RegraItemResultado(
            regraItem.idRegraItemResultado,
            spinnerVariavel.getItemAtPosition(spinnerVariavel.selectedItemId.toInt()) as Variavel,
            spinnerValor.getItemAtPosition(spinnerValor.selectedItemId.toInt()) as VariavelValor,
            editTextFatorConfianca.text.toString().toDouble(),
            regra
        )
        val call = RetrofitInitializer().Service().postRegraItemResultado(regra.idRegra!!, postRegraItem)
        call.enqueue(object : Callback<RegraItemResultado> {
            override fun onFailure(call: Call<RegraItemResultado>, t: Throwable) {
                Toast.makeText(
                    this@RegraItemResultadoActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<RegraItemResultado>, response: Response<RegraItemResultado>) {
                if (response.isSuccessful) {
                    regraItem = response.body()!!
                    recarregaRegraEVoltaActivityAnterior()
                }
            }
        })
    }

    private fun postRegraItem() {
        var postRegraItem = RegraItemResultado(
            null,
            spinnerVariavel.getItemAtPosition(spinnerVariavel.selectedItemId.toInt()) as Variavel,
            spinnerValor.getItemAtPosition(spinnerValor.selectedItemId.toInt()) as VariavelValor,
            editTextFatorConfianca.text.toString().toDouble(),
            regra
        )
        val call = RetrofitInitializer().Service().postRegraItemResultado(regra.idRegra!!, postRegraItem)
        call.enqueue(object : Callback<RegraItemResultado> {
            override fun onFailure(call: Call<RegraItemResultado>, t: Throwable) {
                Toast.makeText(
                    this@RegraItemResultadoActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<RegraItemResultado>, response: Response<RegraItemResultado>) {
                if (response.isSuccessful) {
                    regra.resultados?.add(response.body()!!)
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
        var call = RetrofitInitializer().Service().deleteRegraItemResultado(regra.idRegra!!, regraItem)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@RegraItemResultadoActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@RegraItemResultadoActivity,
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
                    this@RegraItemResultadoActivity,
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
            if (regraItem.idRegraItemResultado == null) {
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