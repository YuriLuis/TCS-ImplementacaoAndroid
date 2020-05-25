package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Execucao
import kotlinx.android.synthetic.main.fragment_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * A simple [Fragment] subclass.
 */
class ResultFragment : Fragment() {

    var idImageSel: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result, container, false)

        val param = activity?.intent?.getStringExtra("idImage") ?: "0"
        Log.d("CHRISTIAN", "param: $param" )
        idImageSel = param.toInt()

        getTomadaDecisao(idImageSel)
        return view
    }

    override fun onStart() {
        super.onStart()

        val param = activity?.intent?.getStringExtra("idImage") ?: "0"
        Log.d("CHRISTIAN", "param: $param" )
        idImageSel = param.toInt()

        getTomadaDecisao(idImageSel)

    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        getTomadaDecisao(3)

    }

    override fun onResume() {
        super.onResume()
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


