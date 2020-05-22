package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Execucao
import kotlinx.android.synthetic.main.fragment_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream


/**
 * A simple [Fragment] subclass.
 */
class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result, container, false)
       var id = 3
        getTomadaDecisao(id)
        return view
    }

    override fun onStart() {
        super.onStart()
        val id = 3
        getTomadaDecisao(id)

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

            override fun onResponse(call: Call<Execucao>, response: Response<Execucao>) {
                Log.d("SUCESSO!", "Retornou getExecucao: $response.isSuccessful")
                if (response.isSuccessful) {
                    execucao = response.body()!!
                    var imageData = execucao.image?.image
                    val arrayInputStream = ByteArrayInputStream(imageData)
                    val bitmap = BitmapFactory.decodeStream(arrayInputStream)
                    imageViewResult.setImageBitmap(bitmap)
                    textPercent.text = execucao.percentualAcerto.toString() + " % "
                    TextDate.text = execucao.concluido
                }
            }

        })
        return execucao;
    }



}


