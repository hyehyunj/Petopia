package com.android.petopia.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.petopia.databinding.FragmentHomePetopiaBinding
import com.android.petopia.presentation.memory.MemoryFragment


class HomePetopiaFragment : Fragment() {
    private val _binding: FragmentHomePetopiaBinding by lazy {
        FragmentHomePetopiaBinding.inflate(layoutInflater)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnToMemory.setOnClickListener {
            setMemoryFragment()
        }
    }


    private fun setMemoryFragment() {
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.home_layout, MemoryFragment())
//        transaction.addToBackStack(null)
//        transaction.commit()

        MemoryFragment().show(childFragmentManager, "MEMORY_FRAGMENT")



    }


}