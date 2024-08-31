package com.android.petopia.presentation.gallery

import android.annotation.SuppressLint
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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.android.petopia.R
import com.android.petopia.data.GalleryModel
import com.android.petopia.databinding.FragmentGalleryBinding
import com.android.petopia.presentation.MainActivity


class GalleryFragment : DialogFragment() {
    private lateinit var galleryRecyclerViewAdapter: GalleryRecyclerViewAdapter
    private val _binding: FragmentGalleryBinding by lazy {
        FragmentGalleryBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val galleryViewModel by viewModels<GalleryViewModel> {
        GalleryViewModelFactory(requireContext())
    }
    private val gallerySharedViewModel by viewModels<GallerySharedViewModel>()

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
            Log.d("갤러리 변경감지", "${gallerySharedViewModel.galleryListLiveData.value}")
            galleryRecyclerViewAdapter.updateList(it)
//            galleryRecyclerViewAdapter.notifyDataSetChanged()
        }
        initDialog()
//        btnBackListener()
    }

    //어댑터 초기화 함수 : 사용자 입력 사진을 리사이클러뷰로 보여주는 함수. 사진 클릭시 상세페이지로 이동.
    private fun initAdapter() {
        galleryRecyclerViewAdapter = GalleryRecyclerViewAdapter(
            gallerySharedViewModel.galleryListLiveData.value!!,
            itemClickListener = { item, position ->
                Log.d("갤러리 변경감지", "${position}")
                gallerySharedViewModel.updateCurrentPhoto(item, position)
                gallerySharedViewModel.changeLayoutMode("Read")
                showPhoto()
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

    //뒤로가기 버튼 함수 : 홈프래그먼트로 돌아간다.
//    private fun btnBackListener() {
//        requireActivity().onBackPressedDispatcher.addCallback(object :
//            OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                parentFragmentManager.beginTransaction()
//                    .remove(this@GalleryFragment)
//                    .commit()
//            }
//        })
//    }

    private fun initDialog() {
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val size = Point()
        display.getSize(size)
        size.x
        size.y
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceWidth * 1.4).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}