package com.nutriomatic.app.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.nutriomatic.app.R

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        view.findViewById<View>(R.id.tv_redirect_login).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return view
    }
}