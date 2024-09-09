package com.djhb.petopia.presentation.register.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentPersonalTermBinding


class PersonalTermFragment : Fragment() {

    private val _binding: FragmentPersonalTermBinding by lazy {
        FragmentPersonalTermBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = binding.wbWebviewLayout
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://petopia.imweb.me/?mode=privacy")
    }

}