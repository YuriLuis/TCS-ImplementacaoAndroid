package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
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
    private lateinit var textoValor: TextInputEditText
    private lateinit var botaoExlcuir: Button
    private lateinit var regra: Regra
    private lateinit var regraItem: RegraItem
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
        botaoAdicionarRegraItem = findViewById(R.id.btnAddValor)
        botaoCancelar = findViewById(R.id.btnCancelarValor)
        textoValor = findViewById(R.id.textInputValorVariavel)
        botaoExlcuir = findViewById(R.id.buttonExcluirValor)
        verificaSeRegraEhEditadaOuNova()
    }

    private fun recuperaDadosIntentRegra() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            regra = bundle.getSerializable("regra") as Regra
            regraItem = bundle.getSerializable("regraItem") as RegraItem
        }
    }

    private fun updateRegraItem() {
        val postRegraItem = RegraItem(regraItem.idRegraItem , 0, null, "", null, "", null)
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
        textoValor.setText(regraItem.variavel?.nome)

        if (isCampoInvalido(textoValor.text.toString())) {
            botaoExlcuir.visibility = View.INVISIBLE
            botaoCancelar.visibility = View.INVISIBLE
            isNewRegra = true
            /**NOVA_VARIAVEL*/
            textoValor.setText(regraItem.variavel?.nome)
        } else {
            botaoExlcuir.visibility = View.VISIBLE
            botaoCancelar.visibility = View.VISIBLE
            /**EDITAR*/
            botaoAdicionarRegraItem.text = getString(R.string.editar)
        }
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
        if (isCampoInvalido(textoValor.text.toString())) {
            textoValor.error = CAMPO_OBRIGATORIO
            return false
        }
        return true
    }

    private fun isCampoInvalido(texto: String): Boolean {
        return texto.isNullOrEmpty()
    }

}