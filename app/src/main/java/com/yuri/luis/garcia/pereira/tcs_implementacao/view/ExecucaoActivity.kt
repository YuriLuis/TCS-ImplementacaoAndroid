package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Execucao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExecucaoActivity : AppCompatActivity() {
    private lateinit var objExecucao: Execucao
    val checkBoxs: MutableList<String> = mutableListOf<String>()
    val checkBoxsSelec: MutableList<String> = mutableListOf<String>()
    var idImageSel: Int = 0


    private fun abreActivityResultado() {
        /* val i = Intent(this, ResultadoActivity::class.java)
         i.putExtra("idExecucao", objExecucao.idExecucao)
         startActivity(i)*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("CHRISTIAN", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_execucao)
      
        val param = intent.getStringExtra("idImage") ?: "0"
        Log.d("CHRISTIAN", "param: $param" )
        idImageSel = param.toInt()
        Log.d("CHRISTIAN", "idImageSel: $idImageSel")

        val btn = findViewById<Button>(R.id.bt_Seguinte)
        btn.setOnClickListener {
            if (validate()) {
                salvaRespostas()
                abreActivityResultado()
            }
        }
        iniciaExecucao()
    }

    private fun salvaRespostas() {
        var call = RetrofitInitializer().Service().adicionaRespostas(objExecucao?.idExecucao!!, checkBoxsSelec)
        call.enqueue(object : Callback<Execucao> {
            override fun onFailure(call: Call<Execucao>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou salvaRespostas: $t.message")
            }

            override fun onResponse(call: Call<Execucao>, response: Response<Execucao>) {
                Log.d("CHRISTIAN", "********** Retornou salvaRespostas *********:  $response.isSuccessful")
                Log.d("CHRISTIAN", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    Log.d("CHRISTIAN", "Capturando objeto...")
                    objExecucao = response.body()!!
                    Log.d("CHRISTIAN", "Retornou: $objExecucao")
                    Log.d("CHRISTIAN", "$objExecucao")
                }
            }

        })
    }

    private fun validate(): Boolean {
        var result = false
        val linearLayout = findViewById<LinearLayout>(R.id.layoutRadios)
        checkBoxs.forEach {
            val ck = linearLayout.findViewById<CheckBox>(it.toInt())
            if (ck.isChecked) {
                checkBoxsSelec.add(ck.id.toString())
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

    private fun getExecucao(idExecucao: Int?): Execucao {
        var execucao: Execucao = Execucao(null, null, null, emptyList(), null,null,null)
        var call = RetrofitInitializer().Service().getExecucao(idExecucao)
        call.enqueue(object : Callback<Execucao> {
            override fun onFailure(call: Call<Execucao>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou getExecucao: $t.message")
            }

            override fun onResponse(call: Call<Execucao>, response: Response<Execucao>) {
                Log.d("CHRISTIAN", "Retornou getExecucao: $response.isSuccessful")
                if (response.isSuccessful) {
                    execucao = response.body()!!
                    Log.d("CHRISTIAN", "$execucao")
                }
            }

        })
        return execucao;
    }

    private fun iniciaExecucao() {
        var call = RetrofitInitializer().Service().iniciaExecucao(idImageSel)

        //var call = RetrofitInitializer().Service().getExecucao(1)

        call.enqueue(object : Callback<Execucao> {
            override fun onFailure(call: Call<Execucao>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou Execucao: $t.message")
            }

            override fun onResponse(call: Call<Execucao>, response: Response<Execucao>) {
                Log.d("CHRISTIAN", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    objExecucao = response.body()!!
                    Log.d("CHRISTIAN", "Execucao(1): ${objExecucao.idExecucao}")
                    carregaPerguntas()
                }
            }
        })
    }

    private fun carregaPerguntas() {
        val linearLayout = findViewById<LinearLayout>(R.id.layoutRadios)
        val textViewPergunta = TextView(this)
        textViewPergunta.text = "Quais sintomas está ocorrendo nas folhas?"
        linearLayout.addView(textViewPergunta)
        Log.d("CHRISTIAN", "Carregando as regras...")
        for (regra in objExecucao.regras) {
            for (resposta in regra.respostas) {
                var valor = resposta.regraItem?.variavelValor
                val checkBox = CheckBox(this)

                checkBox.text = valor?.valor
                checkBox.id = valor?.idVariavelValor!!.toInt()
                if (objExecucao.image != null) {
                    if ((objExecucao.image!!.contornos!! > 0) && (objExecucao.image!!.manchas!! > 1) && (valor?.idVariavelValor!!.toInt() == 2)) {
                        checkBox.isChecked = true
                        checkBox.isEnabled = false
                        checkBox.text = valor?.valor + " (Identificado)"
                    }
                }
                linearLayout.addView(checkBox)
                checkBoxs.add(checkBox.id.toString())
            }
        }
        val checkBox = CheckBox(this)
        checkBox.text = "Não consigo ter certeza"
        checkBox.id = 1
        if (objExecucao.image != null) {
            if ((objExecucao.image!!.contornos!! > 0) && (objExecucao.image!!.manchas!! > 1)) {
            } else {
                linearLayout.addView(checkBox)
                checkBoxs.add(checkBox.id.toString())
            }
        } else {
            linearLayout.addView(checkBox)
            checkBoxs.add(checkBox.id.toString())
        }
    }

}
