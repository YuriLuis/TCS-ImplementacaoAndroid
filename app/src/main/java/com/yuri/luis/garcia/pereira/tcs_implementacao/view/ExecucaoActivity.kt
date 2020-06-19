package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Execucao
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Interface
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExecucaoActivity : AppCompatActivity() {
    private lateinit var objExecucao: Execucao
    val checkBoxs: MutableList<String> = mutableListOf<String>()
    var idImageSel: Int = 0

    var perguntas: MutableList<Interface> = mutableListOf<Interface>()
    var indexPergunta: Int = 0

    private lateinit var buttonVoltar: Button
    private lateinit var buttonSeguir: Button


    private fun abreActivityResultado() {
         val i = Intent(this, ResultadoAcitivity::class.java)
        Log.d("CHRISTIAN", "**** Enviando: " + objExecucao.idExecucao)
         i.putExtra("idExecucao", objExecucao.idExecucao.toString())
         startActivity(i)
    }

    private fun getPerguntaVariavel(variavel: Variavel) : String {
        var retorno = ""
        for (i in perguntas) {
            if (i.variavel!!.idVariavel == variavel.idVariavel) {
               retorno = i.pergunta
            }
        }
        return retorno
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("CHRISTIAN", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_execucao)
      
        val param = intent.getStringExtra("idImage") ?: "0"
        idImageSel = param.toInt()
        Log.d("CHRISTIAN", "idImageSel: $idImageSel")

        buttonVoltar = findViewById<Button>(R.id.bt_voltar)
        buttonVoltar.visibility = View.INVISIBLE
        buttonVoltar.setOnClickListener {
            perguntaAnterior()
        }
        buttonSeguir = findViewById<Button>(R.id.bt_Seguinte)
        buttonSeguir.setOnClickListener {
            if (validate()) {
                proximaPergunta()
            }
        }
        iniciaExecucao()
    }

    private fun postRespostas() {

        var call = RetrofitInitializer().Service().adicionaRespostas(objExecucao.idExecucao!!, getIdRespostas())
        //var call = RetrofitInitializer().Service().salvaExecucao(objExecucao)
        call.enqueue(object : Callback<Execucao> {
            override fun onFailure(call: Call<Execucao>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou salvaRespostas: $t.message")
            }

            override fun onResponse(call: Call<Execucao>, response: Response<Execucao>) {
                Log.d("CHRISTIAN", "********** Retornou salvaRespostas *********:  $response.isSuccessful")
                if (response.isSuccessful) {
                    Log.d("CHRISTIAN", "Capturando objeto...")
                    //objExecucao = response.body()!!
                    Log.d("CHRISTIAN", "Retornou: $objExecucao")
                    abreActivityResultado()
                }
            }

        })
    }

    private fun getIdRespostas(): MutableList<String> {
        var lista: MutableList<String> = mutableListOf<String>()
        for (regra in objExecucao.regras) {
            for (resposta in regra.respostas) {
                val pergunta = getPerguntaVariavel(resposta.regraItem?.variavel!!)
                if (pergunta.trim().length > 0) {
                    lista.add(resposta.idExecucaoRegraResposta.toString()+";"+resposta.resposta!!.idVariavelValor.toString())
                }
                else
                {
                    lista.add(resposta.idExecucaoRegraResposta.toString()+";"+"-1")
                }
            }
        }
        return lista
    }

    private fun salvaResposta(index: Int) {
        var _index: Int = -1
        var respostaValor: VariavelValor? = null
        for (regra in objExecucao.regras) {
            for (resposta in regra.respostas) {
                val pergunta = getPerguntaVariavel(resposta.regraItem?.variavel!!)
                if (pergunta.trim().length > 0) {
                    _index += 1
                    if (_index == index) {
                        val linearLayout = findViewById<LinearLayout>(R.id.layoutRadios)
                        checkBoxs.forEach {
                            val ck = linearLayout.findViewById<CheckBox>(it.toInt())
                            if (ck.isChecked) {
                                for (valor in resposta.regraItem?.variavel!!.valores) {
                                    if (valor.idVariavelValor == ck.id) {
                                        respostaValor = valor
                                    }
                                }
                            }
                        }
                        resposta.resposta = respostaValor
                        respostaValor = null
                    }
                }
            }
        }
    }

    private fun carregaPerguntas(index: Int) {
        var pergunta: String = ""
        val linearLayout = findViewById<LinearLayout>(R.id.layoutRadios)

        linearLayout.removeAllViews()
        checkBoxs.clear()

        val textViewPergunta = TextView(this)
        linearLayout.addView(textViewPergunta)

        buttonVoltar.visibility = View.INVISIBLE
        if (index > 0) {
            buttonVoltar.visibility = View.VISIBLE
        }
        var _index: Int = -1
        for (regra in objExecucao.regras) {
            for (resposta in regra.respostas) {
                pergunta = getPerguntaVariavel(resposta.regraItem?.variavel!!)
                if (pergunta.trim().length > 0) {
                    _index += 1
                    if (_index == index) {
                        textViewPergunta.text = pergunta
                        for (valor in resposta.regraItem?.variavel!!.valores) {
                            val checkBox = CheckBox(this)
                            checkBox.text = valor?.valor
                            checkBox.id = valor?.idVariavelValor!!.toInt()
                            if (resposta.resposta != null) {
                                checkBox.isChecked = (resposta!!.resposta!!.idVariavelValor == valor?.idVariavelValor!!)
                            }
                            linearLayout.addView(checkBox)
                            checkBoxs.add(checkBox.id.toString())
                        }
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        var result = false
        val linearLayout = findViewById<LinearLayout>(R.id.layoutRadios)
        checkBoxs.forEach {
            val ck = linearLayout.findViewById<CheckBox>(it.toInt())
            if (ck.isChecked) {
                Log.d("CHRISTIAN", "CheckID: " + it.toString())
                result = true
            }
        }
        if (!result) {
            AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage("Selecione uma opção!")
                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }.show()
        }
        return result
    }

    private fun iniciaExecucao() {
        var call = RetrofitInitializer().Service().iniciaExecucao(idImageSel)
        call.enqueue(object : Callback<Execucao> {
            override fun onFailure(call: Call<Execucao>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou Execucao: $t.message")
            }
            override fun onResponse(call: Call<Execucao>, response: Response<Execucao>) {
                Log.d("CHRISTIAN", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    objExecucao = response.body()!!
                    Log.d("CHRISTIAN", "Execucao(1): ${objExecucao.idExecucao}")
                    carregaInterfaces()
                }
            }
        })
    }

    private fun carregaInterfaces() {
        var call = RetrofitInitializer().Service().findInterfacesByRegra()
        call.enqueue(object : Callback<List<Interface>> {
            override fun onFailure(call: Call<List<Interface>>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou carregaInterfaces: $t.message")
            }
            override fun onResponse(call: Call<List<Interface>>, response: Response<List<Interface>>) {
                Log.d("CHRISTIAN", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    perguntas = response.body()!! as MutableList<Interface>
                    indexPergunta = 0
                    carregaPerguntas(indexPergunta)
                }
            }
        })
    }

    private fun perguntaAnterior() {
        if (indexPergunta == 0) { }
        else {
            salvaResposta(indexPergunta)
            indexPergunta -= 1
            carregaPerguntas(indexPergunta)
        }
    }

    private fun finalizaProcesso() {
        val alertDialog: AlertDialog? =this@ExecucaoActivity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("Sim",
                    DialogInterface.OnClickListener { dialog, id ->
                        postRespostas()
                    })
                setNegativeButton("Não",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }
            builder.setMessage("Deseja processar as informações ?")
            builder.create()
            builder.show()
        }
    }

    private fun proximaPergunta() {
        salvaResposta(indexPergunta)
        if (indexPergunta+1 >= perguntas.size) {
            finalizaProcesso()
        }
        else {
            indexPergunta += 1
            carregaPerguntas(indexPergunta)
        }
    }

}
