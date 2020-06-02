package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.yuri.luis.garcia.pereira.tcs_implementacao.R
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.FotoRetorno

class MainActivity : AppCompatActivity() {

    val RETORNO_CARREGAR_FOTO = 99
    val RETORNO_TIRAR_FOTO = 100
    val RETORNO_REGRAS = 101

    private var objRetornoImage: FotoRetorno = FotoRetorno(null,null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public override fun onStart() {
        super.onStart()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var selecao = ""

        when(item?.itemId){
            R.id.cadVariavel -> selecao = "cadVariavel"
            R.id.cadRegra -> selecao ="CadRegra"
            R.id.carregaImagem -> selecao = "carregaImagem"
            R.id.foto -> selecao ="Foto"
            R.id.executaSE -> selecao = "executaSE"
        }

        if (selecao == "executaSE"){
            if ((objRetornoImage.id_image != null) && (objRetornoImage.id_image!! > 0)) {
                val i = Intent(this, ExecucaoActivity::class.java)
                i.putExtra("idImage", objRetornoImage.id_image.toString())
                startActivity(i)
            }
            else {
                this.let {
                    AlertDialog.Builder(it)
                        .setTitle(R.string.app_name)
                        .setMessage("Nenhuma foto foi tira ou selecionada!!!")
                        .setPositiveButton("OK") { dialog, which -> dialog.dismiss()
                        }.show()
                }
            }
        }

        if (selecao == "Foto"){
            val intent = Intent(this,TirarFotoActivity::class.java)
            startActivityForResult(intent, RETORNO_TIRAR_FOTO)
        }

        if (selecao == "CadRegra"){
            val i = Intent(this, ListaRegrasActivity::class.java)
            startActivity(i)
        }

        if (selecao == "cadVariavel"){
            val i = Intent(this, ListaVariavelAndAddActivity::class.java)
            startActivity(i)
        }

        if (selecao == "carregaImagem"){
            val intent = Intent(this, CarregarFotoActivity::class.java)
            startActivityForResult(intent, RETORNO_CARREGAR_FOTO)
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RETORNO_TIRAR_FOTO && resultCode == RESULT_OK) {
            val idFoto = data?.getStringExtra("idFoto") ?: "0"
            if (idFoto.toInt() > 0) {
                objRetornoImage = FotoRetorno(idFoto.toInt(), "success")
                Log.d("CHRISTIAN", "Retornou: " + idFoto)
            }
        }
        if (requestCode == RETORNO_REGRAS && resultCode == RESULT_OK) {

        }
        if (requestCode == RETORNO_CARREGAR_FOTO && resultCode == RESULT_OK) {
            objRetornoImage = FotoRetorno(null,null)
            val idFoto = data?.getStringExtra("idFoto") ?: "0"
            if (idFoto.toInt() > 0) {
                objRetornoImage = FotoRetorno(idFoto.toInt(), "success")
                Log.d("CHRISTIAN", "Retornou: " + idFoto)
            }
        }
    }

}
