package com.android.petopia.presentation.gallery

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.android.petopia.R
import com.android.petopia.data.GalleryModel
import com.android.petopia.databinding.FragmentGalleryBinding
import com.android.petopia.presentation.MainActivity


class GalleryFragment : Fragment() {
    private lateinit var galleryRecyclerViewAdapter: GalleryRecyclerViewAdapter
    private val _binding: FragmentGalleryBinding by lazy {
        FragmentGalleryBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val galleryViewModel by viewModels<GalleryViewModel> {
        GalleryViewModelFactory(requireContext())
    }
    private val gallerySharedViewModel by viewModels<GallerySharedViewModel>()

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


        //다이얼로그 띄우는 함수
        binding.galleryIvRemove.setOnClickListener {
            (activity as MainActivity).showDialog()
            }

        binding.galleryIvMain.setOnClickListener {}

        binding.galleryIvAdd.setOnClickListener {
            gallerySharedViewModel.changeLayoutMode("ADD")
            showPhoto()
        }


        Log.d("리스트", "${gallerySharedViewModel.galleryListLiveData.value}")
//        binding.galleryBtn.setOnClickListener {
//            galleryViewModel.addGalleryList(binding.galleryTvTitle.text.toString())
//        }
//
        gallerySharedViewModel.galleryListLiveData.observe(viewLifecycleOwner) {
            Log.d("갤러리 리스트", "${gallerySharedViewModel.galleryListLiveData.value}")
            galleryRecyclerViewAdapter.submitList(
                gallerySharedViewModel.galleryListLiveData.value?.toList()
            )

        }

    }

    //어댑터 초기화 함수 : 사용자 입력 사진을 리사이클러뷰로 보여주는 함수. 사진 클릭시 Detail로 이동.
    private fun initAdapter() {
        galleryRecyclerViewAdapter = GalleryRecyclerViewAdapter(
            itemClickListener = { item -> gallerySharedViewModel.changeLayoutMode("Read")
                showPhoto()
                gallerySharedViewModel.updateCurrentPhoto(item)

            }, itemLongClickListener = { item -> })
        binding.galleryRv.adapter = galleryRecyclerViewAdapter
        binding.galleryRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    //Photo Fragment 띄우는 함수
    private fun showPhoto() {
PhotoFragment().show(childFragmentManager, "PHOTO_FRAGMENT")


//        childFragmentManager.beginTransaction()
//            .replace(R.id.gallery_photo_container, PhotoFragment())
//            .setReorderingAllowed(true)
//            .addToBackStack(null)
//            .commit()
    }


    override fun onResume() {
        super.onResume()

    }

}