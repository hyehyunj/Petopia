package com.android.petopia.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petopia.R
import com.android.petopia.databinding.FragmentHomeMemoryBridgeBinding
import com.android.petopia.databinding.FragmentHomePetopiaBinding

class HomeMemoryBridgeFragment : Fragment() {

    private val _binding: FragmentHomeMemoryBridgeBinding by lazy {
        FragmentHomeMemoryBridgeBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

}