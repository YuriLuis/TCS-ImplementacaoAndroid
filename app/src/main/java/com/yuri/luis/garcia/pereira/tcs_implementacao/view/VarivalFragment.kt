package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.adapter.AdapterVariavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.enuns.TipoVariavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor
import com.yuri.luis.garcia.pereira.tcs_implementacao.util.RecyclerItemClickListener
import kotlinx.android.synthetic.main.fragment_varival.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class VarivalFragment : Fragment() {

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
    private lateinit var adapter: AdapterVariavel
    private var variavel: Variavel = Variavel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_varival, container, false)
        variavel = VarivalFragmentArgs.fromBundle(arguments!!).variavel!!

        initComponents(view)
        ouvinteClickBotaoAdicionar()
        ouvinteRadioGroup()
        eventoClickRecyclerView()
        return view
    }

    override fun onStart() {
        super.onStart()
        carregaDadosApiVariavel()
        configuraRecyclerViewVariaveis(this.listaVariaveis)
        atualizaRecyclerViewVariaveis()
    }

    private fun initComponents(view: View) {
        nomeVariavel = view.findViewById<TextInputEditText>(R.id.inputTextNomeVariavel)
        radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupOp)
        numerica = view.findViewById<RadioButton>(R.id.radioButtonNumerica)
        univalorada = view.findViewById<RadioButton>(R.id.radioButtonUnivalorada)
        multivalorada = view.findViewById<RadioButton>(R.id.radioButtonMultivalorada)
        botaoAdicionar = view.findViewById<Button>(R.id.buttonAddVariavel)
        recyclerViewVariavel = view.findViewById<RecyclerView>(R.id.recyclerViewVariavel)
        botaoCancelar = view.findViewById<Button>(R.id.buttonCancelar)
        botaoExcluir = view.findViewById<Button>(R.id.buttonExcluir)

        nomeVariavel.setText(variavel.nome)

        if (nomeVariavel.text.toString().isNotEmpty()) {
            botaoExcluir.visibility = View.VISIBLE
            botaoCancelar.visibility = View.VISIBLE
            /**EDITAR*/
            botaoAdicionar.text = getString(R.string.editar)
        } else {
            botaoExcluir.visibility = View.INVISIBLE
            botaoCancelar.visibility = View.INVISIBLE
            /**NOVA_VARIAVEL*/
            nomeVariavel.setText(variavel.nome)
        }

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

    private fun atualizaRecyclerViewVariaveis() {
        var call = RetrofitInitializer().variavelService().findAllVariaveis()
        call.enqueue(object : Callback<List<Variavel>> {
            override fun onFailure(call: Call<List<Variavel>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<List<Variavel>>,
                response: Response<List<Variavel>>
            ) {
                var variavel: List<Variavel> = response.body()!!
                configuraRecyclerViewVariaveis(variavel as MutableList<Variavel>)
                adapter.notifyDataSetChanged()
            }

        })
    }

    private fun configuraRecyclerViewVariaveis(variaveis: MutableList<Variavel>) {
        val layout = LinearLayoutManager(context)
        this.adapter = AdapterVariavel(variaveis)
        recyclerViewVariavel.adapter = adapter
        recyclerViewVariavel.layoutManager = layout
        recyclerViewVariavel.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayout.VERTICAL
            )
        )
        recyclerViewVariavel.setHasFixedSize(true)

    }

    private fun eventoClickRecyclerView() {
        recyclerViewVariavel.addOnItemTouchListener(RecyclerItemClickListener(
            context,
            recyclerViewVariavel,
            object : RecyclerItemClickListener.OnItemClickListener {
                override fun onLongItemClick(view: View?, position: Int) {
                    Toast.makeText(context, "Click Longo", Toast.LENGTH_LONG).show()
                }

                override fun onItemClick(view: View?, position: Int) {
                    Toast.makeText(context, "Click normal", Toast.LENGTH_LONG).show()

                }

                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Toast.makeText(context, "Click normal2", Toast.LENGTH_LONG).show()
                }

            }
        ))
    }

    private fun ouvinteClickBotaoAdicionar() {
        botaoAdicionar.setOnClickListener {
            if (isCampoValido()) {
                postVariaveis()
                limpaCampos()
                atualizaRecyclerViewVariaveis()
            }
        }
    }

    private fun postVariaveis() {
        verificaTipoDaVariavel()
        val postVariavel =
            Variavel(null, nomeVariavel.text.toString(), tipoVariavel.getTipo(), listaValor)

        val call = RetrofitInitializer().variavelService()
            .postVariavel(postVariavel)

        call.enqueue(object : Callback<Variavel> {
            override fun onFailure(call: Call<Variavel>, t: Throwable) {
                Toast.makeText(context, "Ocorreu um erro tente novamente", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Variavel>, response: Response<Variavel>) {
                Toast.makeText(context, "Variavel salva com sucesso!", Toast.LENGTH_LONG).show()
            }
        })
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

    private fun isCampoValido(): Boolean {
        if (isCampoInvalido(nomeVariavel.text.toString())) {
            nomeVariavel.error = CAMPO_OBRIGATORIO
            return false
        }

        if (tipoVariavelEhInvalido(tipoVariavel)) {
            Toast.makeText(context, "Informe o tipo da variável", Toast.LENGTH_LONG).show()
            return false
        }

        return true
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

    private fun limpaCampos() {
        nomeVariavel.setText("")
        numerica.isChecked = false
        univalorada.isChecked = false
        multivalorada.isChecked = false
    }

}
