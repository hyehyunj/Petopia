package com.android.petopia.presentation.gallery

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.petopia.R
import com.android.petopia.databinding.FragmentGalleryBinding
import com.android.petopia.databinding.FragmentMemoryBinding
import com.android.petopia.presentation.MainActivity
import com.android.petopia.presentation.home.HomeSharedViewModel


class GalleryFragment : Fragment() {
    private val _binding: FragmentGalleryBinding by lazy {
        FragmentGalleryBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val galleryViewModel by viewModels<GalleryViewModel> {
        GalleryViewModelFactory(activity as MainActivity)
    }
//    private lateinit var homeSharedViewModel : HomeSharedViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

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
//        homeSharedViewModel = ViewModelProvider(requireParentFragment()).get(HomeSharedViewModel::class.java)


        binding.galleryBtn.setOnClickListener {
            galleryViewModel.addGalleryList(binding.galleryTvTitle.text.toString())
        }

        galleryViewModel.galleryListLiveData.observe(viewLifecycleOwner) {
            Log.d("리스트", "${galleryViewModel.galleryListLiveData.value}")

            galleryViewModel.getGalleryList((activity as MainActivity))
        }

    }

}