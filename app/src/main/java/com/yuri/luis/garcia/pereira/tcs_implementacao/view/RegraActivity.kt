package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.app.Activity
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
import com.yuri.luis.garcia.pereira.tcs_implementacao.adapter.AdapterRegraItemResultado
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.*
import com.yuri.luis.garcia.pereira.tcs_implementacao.util.RecyclerItemClickListener
import kotlinx.android.synthetic.main.activity_regra.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegraActivity : AppCompatActivity() {

    private lateinit var nomeRegra: TextInputEditText
    private lateinit var ordemRegra: EditText

    private val CAMPO_OBRIGATORIO = "Campo Obrigatório"

    private lateinit var botaoAdicionar: Button
    private lateinit var botaoExcluir: Button
    private lateinit var botaoCancelar: Button

    private lateinit var recyclerViewRegra: RecyclerView
    private lateinit var recyclerViewRegraResultado: RecyclerView

    private var listaRegras: ArrayList<Regra> = mutableListOf<Regra>() as ArrayList<Regra>

    private lateinit var adapter: AdapterRegraItem
    private lateinit var adapterResultado: AdapterRegraItemResultado

    private var regra: Regra = Regra()
    private var novaRegra = false

    private lateinit var floatButtonAddItem: FloatingActionButton
    private lateinit var floatButtonAddItemResultado: FloatingActionButton

    val RETORNO_REGRA_ITEM = 200
    val RETORNO_REGRA_ITEM_RESULTADO = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regra)

        recuperaDadosIntentRegra()
        initComponents()
        carregaDadosApiRegra()
        ouvinteClickBotaoAdicionar()
        ouvinteClickBotaoCancelar()
        eventoClickRecyclerView()
        eventoClickRecyclerViewResultado()
        ouvinteClickBotaoExcluir()
        eventoClickFloatActionButtonAdicionarItem()
        eventoClickFloatActionButtonAdicionarItemResultado()
    }

    override fun onStart() {
        super.onStart()
        carregaDadosApiRegra()
        atualizaRecyclerViewRegras()
        atualizaRecyclerViewRegrasResultado()
        carregaDadosApiRegra()
    }

    private fun initComponents() {
        nomeRegra = findViewById(R.id.inputTextNomeRegra)
        ordemRegra = findViewById(R.id.et_ordem)

        botaoAdicionar = findViewById(R.id.buttonAddRegra)
        botaoCancelar = findViewById(R.id.buttonCancelar)
        botaoExcluir = findViewById(R.id.buttonExcluir)

        recyclerViewRegra = findViewById(R.id.recyclerViewRegra)
        recyclerViewRegraResultado = findViewById(R.id.recyclerViewRegraResultado)

        floatButtonAddItem = findViewById(R.id.floatingActionButtonAddItem)
        floatButtonAddItemResultado = findViewById(R.id.floatingActionButtonAddItemResultado)

        verificaSeRegraEhEditadaOuNova()
    }

    private fun verificaSeRegraEhEditadaOuNova() {
        nomeRegra.setText(regra.nome)
        ordemRegra.setText(regra.ordem.toString())
        if (!novaRegra) {
            atualizaRecyclerViewRegras()
            atualizaRecyclerViewRegrasResultado()
            botaoExcluir.visibility = View.VISIBLE
            botaoCancelar.visibility = View.VISIBLE
            /**EDITAR*/
            botaoAdicionar.text = getString(R.string.editar)
        } else {
            botaoExcluir.visibility = View.INVISIBLE
            botaoCancelar.visibility = View.INVISIBLE
            /**NOVA_VARIAVEL*/
        }

    }

    private fun atualizaRecyclerViewRegras() {
        var call = RetrofitInitializer().Service().RegrafindById(regra.idRegra)
        call.enqueue(object : Callback<Regra> {
            override fun onFailure(call: Call<Regra>, t: Throwable) { }
            override fun onResponse(call: Call<Regra>, response: Response<Regra>) {
                if (response.isSuccessful) {
                    Log.d("CHRISTIAN", "Recarregado lista de regras...")
                    var reg = response.body()!!
                    Log.d("CHRISTIAN", reg.itens.toString())
                    reg.itens?.let { configuraAdapter(it) }
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun atualizaRecyclerViewRegrasResultado() {
        var call = RetrofitInitializer().Service().RegrafindById(regra.idRegra)
        call.enqueue(object : Callback<Regra> {
            override fun onFailure(call: Call<Regra>, t: Throwable) { }
            override fun onResponse(call: Call<Regra>, response: Response<Regra>) {
                if (response.isSuccessful) {
                    var reg = response.body()!!
                    reg.resultados?.let { configuraAdapterResultado(it) }
                    adapterResultado.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (((requestCode == RETORNO_REGRA_ITEM) || (requestCode == RETORNO_REGRA_ITEM_RESULTADO)) && resultCode == RESULT_OK) {
            if (data != null) {
                val _regra = data.getSerializableExtra("regra") as Regra
                if (_regra != null) {
                    regra = _regra
                }
            }
        }
    }

    private fun carregaDadosApiRegra() {
        var call: Call<List<Regra>> = RetrofitInitializer().Service().findAllRegra()
        call.enqueue(object : Callback<List<Regra>> {
            override fun onResponse(call: Call<List<Regra>>, response: Response<List<Regra>>) {
                if (response.isSuccessful) {
                    listaRegras = response.body()!! as ArrayList<Regra>
                }
            }
            override fun onFailure(call: Call<List<Regra>>, t: Throwable) {  }
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
    }

    private fun configuraRecyclerViewRegraResultado(adapter: AdapterRegraItemResultado) {
        val layout = LinearLayoutManager(this)
        this.adapterResultado = adapter
        recyclerViewRegraResultado.adapter = adapter
        recyclerViewRegraResultado.layoutManager = layout
        recyclerViewRegraResultado.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayout.VERTICAL
            )
        )
        recyclerViewRegraResultado.setHasFixedSize(true)
    }

    private fun configuraAdapter(list: List<RegraItem>) {
        adapter = AdapterRegraItem(list as MutableList<RegraItem>)
        configuraRecyclerViewRegras(adapter)
    }

    private fun configuraAdapterResultado(list: List<RegraItemResultado>) {
        adapterResultado = AdapterRegraItemResultado(list as MutableList<RegraItemResultado>)
        configuraRecyclerViewRegraResultado(adapterResultado)
    }

    private fun voltaListaRegras() {
        this.setResult(Activity.RESULT_OK, intent)
        this.finish()
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
                if (response.isSuccessful) {
                    Toast.makeText(this@RegraActivity, "Excluído com sucesso", Toast.LENGTH_LONG)
                        .show()
                    Log.d("CHRISTIAN", "Regra deletada...")
                    voltaListaRegras()
                }
            }

        })
    }

    private fun updateRegra() {
        val postRegra =
            Regra(
                regra.idRegra,
                regra.ordem,
                nomeRegra.text.toString(),
                regra.dataRegra,
                regra.itens,
                regra.resultados
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
        val postRegra = Regra(null, ordemRegra.text.toString().toInt(), nomeRegra.text.toString(), "2020-05-31", null, null)
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
                if (response.isSuccessful) {
                    regra = response.body()!!
                }
            }
        })
    }

    private fun showActivityRegraItem(item: RegraItem) {
        val intent = Intent(this@RegraActivity, RegraItemActivity::class.java)
        intent.putExtra("regra", regra)
        intent.putExtra("regraItem", item)
        intent.putExtra("novo", item.idRegraItem == null)
        startActivityForResult(intent, RETORNO_REGRA_ITEM)
    }

    private fun abreActivityRegraItem(item: RegraItem) {
        if (regra.idRegra == null) {
            val postRegra = Regra(null, ordemRegra.text.toString().toInt(), nomeRegra.text.toString(), "2020-05-31", null, null)
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
                    if (response.isSuccessful) {
                        regra = response.body()!!
                        showActivityRegraItem(item)
                    }
                }
            })
        }
        else
        {
            showActivityRegraItem(item)
        }
    }

    private fun showActivityRegraItemResultado(item: RegraItemResultado) {
        val intent = Intent(this@RegraActivity, RegraItemResultadoActivity::class.java)
        intent.putExtra("regra", regra)
        intent.putExtra("regraItem", item)
        intent.putExtra("novo", item.idRegraItemResultado == null)
        startActivityForResult(intent, RETORNO_REGRA_ITEM_RESULTADO)
    }

    private fun abreActivityRegraItemResultado(item: RegraItemResultado) {
        if (regra.idRegra == null) {
            val postRegra = Regra(null, ordemRegra.text.toString().toInt(), nomeRegra.text.toString(), "2020-05-31", null, null)
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
                    if (response.isSuccessful) {
                        regra = response.body()!!
                        showActivityRegraItemResultado(item)
                    }
                }
            })
        }
        else
        {
            showActivityRegraItemResultado(item)
        }
    }

    private fun eventoClickRecyclerView() {
        recyclerViewRegra.addOnItemTouchListener(
            RecyclerItemClickListener(this,
                recyclerViewRegra,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onLongItemClick(view: View?, position: Int) {   }
                    override fun onItemClick(view: View?, position: Int) {
                        val item = (regra.itens?.get(position)!! ?: null) as RegraItem
                        abreActivityRegraItem(item)
                    }
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {    }
                })
        )
    }

    private fun eventoClickRecyclerViewResultado() {
        recyclerViewRegraResultado.addOnItemTouchListener(
            RecyclerItemClickListener(this,
                recyclerViewRegraResultado,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onLongItemClick(view: View?, position: Int) {   }
                    override fun onItemClick(view: View?, position: Int) {
                        val item = (regra.resultados?.get(position)!! ?: null) as RegraItemResultado
                        abreActivityRegraItemResultado(item)
                    }
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {    }
                })
        )
    }

    private fun eventoClickFloatActionButtonAdicionarItem() {
        floatButtonAddItem.setOnClickListener {
            abreActivityRegraItem(RegraItem())
        }
    }

    private fun eventoClickFloatActionButtonAdicionarItemResultado() {
        floatButtonAddItemResultado.setOnClickListener {
            abreActivityRegraItemResultado(RegraItemResultado())
        }
    }

    private fun ouvinteClickBotaoAdicionar() {
        botaoAdicionar.setOnClickListener {
            if (isCampoValido()) {
                if ((novaRegra) && (regra.idRegra == null)) {
                    postRegras()
                    atualizaRecyclerViewRegras()
                    atualizaRecyclerViewRegrasResultado()
                    this.setResult(Activity.RESULT_OK, intent)
                    this.finish()
                } else {
                    updateRegra()
                    this.setResult(Activity.RESULT_OK, intent)
                    this.finish()
                }
            }
        }
    }

    private fun ouvinteClickBotaoExcluir() {
        botaoExcluir.setOnClickListener {
            deleteRegra()
        }
    }

    private fun ouvinteClickBotaoCancelar() {
        botaoCancelar.setOnClickListener {
            this.finish()
        }
    }

    private fun isCampoInvalido(texto: String): Boolean {
        return texto.isNullOrEmpty()
    }

    private fun isCampoValido(): Boolean {
        var result: Boolean = true
        if (isCampoInvalido(nomeRegra.text.toString())) {
            nomeRegra.error = CAMPO_OBRIGATORIO
            result = false
        }
        if ((result) && (isCampoInvalido(ordemRegra.text.toString()))) {
            ordemRegra.error = CAMPO_OBRIGATORIO
            result = false
        }
        else {
            if ((result) && (ordemRegra.text.toString().toInt() <= 0)) {
                ordemRegra.error = CAMPO_OBRIGATORIO
                result = false
            }
        }

        if (result) {
            if (regra.itens != null) {
                result = regra.itens!!.size > 0
            }
            else {
                result = false
            }

            if (result) {
                if (regra.resultados != null) {
                    result = regra.resultados!!.size > 0
                } else {
                    result = false
                }
            }
            if (!result) {
                Toast.makeText(
                    this@RegraActivity,
                    "A regra não esta estruturada de maneira correta!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return result
    }

    private fun limpaCampos() {
        nomeRegra.setText("")
        ordemRegra.setText("")
    }

    private fun recuperaDadosIntentRegra() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            regra = bundle.getSerializable("regra") as Regra
            novaRegra = bundle.getBoolean("novo")
        }
    }

}