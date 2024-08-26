package com.android.petopia.presentation.gallery

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.android.petopia.R
import com.android.petopia.databinding.FragmentGalleryBinding
import com.android.petopia.presentation.MainActivity


class GalleryFragment : Fragment() {
    private lateinit var galleryRecyclerViewAdapter: GalleryRecyclerViewAdapter
    private val _binding: FragmentGalleryBinding by lazy {
        FragmentGalleryBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val galleryViewModel by viewModels<GalleryViewModel> {
        GalleryViewModelFactory(activity as MainActivity)
    }
    private val gallerySharedViewModel by viewModels<GallerySharedViewModel> {
        GallerySharedViewModelFactory(activity as MainActivity)
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
        initAdapter()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        homeSharedViewModel = ViewModelProvider(requireParentFragment()).get(HomeSharedViewModel::class.java)

        binding.galleryIvRemove.setOnClickListener {}

        binding.galleryIvMain.setOnClickListener {}

        binding.galleryIvAdd.setOnClickListener {
            gallerySharedViewModel.changeLayoutMode("ADD")
            Log.d("갤러리","${gallerySharedViewModel.layoutModeLiveData.value}")
            showPhoto()
        }


//        binding.galleryBtn.setOnClickListener {
//            galleryViewModel.addGalleryList(binding.galleryTvTitle.text.toString())
//        }
//
//        galleryViewModel.galleryListLiveData.observe(viewLifecycleOwner) {
//            Log.d("리스트", "${galleryViewModel.galleryListLiveData.value}")
//
//            galleryViewModel.getGalleryList((activity as MainActivity))
//        }

    }

    //어댑터 초기화 함수 : 사용자 입력 사진을 리사이클러뷰로 보여주는 함수. 사진 클릭시 Detail로 이동.
    private fun initAdapter() {
        galleryRecyclerViewAdapter = GalleryRecyclerViewAdapter(
            itemClickListener = { item ->
                gallerySharedViewModel.updateCurrentPhoto(item)
            }, itemLongClickListener = { item -> })
        binding.galleryRv.adapter = galleryRecyclerViewAdapter
        binding.galleryRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    //Photo Fragment 띄우는 함수
    private fun showPhoto() {
        childFragmentManager.beginTransaction()
            .replace(R.id.gallery_photo_container, PhotoFragment())
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }
}