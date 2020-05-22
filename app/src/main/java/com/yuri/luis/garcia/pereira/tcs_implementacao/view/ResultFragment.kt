package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Resultado
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import kotlinx.android.synthetic.main.fragment_principal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
//        getTomadaDecisao(id)

    }

    private fun getTomadaDecisao(id: Int?) {
        var resultado: Resultado = Resultado( null, null, null, null, null, null, null,null)
        var call = RetrofitInitializer().Service().getTomadaDecisao(id)
        call.enqueue(object : Callback<Resultado> {

            override fun onFailure(call: Call<Resultado>, t: Throwable) {
                Log.d("pedro", "Falhou resultado: $t.message")

            }

            override fun onResponse(call: Call<Resultado>, response: Response<Resultado>) {

                if (response.isSuccessful){
                   resultado = response.body()!!
                    Log.d("pedro", "$resultado")
                }
            }


        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK){
            val URL = data?.data
            val bitImage = MediaStore.Images.Media.getBitmap(activity?.contentResolver, URL)
            imageView.setImageBitmap(bitImage)
        }

    }

}
