package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.config.RetrofitInitializer
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Foto
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.FotoRetorno
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_resultado_acitivity.*
import kotlinx.android.synthetic.main.activity_tirar_foto.*
import kotlinx.android.synthetic.main.activity_tirar_foto.buttonConfirm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class TirarFotoActivity : AppCompatActivity() {

    private var objRetornoImage: FotoRetorno = FotoRetorno(null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tirar_foto)
        imageView.visibility = View.INVISIBLE
        tirarFoto.setOnClickListener { takePhoto() }
        buttonConfirm.setOnClickListener { confirmar() }
    }

    private fun getFoto(_id: Int) {
        var call = RetrofitInitializer().Service().getFoto(_id)
        call.enqueue(object : Callback<Foto> {
            override fun onFailure(call: Call<Foto>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou getFoto: $t.message")
            }

            override fun onResponse(call: Call<Foto>, response: Response<Foto>) {
                Log.d(
                    "CHRISTIAN",
                    "********** Retornou getFoto *********:  $response.isSuccessful"
                )
                Log.d("CHRISTIAN", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    val foto = response.body()!!
                    objRetornoImage = FotoRetorno(foto.idFoto, "success")
                    Log.d("CHRISTIAN", "Retornou: $objRetornoImage")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            imageView.visibility = View.VISIBLE
            val stream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val imageEncoded = stream.toByteArray()
            //enviaImagem(URL)
            getFoto(1)
        }
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
        }
        else {
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
