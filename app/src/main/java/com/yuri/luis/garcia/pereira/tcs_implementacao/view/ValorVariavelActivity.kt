package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ValorVariavelActivity : AppCompatActivity() {

    private lateinit var botaoAdicionarValorVariavel: Button
    private lateinit var botaoCancelar: Button
    private lateinit var textoValor: TextInputEditText
    private lateinit var botaoExlcuir: Button
    private lateinit var variavel: Variavel
    private lateinit var valor: VariavelValor
    private val CAMPO_OBRIGATORIO = "Campo Obrigatório"
    private var isNewVariavel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valor_variavel)

        recuperaDadosIntentVariavel()
        initComponent()
        eventoBotaoAdicionar()
        eventoBotaoCancelar()
        eventoClickBotaoExcluir()
    }

    private fun initComponent() {
        botaoAdicionarValorVariavel = findViewById(R.id.btnAddValor)
        botaoCancelar = findViewById(R.id.btnCancelarValor)
        textoValor = findViewById(R.id.textInputValorVariavel)
        botaoExlcuir = findViewById(R.id.buttonExcluirValor)
        verificaSeVariavelEhEditadaOuNova()
    }

    private fun recuperaDadosIntentVariavel() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            variavel = bundle.getSerializable("variavel") as Variavel
            valor = bundle.getSerializable("valor") as VariavelValor
        }


    }

    private fun updateValor() {

        val postValorVariavelValor = VariavelValor(valor.idVariavelValor , textoValor.text.toString() , null)

        val call = RetrofitInitializer().variavelService()
            .postValorVariavel(variavel.idVariavel!!, postValorVariavelValor)

        call.enqueue(object : Callback<VariavelValor> {
            override fun onFailure(call: Call<VariavelValor>, t: Throwable) {
                Toast.makeText(
                    this@ValorVariavelActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<VariavelValor>, response: Response<VariavelValor>) {
            }
        })
    }

    private fun postValorVariavel() {
        val postValorVariavel =
            VariavelValor(null, textoValor.text.toString(), variavel)

        val call = RetrofitInitializer()
            .variavelService().postValorVariavel(variavel.idVariavel!!, postValorVariavel)

        call.enqueue(object : Callback<VariavelValor> {
            override fun onFailure(call: Call<VariavelValor>, t: Throwable) {
                Toast.makeText(
                    this@ValorVariavelActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<VariavelValor>, response: Response<VariavelValor>) {
                Toast.makeText(
                    this@ValorVariavelActivity,
                    "Variavel salva com sucesso!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun verificaSeVariavelEhEditadaOuNova() {
        textoValor.setText(valor.valor)

        if (isCampoInvalido(textoValor.text.toString())) {
            botaoExlcuir.visibility = View.INVISIBLE
            botaoCancelar.visibility = View.VISIBLE
            isNewVariavel = true
            /**NOVA_VARIAVEL*/
            textoValor.setText(valor.valor)
        } else {
            botaoExlcuir.visibility = View.VISIBLE
            botaoCancelar.visibility = View.VISIBLE
            /**EDITAR*/
            botaoAdicionarValorVariavel.text = getString(R.string.editar)
        }
    }

    private fun deleteVariavel() {
        var call =
            RetrofitInitializer().variavelService()
                .deleteValorVariavel(variavel.idVariavel!!, valor)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@ValorVariavelActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(
                    this@ValorVariavelActivity,
                    "Excluido com sucesso",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }

    private fun eventoBotaoAdicionar() {
        botaoAdicionarValorVariavel.setOnClickListener {
            if (valor == null) {
                postValorVariavel()
                Toast.makeText(
                    this@ValorVariavelActivity,
                    "Sucesso ao Salvar valor variavel!",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            } else {
                updateValor()
                Toast.makeText(
                    this@ValorVariavelActivity,
                    "Sucesso ao Atualizar Valor!!",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private fun eventoClickBotaoExcluir() {
        botaoExlcuir.setOnClickListener {
            deleteVariavel()
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
