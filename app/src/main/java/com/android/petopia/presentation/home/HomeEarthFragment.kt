package com.android.petopia.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petopia.R
import com.android.petopia.databinding.FragmentHomeEarthBinding
import com.android.petopia.databinding.FragmentHomeMemoryBridgeBinding
import com.android.petopia.presentation.community.CommunityFragment
import com.android.petopia.presentation.gallery.GalleryFragment

class HomeEarthFragment : Fragment() {
    private val _binding: FragmentHomeEarthBinding by lazy {
        FragmentHomeEarthBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initlayout()
        return binding.root
    }


    private fun initlayout()
    {
        binding.homeEarthIvSetting.setOnClickListener {

        }
        binding.homeEarthIvCloudLeft.setOnClickListener {

        }
        binding.homeEarthIvCloudRight.setOnClickListener {

        }
        binding.homeEarthIvCommunity.setOnClickListener {
        childFragmentManager.beginTransaction()
            .replace(R.id.home_earth_container, CommunityFragment())
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
        }


    }

}