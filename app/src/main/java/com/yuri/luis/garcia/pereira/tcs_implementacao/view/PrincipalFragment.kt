package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import androidx.navigation.Navigation
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.ImageRetorno
import kotlinx.android.synthetic.main.fragment_principal.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class PrincipalFragment : Fragment() {

    private val LOAD_IMAGE_RESULTS = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_principal, container, false)


        view.findViewById<Button>(R.id.btnVariavelTeste).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_principalFragment_to_variavelListAndAdd)
        }

        view.findViewById<Button>(R.id.buttonPick).setOnClickListener {
            pegaImagemGaleria()
        }

        view.findViewById<Button>(R.id.buttonExecucao).setOnClickListener {
            teste(it)
        }


        return view
    }

    private fun teste(view : View){
        val i = Intent(context, ExecucaoActivity::class.java)
        i.putExtra("idImage",1.toString())
        startActivity(i)
    }

    private fun pegaImagemGaleria()
    {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(i, LOAD_IMAGE_RESULTS)


    }

    private fun enviaImagem(data: Uri) {
        var file = File(this.context?.let { getRealPathFromURIAPI11to18(it, data) })
        var requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
        var filePart = MultipartBody.Part.createFormData("upload_file", "image_original", requestBody)

        var call = RetrofitInitializer().ServicePython().enviaImagem(filePart)
        call.enqueue(object : Callback<ImageRetorno> {
            override fun onFailure(call: Call<ImageRetorno>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou enviaImagem: $t.message")
            }

            override fun onResponse(call: Call<ImageRetorno>, response: Response<ImageRetorno>) {
                Log.d("CHRISTIAN", "********** Retornou enviaImagem *********:  $response.isSuccessful")
                Log.d("CHRISTIAN", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    var objExecucao = response.body()!!
                    Log.d("CHRISTIAN", "Retornou: $objExecucao")
                }
            }

        })
    }

    private fun testemagem(data: Uri) {
        var file = File(this.context?.let { getRealPathFromURIAPI11to18(it, data) })
        var requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
        var filePart = MultipartBody.Part.createFormData("image_original", "image_original", requestBody)

        var call = RetrofitInitializer().ServicePython().getImagem()
        call.enqueue(object : Callback<ImageRetorno> {
            override fun onFailure(call: Call<ImageRetorno>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou enviaImagem: $t.message")
            }

            override fun onResponse(call: Call<ImageRetorno>, response: Response<ImageRetorno>) {
                Log.d("CHRISTIAN", "********** Retornou enviaImagem *********:  $response.isSuccessful")
                Log.d("CHRISTIAN", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    var objExecucao = response.body()!!
                    Log.d("CHRISTIAN", "Retornou: $objExecucao")
                }
            }

        })
    }

    fun getRealPathFromURIAPI11to18(context: Context, contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null

        val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK){
            val URL = data?.data
            val bitImage = MediaStore.Images.Media.getBitmap(activity?.contentResolver, URL)
            imageView.setImageBitmap(bitImage)
            if (URL != null) {
                enviaImagem(URL)
//                testemagem(URL)
            }
        }

    }

}
