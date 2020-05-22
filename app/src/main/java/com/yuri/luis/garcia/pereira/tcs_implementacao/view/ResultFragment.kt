package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import kotlinx.android.synthetic.main.fragment_principal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class ResultFragment : Fragment() {

    private val LOAD_IMAGE_RESULTS = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        atualizaInformacoes()

    }

    private fun atualizaInformacoes() {
        var call = RetrofitInitializer().variavelService().findAllVariaveis()
        call.enqueue(object : Callback<List<Variavel>> {
            override fun onFailure(call: Call<List<Variavel>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<Variavel>>, response: Response<List<Variavel>>) {

                if (response.isSuccessful){
                    var variavel : List<Variavel> = response.body()!!

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
