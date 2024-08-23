package com.android.petopia.presentation.memory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.petopia.R
import com.android.petopia.databinding.FragmentLetterBinding
import com.android.petopia.databinding.FragmentMemoryBinding
import com.android.petopia.presentation.home.HomeSharedViewModel


class MemoryFragment : Fragment() {

    private var _binding: FragmentMemoryBinding? = null
    private val binding get() = _binding as FragmentMemoryBinding
    private lateinit var homeSharedViewModel : HomeSharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMemoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeSharedViewModel = ViewModelProvider(requireParentFragment()).get(HomeSharedViewModel::class.java)


    }

}