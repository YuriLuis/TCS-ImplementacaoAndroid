package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Interface
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InterfaceActivity : AppCompatActivity() {
    private lateinit var botaoAdicionar: Button
    private lateinit var botaoCancelar: Button
    private lateinit var botaoExlcuir: Button

    private lateinit var objInterface: Interface

    private lateinit var spinnerVariavel: Spinner
    private lateinit var editTextPergunta: EditText

    private lateinit var variaveis: List<Variavel>
    private val CAMPO_OBRIGATORIO = "Campo Obrigatório"
    private var novoItem = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interface)

        recuperaDadosIntent()
        initComponent()

        eventoBotaoAdicionar()
        eventoBotaoCancelar()
        eventoClickBotaoExcluir()
    }

    private fun getPositionSpinner(spinner: Spinner, keyValue: Int): Int {
        var position: Int = 0
        while (position < spinner.adapter.count) {
            if ((spinner.adapter.getItem(position) as Variavel).idVariavel == keyValue) {
                return position
            }
            position += 1
        }
        return -1
    }

    private fun initComponent() {
        spinnerVariavel = findViewById<Spinner>(R.id.spVariavelInterface)
        editTextPergunta = findViewById<EditText>(R.id.et_pergunta)

        botaoAdicionar = findViewById(R.id.btnAddIterface)
        botaoCancelar = findViewById(R.id.btnCancelarInterface)
        botaoExlcuir = findViewById(R.id.buttonExcluirInterface)

        preencheVariaveis()
        verificaSeEhEditadaOuNova()
    }

    private fun setaValores() {
        spinnerVariavel.setSelection(-1)
        editTextPergunta.setText("")
        if ((objInterface != null)  && (!novoItem)) {
            if (objInterface.variavel != null) {
                val ind = this.variaveis.indexOf(objInterface.variavel!!)
                spinnerVariavel.setSelection(ind)
                editTextPergunta.setText(objInterface.pergunta)
            }
        }
    }

    private fun configAdapterVariavel(variaveis: List<Variavel>): ArrayAdapter<Variavel> {
        var adapter: ArrayAdapter<Variavel> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, variaveis)
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
                    setaValores()
                }
            }
        })
    }

    private fun recuperaDadosIntent() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            objInterface = bundle.getSerializable("objInterface") as Interface
            novoItem = bundle.getBoolean("novo")
        }
    }

    private fun updateInterface() {
        var postInterface = Interface(
            objInterface.idInterface,
            spinnerVariavel.getItemAtPosition(spinnerVariavel.selectedItemId.toInt()) as Variavel,
            editTextPergunta.text.toString()
        )
        val call = RetrofitInitializer().Service().postInterface(postInterface)
        call.enqueue(object : Callback<Interface> {
            override fun onFailure(call: Call<Interface>, t: Throwable) {
                Toast.makeText(
                    this@InterfaceActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Interface>, response: Response<Interface>) {
                if (response.isSuccessful) {
                    objInterface = response.body()!!
                    recarregaEVoltaActivityAnterior()
                }
            }
        })
    }

    private fun postInterface() {
        var postInterface = Interface(
            null,
            spinnerVariavel.getItemAtPosition(spinnerVariavel.selectedItemId.toInt()) as Variavel,
            editTextPergunta.text.toString()
        )
        val call = RetrofitInitializer().Service().postInterface(postInterface)
        call.enqueue(object : Callback<Interface> {
            override fun onFailure(call: Call<Interface>, t: Throwable) {
                Toast.makeText(
                    this@InterfaceActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Interface>, response: Response<Interface>) {
                if (response.isSuccessful) {
                    objInterface = response.body()!!
                    recarregaEVoltaActivityAnterior()
                }
            }
        })
    }

    private fun verificaSeEhEditadaOuNova() {
        if (novoItem) {
            botaoExlcuir.visibility = View.INVISIBLE
            botaoCancelar.visibility = View.VISIBLE
            //NOVO
        } else {
            botaoExlcuir.visibility = View.VISIBLE
            botaoCancelar.visibility = View.VISIBLE
            //EDITAR
            botaoAdicionar.text = getString(R.string.editar)
        }
    }

    private fun deleteInterface() {
        var call = RetrofitInitializer().Service().deleteInterface(objInterface.idInterface!!)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@InterfaceActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@InterfaceActivity,
                        "Excluído com sucesso",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        })
    }

    private fun voltaActivityAnterior() {
        this.intent.putExtra("objInterface", objInterface)
        this.setResult(Activity.RESULT_OK, intent)
        this.finish()
    }

    private fun recarregaEVoltaActivityAnterior() {
        var call = RetrofitInitializer().Service().findByIdInterface(objInterface.idInterface!!)
        call.enqueue(object : Callback<Interface> {
            override fun onFailure(call: Call<Interface>, t: Throwable) {
                Toast.makeText(
                    this@InterfaceActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Interface>, response: Response<Interface>) {
                if (response.isSuccessful) {
                    objInterface = response.body()!!
                    voltaActivityAnterior()
                }
            }
        })
    }

    private fun eventoBotaoAdicionar() {
        botaoAdicionar.setOnClickListener {
            if (objInterface.idInterface == null) {
                postInterface()
            } else {
                updateInterface()
            }
        }
    }

    private fun eventoClickBotaoExcluir() {
        botaoExlcuir.setOnClickListener {
            deleteInterface()
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