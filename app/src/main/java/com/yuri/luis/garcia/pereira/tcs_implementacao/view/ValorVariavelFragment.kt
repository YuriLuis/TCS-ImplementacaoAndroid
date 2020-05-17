package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.yuri.luis.garcia.pereira.tcs_implementacao.R

/**
 * A simple [Fragment] subclass.
 */
class ValorVariavelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_valor_variavel, container, false)
    }

}
