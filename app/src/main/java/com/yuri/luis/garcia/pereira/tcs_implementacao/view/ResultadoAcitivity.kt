package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Execucao
import kotlinx.android.synthetic.main.activity_resultado_acitivity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ResultadoAcitivity : AppCompatActivity() {

    var idExecucaoSel: Int = 0
    private val TAG_SISTEMA = "CHRISTIAN";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_acitivity)
        textViewLabelFoto.visibility = View.INVISIBLE
        TextDate.text = ""
        textPercent.text = ""
        imageViewResult.visibility = View.INVISIBLE
        buttonConfirm.setOnClickListener { confirmar() }
        val param = intent.getStringExtra("idExecucao") ?: "0"
        idExecucaoSel = param.toInt()
        Log.d(TAG_SISTEMA, "idExecucaoSel: $idExecucaoSel" )
        getTomadaDecisao(idExecucaoSel)
    }
    private fun getTomadaDecisao(idExecucao: Int?): Execucao {
        var execucao: Execucao = Execucao(null, null, null, emptyList(), null,null,null)
        var call = RetrofitInitializer().Service().getTomadaDecisao(idExecucao)
        call.enqueue(object : Callback<Execucao> {
            override fun onFailure(call: Call<Execucao>, t: Throwable) {
                Log.d(TAG_SISTEMA, "Falhou FALHOU CHAMDA: $t.message")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Execucao>, response: Response<Execucao>) {
                Log.d(TAG_SISTEMA, "Retornou getTomadaDecisao: ${response.toString()}")
                if (response.isSuccessful) {
                    execucao = response.body()!!
                    Log.d(TAG_SISTEMA, "percentualAcerto: " + execucao.percentualAcerto)
                    if (execucao.image != null) {
                        var imageData = execucao.image?.imageProc
                        val imageBytes = Base64.decode(imageData, 0)
                        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        imageViewResult.setImageBitmap(image)
                        textViewLabelFoto.visibility = View.VISIBLE
                        imageViewResult.visibility = View.VISIBLE
                    }
                    textPercent.text = String.format("%.2f", execucao.percentualAcerto) + " % "
                    //TextDate.text = execucao.concluido?.substring(0,10) ?: ""
                    if (execucao.concluido != null) {
                        val date = execucao.concluido
                        val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
                        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        TextDate.text = formattedDate
                    }
                }
            }
        })
        return execucao;
    }

    fun confirmar() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}