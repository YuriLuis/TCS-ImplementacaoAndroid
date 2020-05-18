package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.adapter.AdapterVariavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.enuns.TipoVariavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor
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
    private  var tipoVariavel = TipoVariavel.NAO_INFORMADO
    private lateinit var botaoAdicionar : Button
    private lateinit var recyclerViewVariavel : RecyclerView
    private var listaValor: ArrayList<VariavelValor> =
        mutableListOf<String>() as ArrayList<VariavelValor>
    private var listaVariaveis : ArrayList<Variavel> =
        mutableListOf<Variavel>() as ArrayList<Variavel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_varival, container, false)
        initComponents(view)
        carregaDadosApiVariavel(view)
        listaVariaveisRecyclerView(this.listaVariaveis)

        ouvinteRadioGroup(view)
        return view
    }

    private fun postVariaveis(view: View) {
        verificaTipoDaVariavel()
        var postVariavel =
            Variavel(null, nomeVariavel.text.toString(), tipoVariavel.getTipo(), listaValor)

        var call = RetrofitInitializer().variavelService()
            .postVariavel(postVariavel)

        call.enqueue(object : Callback<Variavel> {
            override fun onFailure(call: Call<Variavel>, t: Throwable) {
                Toast.makeText(context, "Falhou", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Variavel>, response: Response<Variavel>) {
                Toast.makeText(context, "Salvou", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun listaVariaveisRecyclerView(variaveis : MutableList<Variavel>){
        val layout = LinearLayoutManager(context)
        val adapter =
            AdapterVariavel(variaveis)
        recyclerViewVariavel.adapter = adapter
        recyclerViewVariavel.layoutManager = layout
        recyclerViewVariavel.setHasFixedSize(true)
    }

    private fun carregaDadosApiVariavel(view : View){
        val call: Call<List<Variavel>> = RetrofitInitializer()
            .variavelService().getVariavel()
        call.enqueue(object : Callback<List<Variavel>> {
            override fun onResponse(
                call: Call<List<Variavel>>,
                response: Response<List<Variavel>>
            ) {
                if (response.isSuccessful) {
                    var variaveis: List<Variavel> = response.body()!!
                    variaveis.forEach { variavel -> // Preenche lista com nomes variaveis
                        listaVariaveis.add(variavel)
                    }
                }
            }

            override fun onFailure(call: Call<List<Variavel>>, t: Throwable) {
                Log.d("Resultado", "Falhou")
            }
        })
    }

    private fun ouvinteRadioGroup(view: View){
        radioGroup.setOnCheckedChangeListener{_, checkId ->
            tipoVariavel =
                when(checkId){
                    R.id.radioButtonNumerica -> TipoVariavel.TIPO_NUMERICA
                    R.id.radioButtonUnivalorada -> TipoVariavel.TIPO_UNIVALORADA
                    R.id.radioButtonMultivalorada -> TipoVariavel.MULTIVALORA
                    else -> TipoVariavel.NAO_INFORMADO
                }
        }
    }

    private fun initComponents(view : View) {
        nomeVariavel = view.findViewById<TextInputEditText>(R.id.inputTextNomeVariavel)
        radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupOp)
        numerica = view.findViewById<RadioButton>(R.id.radioButtonNumerica)
        univalorada = view.findViewById<RadioButton>(R.id.radioButtonUnivalorada)
        multivalorada = view.findViewById<RadioButton>(R.id.radioButtonMultivalorada)
        botaoAdicionar = view.findViewById<Button>(R.id.buttonAddVariavel)
        recyclerViewVariavel = view.findViewById<RecyclerView>(R.id.recyclerViewVariavel)

        botaoAdicionar.setOnClickListener {

            if (isCampoValido()){
                postVariaveis(it)
            }
        }
    }



    private fun isCampoValido(): Boolean {
        if (isCampoInvalido(nomeVariavel.text.toString())) {
            nomeVariavel.error = CAMPO_OBRIGATORIO
            return false
        }

        if (tipoVariavelEhInvalido(tipoVariavel)) {
            Toast.makeText(context, "Please long press the key", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun verificaTipoDaVariavel() {
        tipoVariavel = when {
            ehNumerico() -> {
                TipoVariavel.TIPO_NUMERICA
            }
            ehUnivalorada() -> {
                TipoVariavel.TIPO_UNIVALORADA
            }
            ehMultivalorada() -> {
                TipoVariavel.MULTIVALORA
            }
            else -> {
                TipoVariavel.NAO_INFORMADO
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

}
