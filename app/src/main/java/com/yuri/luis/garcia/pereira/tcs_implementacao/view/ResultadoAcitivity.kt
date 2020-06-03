package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_acitivity)
        buttonConfirm.setOnClickListener { confirmar() }
        val param = intent.getStringExtra("idExecucao") ?: "0"
        idExecucaoSel = param.toInt()
        Log.d("CHRISTIAN", "idExecucaoSel: $idExecucaoSel" )
        getTomadaDecisao(idExecucaoSel)
    }
    private fun getTomadaDecisao(idExecucao: Int?): Execucao {
        var execucao: Execucao = Execucao(null, null, null, emptyList(), null,null,null)
        var call = RetrofitInitializer().Service().getTomadaDecisao(idExecucao)
        call.enqueue(object : Callback<Execucao> {
            override fun onFailure(call: Call<Execucao>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou FALHOU CHAMDA: $t.message")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Execucao>, response: Response<Execucao>) {
                Log.d("CHRISTIAN", "Retornou getExecucao: $response.isSuccessful")
                if (response.isSuccessful) {
                    execucao = response.body()!!
                    var imageData = execucao.image?.imageProc
                    Log.d("CHRISTIAN", "percentualAcerto: " + execucao.percentualAcerto)

                    val imageBytes = Base64.decode(imageData, 0)
                    val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    val date = execucao.concluido
                    //val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
                    //val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    imageViewResult.setImageBitmap(image)
                    textPercent.text = String.format("%.2f", execucao.percentualAcerto) + " % "
                    //TextDate.text = formattedDate
                    TextDate.text = execucao.concluido?.substring(0,10) ?: ""
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