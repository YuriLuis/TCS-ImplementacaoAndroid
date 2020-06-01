package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.loader.content.CursorLoader
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.ImageRetorno
import kotlinx.android.synthetic.main.activity_principal.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PrincipalActivity : AppCompatActivity() {

    private val LOAD_IMAGE_RESULTS = 1
    private var objRetornoImagem: ImageRetorno = ImageRetorno(null, null)
    private lateinit var buttonVariavel : Button
    private lateinit var btnTesteRegra : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        buttonVariavel = findViewById(R.id.btnVariavelTeste)
        btnTesteRegra = findViewById(R.id.btnRegra)
        clickBotaoAddVariavelTeste()
        eventoBotaoTesteRegra()
    }

    private fun clickBotaoAddVariavelTeste(){
        buttonVariavel.setOnClickListener {
            startActivity(Intent(this, ListaVariavelAndAddActivity::class.java))
        }
    }

    fun eventoBotaoTesteRegra(){
        btnTesteRegra.setOnClickListener{
            startActivity(Intent(this, RegraActivity::class.java))
        }
    }

    fun testeExecucao(view: View) {
        if ((objRetornoImagem.id_image != null) && (objRetornoImagem.id_image!! > 0)) {
            val i = Intent(this, ExecucaoActivity::class.java)
            i.putExtra("idImage", objRetornoImagem.id_image.toString())
            startActivity(i)
        } else {
            this?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.app_name)
                    .setMessage("Selecione uma foto!")
                    .setPositiveButton("OK") { dialog, which ->
                        dialog.dismiss()
                    }.show()
            }
        }

        val i = Intent(this, ExecucaoActivity::class.java)
        i.putExtra("idImage", 1.toString())
        startActivity(i)
    }

    fun pegaImagemGaleria(view: View) {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(i, LOAD_IMAGE_RESULTS)
    }

    private fun enviaImagem(data: Uri) {
        var file = File(getRealPathFromURIAPI11to18(this, data))
        Log.d("CHRISTIAN", "dir: ${file.absoluteFile}")

        var requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        var fileToUpload: MultipartBody.Part =
            MultipartBody.Part.createFormData("image_original", file.name, requestFile)

        var call = RetrofitInitializer().ServicePython().enviaImagem(fileToUpload, requestFile)
        call.enqueue(object : Callback<ImageRetorno> {
            override fun onFailure(call: Call<ImageRetorno>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou enviaImagem: $t.message")
            }

            override fun onResponse(call: Call<ImageRetorno>, response: Response<ImageRetorno>) {
                Log.d(
                    "CHRISTIAN",
                    "********** Retornou enviaImagem *********:  $response.isSuccessful"
                )
                Log.d("CHRISTIAN", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    objRetornoImagem = response.body()!!
                    Log.d("CHRISTIAN", "Retornou: $objRetornoImagem")
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
        super.onActivityResult(requestCode, resultCode, data)
        objRetornoImagem = ImageRetorno(null, null)
        if (resultCode == RESULT_OK) {
            val URL = data?.data
            val bitImage = MediaStore.Images.Media.getBitmap(contentResolver, URL)
            imageViewActivity.setImageBitmap(bitImage)
            if (URL != null) {
                enviaImagem(URL)
            }
        }
    }
}
