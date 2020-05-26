package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.adapter.AdapterValorVariavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.enuns.TipoVariavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VariavelActivity : AppCompatActivity() {

    private lateinit var nomeVariavel: TextInputEditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var numerica: RadioButton
    private lateinit var univalorada: RadioButton
    private lateinit var multivalorada: RadioButton
    private val CAMPO_OBRIGATORIO = "Campo Obrigatório"
    private var tipoVariavel = TipoVariavel.NAO_INFORMADO
    private lateinit var botaoAdicionar: Button
    private lateinit var botaoExcluir: Button
    private lateinit var botaoCancelar: Button
    private lateinit var recyclerViewVariavel: RecyclerView
    private var listaValor: ArrayList<VariavelValor> =
        mutableListOf<String>() as ArrayList<VariavelValor>
    private var listaVariaveis: ArrayList<Variavel> =
        mutableListOf<Variavel>() as ArrayList<Variavel>
    private lateinit var adapter: AdapterValorVariavel
    private var variavel: Variavel = Variavel()
    private var isNewVariavel = false
    private lateinit var floatButtonAddValor : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_variavel)

        recuperaDadosIntentVariavel()
        initComponents()
        carregaDadosApiVariavel()
        ouvinteClickBotaoAdicionar()
        ouvinteClickBotaoCancelar()
        ouvinteClickBotaoExcluir()
        eventoClickFloatActionButtonAdicionarValor()
        ouvinteRadioGroup()
    }

    override fun onStart() {
        super.onStart()
        carregaDadosApiVariavel()
        atualizaRecyclerViewVariaveis()
    }

    private fun initComponents() {
        nomeVariavel = findViewById(R.id.inputTextNomeVariavel)
        radioGroup = findViewById(R.id.radioGroupOp)
        numerica = findViewById(R.id.radioButtonNumerica)
        univalorada = findViewById(R.id.radioButtonUnivalorada)
        multivalorada = findViewById(R.id.radioButtonMultivalorada)
        botaoAdicionar = findViewById(R.id.buttonAddVariavel)
        recyclerViewVariavel = findViewById(R.id.recyclerViewVariavel)
        botaoCancelar = findViewById(R.id.buttonCancelar)
        botaoExcluir = findViewById(R.id.buttonExcluir)
        floatButtonAddValor = findViewById(R.id.floatingActionButtonAddValor)

        verificaSeVariavelEhEditadaOuNova()
    }

    private fun recuperaDadosIntentVariavel() {
        var bundle: Bundle? = intent.extras
        if (bundle != null){
            variavel = bundle.getSerializable("variavel") as Variavel
        }
    }

    private fun verificaSeVariavelEhEditadaOuNova() {
        nomeVariavel.setText(variavel.nome)

        if (nomeVariavel.text.toString().isNotEmpty()) {
            atualizaRecyclerViewVariaveis()
            botaoExcluir.visibility = View.VISIBLE
            botaoCancelar.visibility = View.VISIBLE
            /**EDITAR*/
            botaoAdicionar.text = getString(R.string.editar)
            verificaTipoDaVariavelPorId()

        } else {
            botaoExcluir.visibility = View.INVISIBLE
            botaoCancelar.visibility = View.INVISIBLE
            isNewVariavel = true
            /**NOVA_VARIAVEL*/
            nomeVariavel.setText(variavel.nome)
        }
    }

    private fun atualizaRecyclerViewVariaveis() {
        var call = RetrofitInitializer().variavelService().findById(variavel.idVariavel)
        call.enqueue(object : Callback<Variavel> {
            override fun onFailure(call: Call<Variavel>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<Variavel>,
                response: Response<Variavel>
            ) {
                if (response.isSuccessful) {
                    var variavel = response.body()!!
                    configuraAdapter(variavel.valores)
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
                        /**Salva dados da API no Array*/
                    }
                }
            }

            override fun onFailure(call: Call<List<Variavel>>, t: Throwable) {
                Log.d("Resultado", "Falhou")
            }
        })
    }

    private fun configuraRecyclerViewVariaveis(adapter: AdapterValorVariavel) {
        val layout = LinearLayoutManager(this)
        this.adapter = adapter
        recyclerViewVariavel.adapter = adapter
        recyclerViewVariavel.layoutManager = layout
        recyclerViewVariavel.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayout.VERTICAL
            )
        )

        recyclerViewVariavel.setHasFixedSize(true)
        adapter.onItemClick = { variavel ->
            startActivity(
                Intent(this, ValorVariavelActivity::class.java)
                    .putExtra("variavel", this.variavel)
                    .putExtra("valor", adapter.getteste())
            )
        }
    }

    private fun configuraAdapter(list: List<VariavelValor>) {
        adapter = AdapterValorVariavel(list as MutableList<VariavelValor>)
        configuraRecyclerViewVariaveis(adapter)
    }

    private fun verificaTipoDaVariavelPorId() {
        when {
            isTipoVariavel(0) -> {
                ouvinteRadioGroup().run { numerica.isChecked = true }
            }
            isTipoVariavel(1) -> {
                ouvinteRadioGroup().run { univalorada.isChecked = true }
                univalorada.isChecked = true
            }
            else -> {
                ouvinteRadioGroup().run { multivalorada.isChecked = true }
                multivalorada.isChecked = true
            }
        }
    }

    private fun deleteVariavel() {
        var call =
            RetrofitInitializer().variavelService().deleteVariavel(variavel.idVariavel!!)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@VariavelActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(this@VariavelActivity, "Excluido com sucesso", Toast.LENGTH_LONG)
                    .show()
            }

        })
    }

    private fun updateVariavel() {
        verificaTipoDaVariavel()
        val postVariavel =
            Variavel(
                variavel.idVariavel,
                nomeVariavel.text.toString(),
                tipoVariavel.getTipo(),
                listaValor
            )

        val call = RetrofitInitializer().variavelService()
            .postVariavel(postVariavel)

        call.enqueue(object : Callback<Variavel> {
            override fun onFailure(call: Call<Variavel>, t: Throwable) {
                Toast.makeText(
                    this@VariavelActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Variavel>, response: Response<Variavel>) {
                Toast.makeText(
                    this@VariavelActivity,
                    "Variavel editada com sucesso!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun postVariaveis() {
        verificaTipoDaVariavel()
        val postVariavel =
            Variavel(null, nomeVariavel.text.toString(), tipoVariavel.getTipo(), listaValor)

        val call = RetrofitInitializer().variavelService()
            .postVariavel(postVariavel)

        call.enqueue(object : Callback<Variavel> {
            override fun onFailure(call: Call<Variavel>, t: Throwable) {
                Toast.makeText(
                    this@VariavelActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Variavel>, response: Response<Variavel>) {
                Toast.makeText(
                    this@VariavelActivity,
                    "Variavel salva com sucesso!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun verificaTipoDaVariavel() {
        tipoVariavel = when {
            ehNumerico() -> {
                TipoVariavel.TIPO_NUMERICA
                /**(0)*/
            }
            ehUnivalorada() -> {
                TipoVariavel.TIPO_UNIVALORADA
                /**(1)*/
            }
            ehMultivalorada() -> {
                TipoVariavel.MULTIVALORA
                /**(2)*/
            }
            else -> {
                TipoVariavel.NAO_INFORMADO
                /**(3)*/
            }
        }
    }

    private fun eventoClickFloatActionButtonAdicionarValor(){
        floatButtonAddValor.setOnClickListener{
            startActivity(Intent(this, ValorVariavelActivity::class.java)
                .putExtra("variavel", variavel))
        }
    }

    private fun ouvinteClickBotaoAdicionar() {
        botaoAdicionar.setOnClickListener {
            if (isCampoValido()) {
                if (isNewVariavel) {
                    postVariaveis()
                    limpaCampos()
                    atualizaRecyclerViewVariaveis()
                    limpaCampos()
                    finish()
                } else {
                    updateVariavel()
                    finish()
                }
            }
        }
    }

    private fun ouvinteClickBotaoExcluir() {
        botaoExcluir.setOnClickListener {
            deleteVariavel()
            atualizaRecyclerViewVariaveis()
            finish()
        }
    }

    private fun ouvinteClickBotaoCancelar() {
        botaoCancelar.setOnClickListener {
            atualizaRecyclerViewVariaveis()
            finish()
        }
    }

    private fun ouvinteRadioGroup() {
        radioGroup.setOnCheckedChangeListener { _, checkId ->
            tipoVariavel =
                when (checkId) {
                    R.id.radioButtonNumerica -> TipoVariavel.TIPO_NUMERICA
                    R.id.radioButtonUnivalorada -> TipoVariavel.TIPO_UNIVALORADA
                    R.id.radioButtonMultivalorada -> TipoVariavel.MULTIVALORA
                    else -> TipoVariavel.NAO_INFORMADO
                }
        }
    }

    private fun isTipoVariavel(numero: Int): Boolean {
        return variavel.tipoVariavel == numero
    }

    private fun isCampoInvalido(texto: String): Boolean {
        return texto.isNullOrEmpty()
    }

    private fun ehNumerico(): Boolean {
        return numerica.isChecked
    }

    private fun ehUnivalorada(): Boolean {
        return univalorada.isChecked
    }

    private fun ehMultivalorada(): Boolean {
        return multivalorada.isChecked
    }

    private fun tipoVariavelEhInvalido(tipoVariavel: TipoVariavel): Boolean {
        return tipoVariavel.getTipo() >= 3
    }

    private fun isCampoValido(): Boolean {
        if (isCampoInvalido(nomeVariavel.text.toString())) {
            nomeVariavel.error = CAMPO_OBRIGATORIO
            return false
        }

        if (tipoVariavelEhInvalido(tipoVariavel)) {
            Toast.makeText(this, "Informe o tipo da variável", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun limpaCampos() {
        nomeVariavel.setText("")
        numerica.isChecked = false
        univalorada.isChecked = false
        multivalorada.isChecked = false
    }
}
