package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.adapter.AdapterRegraItem
import com.yuri.luis.garcia.pereira.tcs_implementacao.adapter.AdapterValorVariavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.enuns.TipoVariavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Regra
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.RegraItem
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor
import com.yuri.luis.garcia.pereira.tcs_implementacao.util.RecyclerItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegraActivity : AppCompatActivity() {

    private lateinit var nomeRegra: TextInputEditText
    private val CAMPO_OBRIGATORIO = "Campo Obrigatório"
    private lateinit var botaoAdicionar: Button
    private lateinit var botaoExcluir: Button
    private lateinit var botaoCancelar: Button
    private lateinit var recyclerViewRegra: RecyclerView
    private var listaItem: ArrayList<RegraItem> = mutableListOf<RegraItem>() as ArrayList<RegraItem>
    private var listaRegras: ArrayList<Regra> = mutableListOf<Regra>() as ArrayList<Regra>
    private lateinit var adapter: AdapterRegraItem
    private var regra: Regra = Regra()
    private var item: RegraItem = RegraItem()
    private var isNewRegra = false
    private lateinit var floatButtonAddItem: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regra)

        recuperaDadosIntentRegra()
        initComponents()
        carregaDadosApiRegra()
        ouvinteClickBotaoAdicionar()
        ouvinteClickBotaoCancelar()
        eventoClickRecyclerView()
        ouvinteClickBotaoExcluir()
        eventoClickFloatActionButtonAdicionarItem()
    }

    override fun onStart() {
        super.onStart()
        carregaDadosApiRegra()
        atualizaRecyclerViewRegras()
        carregaDadosApiRegra()
    }

    private fun initComponents() {
       /* nomeVariavel = findViewById(R.id.inputTextNomeVariavel)
        radioGroup = findViewById(R.id.radioGroupOp)
        numerica = findViewById(R.id.radioButtonNumerica)
        univalorada = findViewById(R.id.radioButtonUnivalorada)
        multivalorada = findViewById(R.id.radioButtonMultivalorada)
        botaoAdicionar = findViewById(R.id.buttonAddVariavel)
        recyclerViewVariavel = findViewById(R.id.recyclerViewVariavel)
        botaoCancelar = findViewById(R.id.buttonCancelar)
        botaoExcluir = findViewById(R.id.buttonExcluir)
        floatButtonAddValor = findViewById(R.id.floatingActionButtonAddValor)*/

        verificaSeRegraEhEditadaOuNova()
    }

    private fun verificaSeRegraEhEditadaOuNova() {
        nomeRegra.setText(regra.nome)

        if (item !=null){
            atualizaRecyclerViewRegras()
            botaoExcluir.visibility = View.VISIBLE
            botaoCancelar.visibility = View.VISIBLE
            /**EDITAR*/
            botaoAdicionar.text = getString(R.string.editar)
        }else {
            botaoExcluir.visibility = View.INVISIBLE
            botaoCancelar.visibility = View.INVISIBLE
            isNewRegra = true
            /**NOVA_VARIAVEL*/
            nomeRegra.setText(regra.nome)
        }

    }

    private fun atualizaRecyclerViewRegras() {
        var call = RetrofitInitializer().Service().RegrafindById(regra.idRegra)
        call.enqueue(object : Callback<Regra> {
            override fun onFailure(call: Call<Regra>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<Regra>,
                response: Response<Regra>
            ) {
                if (response.isSuccessful) {
                    var reg = response.body()!!
                    reg.itens?.let { configuraAdapter(it) }
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun carregaDadosApiRegra() {
        var call: Call<List<Regra>> = RetrofitInitializer().Service().findAllRegra()
        call.enqueue(object : Callback<List<Regra>> {
            override fun onResponse(
                call: Call<List<Regra>>,
                response: Response<List<Regra>>
            ) {
                if (response.isSuccessful) {
                    var regs: List<Regra> = response.body()!!
                    regs.forEach { r ->
                        listaRegras.add(r)
                        /**Salva dados da API no Array*/
                    }
                }
            }

            override fun onFailure(call: Call<List<Regra>>, t: Throwable) {
                Log.d("Resultado", "Falhou")
            }
        })
    }

    private fun configuraRecyclerViewRegras(adapter: AdapterRegraItem) {
        val layout = LinearLayoutManager(this)
        this.adapter = adapter
        recyclerViewRegra.adapter = adapter
        recyclerViewRegra.layoutManager = layout
        recyclerViewRegra.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayout.VERTICAL
            )
        )
        recyclerViewRegra.setHasFixedSize(true)
        /*
        adapter.onItemClick = { variavel ->
            startActivity(
                Intent(this, ValorVariavelActivity::class.java)
                    .putExtra("variavel", variavel)
                    .putExtra("valor", adapter.getteste())
            )
        }
         */
    }

    private fun eventoClickRecyclerView() {
        recyclerViewRegra.addOnItemTouchListener(
            RecyclerItemClickListener(this,
                recyclerViewRegra,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onLongItemClick(view: View?, position: Int) {

                    }

                    override fun onItemClick(view: View?, position: Int) {
                        if (item != null){
                            item = (regra.itens?.get(position)!!?:null) as RegraItem
                        }
                        startActivity(
                            Intent(this@RegraActivity, ValorVariavelActivity::class.java)
                                .putExtra("regra", regra)
                                .putExtra("item", item)
                        )
                    }

                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                    }

                })
        )
    }

    private fun configuraAdapter(list: List<RegraItem>) {
        adapter = AdapterRegraItem(list as MutableList<RegraItem>)
        configuraRecyclerViewRegras(adapter)
    }

    private fun deleteRegra() {
        var call = RetrofitInitializer().Service().deleteRegra(regra.idRegra!!)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@RegraActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(this@RegraActivity, "Excluido com sucesso", Toast.LENGTH_LONG)
                    .show()
            }

        })
    }

    private fun updateRegra() {
        val postRegra =
            Regra(
                regra.idRegra,
                nomeRegra.text.toString(),
                null,
                listaItem,
                null
            )

        val call = RetrofitInitializer().Service().postRegra(postRegra)
        call.enqueue(object : Callback<Regra> {
            override fun onFailure(call: Call<Regra>, t: Throwable) {
                Toast.makeText(
                    this@RegraActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Regra>, response: Response<Regra>) {
                Toast.makeText(
                    this@RegraActivity,
                    "Regra editada com sucesso!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun postRegras() {
        val postRegra = Regra(null, nomeRegra.text.toString(),null, listaItem, null)

        val call = RetrofitInitializer().Service().postRegra(postRegra)
        call.enqueue(object : Callback<Regra> {
            override fun onFailure(call: Call<Regra>, t: Throwable) {
                Toast.makeText(
                    this@RegraActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Regra>, response: Response<Regra>) {
                Toast.makeText(
                    this@RegraActivity,
                    "Regra salva com sucesso!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun eventoClickFloatActionButtonAdicionarItem() {
        floatButtonAddItem.setOnClickListener {
            startActivity(
                Intent(this, RegraItemActivity::class.java)
                    .putExtra("regra", regra)
                    .putExtra("item", item)
            )
        }
    }

    private fun ouvinteClickBotaoAdicionar() {
        botaoAdicionar.setOnClickListener {
            if (isCampoValido()) {
                if (isNewRegra) {
                    postRegras()
                    limpaCampos()
                    atualizaRecyclerViewRegras()
                    limpaCampos()
                    finish()
                } else {
                    updateRegra()
                    finish()
                }
            }
        }
    }

    private fun ouvinteClickBotaoExcluir() {
        botaoExcluir.setOnClickListener {
            deleteRegra()
            atualizaRecyclerViewRegras()
            finish()
        }
    }

    private fun ouvinteClickBotaoCancelar() {
        botaoCancelar.setOnClickListener {
            atualizaRecyclerViewRegras()
            finish()
        }
    }

    private fun isCampoInvalido(texto: String): Boolean {
        return texto.isNullOrEmpty()
    }

    private fun isCampoValido(): Boolean {
        if (isCampoInvalido(nomeRegra.text.toString())) {
            nomeRegra.error = CAMPO_OBRIGATORIO
            return false
        }
        return true
    }

    private fun limpaCampos() {
        nomeRegra.setText("")
    }

    private fun recuperaDadosIntentRegra() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            regra = bundle.getSerializable("regra") as Regra
        }
    }

    private fun findAllRegras() {
        var call = RetrofitInitializer().Service().findAllRegra()
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
        var call = RetrofitInitializer().Service().findAllPerguntas()
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
        val call = RetrofitInitializer().Service().postRegra(regra)
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