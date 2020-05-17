package com.yuri.luis.garcia.pereira.tcs_implementacao.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

import com.yuri.luis.garcia.pereira.tcs_implementacao.R

/**
 * A simple [Fragment] subclass.
 */
class PrincipalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_principal, container, false)
        view.findViewById<Button>(R.id.btnVariavelTeste).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_principalFragment_to_variavelListAndAdd)
        }

        return view
    }
}
