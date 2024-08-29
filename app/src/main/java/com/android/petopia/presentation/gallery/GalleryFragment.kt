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


        //삭제버튼 클릭이벤트
        binding.galleryIvRemove.setOnClickListener {
            Log.d("삭제버튼 클릭", "")
            galleryRecyclerViewAdapter.appearCheckBox(true)
        }

        binding.galleryIvMain.setOnClickListener {}
//추가버튼 클릭이벤트 : 상세페이지로 이동
        binding.galleryIvAdd.setOnClickListener {
            gallerySharedViewModel.changeLayoutMode("ADD")
            showPhoto()
        }

        gallerySharedViewModel.currentPhotoLiveData.observe(viewLifecycleOwner) {
            galleryRecyclerViewAdapter.notifyItemChanged(it.index)
        }

        gallerySharedViewModel.galleryListLiveData.observe(viewLifecycleOwner) {

            galleryRecyclerViewAdapter.notifyItemInserted(0)
            }


    }

    //어댑터 초기화 함수 : 사용자 입력 사진을 리사이클러뷰로 보여주는 함수. 사진 클릭시 상세페이지로 이동.
    private fun initAdapter() {
        galleryRecyclerViewAdapter = GalleryRecyclerViewAdapter(
            gallerySharedViewModel.galleryListLiveData.value ?: listOf(),
            itemClickListener = { item, position ->
                gallerySharedViewModel.changeLayoutMode("Read")
                showPhoto()
                gallerySharedViewModel.updateCurrentPhoto(item, position)
            }, itemLongClickListener = { item, position -> })
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

    //
    private fun removePhoto() {

    }


    override fun onResume() {
        super.onResume()

    }

}