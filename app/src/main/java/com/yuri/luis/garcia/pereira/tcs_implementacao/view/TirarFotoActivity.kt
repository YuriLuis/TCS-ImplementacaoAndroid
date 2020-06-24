package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Foto
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.FotoRetorno
import kotlinx.android.synthetic.main.activity_tirar_foto.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class TirarFotoActivity : AppCompatActivity() {

    private var objRetornoImage: FotoRetorno = FotoRetorno(null, null)
    private lateinit var botaoConfirmar: Button
    private val TAG_SISTEMA = "CHRISTIAN";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tirar_foto)
        imageView.visibility = View.INVISIBLE
        tirarFoto.setOnClickListener { takePhoto() }
        botaoConfirmar = findViewById<Button>(R.id.buttonConfirm)
        botaoConfirmar.setOnClickListener { confirmar() }
        botaoConfirmar.visibility = View.INVISIBLE
    }

    private fun getFoto(_id: Int) {
        var call = RetrofitInitializer().Service().getFoto(_id)
        call.enqueue(object : Callback<Foto> {
            override fun onFailure(call: Call<Foto>, t: Throwable) {
                Log.d(TAG_SISTEMA, "Falhou getFoto: $t.message")
            }

            override fun onResponse(call: Call<Foto>, response: Response<Foto>) {
                Log.d(
                    TAG_SISTEMA,
                    "********** Retornou getFoto *********:  $response.isSuccessful"
                )
                Log.d(TAG_SISTEMA, response.isSuccessful.toString())
                if (response.isSuccessful) {
                    val foto = response.body()!!
                    objRetornoImage = FotoRetorno(foto.idFoto, "success")
                    Log.d(TAG_SISTEMA, "Retornou: $objRetornoImage")
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

    private fun bitmapToFile(bitmap:Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("Images",Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            imageView.visibility = View.VISIBLE
            val URL = bitmapToFile(imageBitmap)
            if (URL != null) {
                enviaImagem(URL)
                //getFoto(1)
            }
        }
    }

    private fun enviaImagem(data: Uri) {
        val file = File(data.path)
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload: MultipartBody.Part =
            MultipartBody.Part.createFormData("image_original", file.name, requestFile)
        val call = RetrofitInitializer().ServicePython().enviaImagem(fileToUpload, requestFile)
        call.enqueue(object : Callback<FotoRetorno> {
            override fun onFailure(call: Call<FotoRetorno>, t: Throwable) {
                Log.d(TAG_SISTEMA, "Falhou enviaImagem: $t.message")
                Toast.makeText(
                    this@TirarFotoActivity,
                    "Ocorreu um erro tente novamente: $t.message",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<FotoRetorno>, response: Response<FotoRetorno>) {
                Log.d(TAG_SISTEMA, response.toString())
                if (response.isSuccessful) {
                    objRetornoImage = response.body()!!
                    botaoConfirmar.visibility = View.VISIBLE
                }
            }

        })
    }

    val REQUEST_IMAGE_CAPTURE = 1
    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun validate(): Boolean {
        var result = true
        if ((objRetornoImage.id_image != null) && (objRetornoImage.id_image!! > 0)) {
        } else {
            AlertDialog.Builder(this)
                .setTitle("Validação")
                .setMessage("Tire uma foto!")
                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }.show()
            result = false
        }
        return result
    }

    fun confirmar() {
        if (validate()) {
            val i = Intent(this, ExecucaoActivity::class.java)
            i.putExtra("idImage", objRetornoImage.id_image.toString())
            startActivity(i)
            /*val intent = this.intent
            intent.putExtra("idFoto", objRetornoImage.id_image.toString())
            this.setResult(Activity.RESULT_OK, intent)
            this.finish()*/
        }
    }
}
