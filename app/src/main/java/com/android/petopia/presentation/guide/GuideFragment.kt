package com.android.petopia.presentation.guide

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.android.petopia.R
import com.android.petopia.databinding.FragmentGalleryBinding
import com.android.petopia.databinding.FragmentGuideBinding
import com.android.petopia.presentation.gallery.GalleryRecyclerViewAdapter
import com.android.petopia.presentation.gallery.GallerySharedViewModel
import com.android.petopia.presentation.gallery.GalleryViewModel
import com.android.petopia.presentation.gallery.GalleryViewModelFactory

//가이드 프래그먼트 : 앱 사용방법과 특징을 안내하는 튜토리얼
class GuideFragment : Fragment() {
    private val _binding: FragmentGuideBinding by lazy {
        FragmentGuideBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val guideViewModel by viewModels<GuideViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        guideViewModel.guidePageNumberLiveData.observe(viewLifecycleOwner) {
           guideViewModel.makeGuideModel()
        }
        guideViewModel.guideModelLiveData.observe(viewLifecycleOwner) {
            binding.guideTvStory.text = guideViewModel.guideModelLiveData.value?.guideStory
            binding.guideTvProgressText.text = guideViewModel.guideModelLiveData.value?.progressText

        }


        binding.guideIvBack.setOnClickListener {
            guideViewModel.guideButtonClickListener("BACK")
        }
        binding.guideIvNext.setOnClickListener {
            guideViewModel.guideButtonClickListener("NEXT")
        }


        return binding.root
    }

    //



}