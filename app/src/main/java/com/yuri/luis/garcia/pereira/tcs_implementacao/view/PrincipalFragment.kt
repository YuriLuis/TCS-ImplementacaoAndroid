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
import kotlinx.android.synthetic.main.fragment_principal.*


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

        return view
    }

    private fun pegaImagemGaleria()
    {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.

        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, LOAD_IMAGE_RESULTS)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK){
            val URL = data?.data
            val bitImage = MediaStore.Images.Media.getBitmap(activity?.contentResolver, URL)
            imageView.setImageBitmap(bitImage)
        }

    }

}
