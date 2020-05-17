package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.yuri.luis.garcia.pereira.tcs_implementacao.R

/**
 * A simple [Fragment] subclass.
 */
class VariavelListAndAdd : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list_and_add_variavel, container, false)
        view.findViewById<FloatingActionButton>(R.id.btnFlutuanteAddVariavel).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_variavelListAndAdd_to_varivalFragment)
        }
        return view
    }

}
