package com.yuri.luis.garcia.pereira.tcs_implementacao

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Execucao
import kotlinx.android.synthetic.main.fragment_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ResultadoAcitivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_acitivity)
        getTomadaDecisao(3)
    }
    private fun getTomadaDecisao(idExecucao: Int?): Execucao {
        var execucao: Execucao = Execucao(null, null, null, emptyList(), null,null,null)
        var call = RetrofitInitializer().Service().getExecucao(idExecucao)
        call.enqueue(object : Callback<Execucao> {
            override fun onFailure(call: Call<Execucao>, t: Throwable) {
                Log.d("ERROR CHAMADA APi", "Falhou FALHOU CHAMDA: $t.message")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Execucao>, response: Response<Execucao>) {
                Log.d("SUCESSO!", "Retornou getExecucao: $response.isSuccessful")
                if (response.isSuccessful) {
                    execucao = response.body()!!
                    var imageData = execucao.image?.image

                    val imageBytes = Base64.decode(imageData, 0)
                    val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    val date = execucao.concluido
                    val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
                    val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    imageViewResult.setImageBitmap(image)
                    textPercent.text = execucao.percentualAcerto.toString() + " % "
                    TextDate.text = formattedDate
                }
            }

        })
        return execucao;
    }
}
