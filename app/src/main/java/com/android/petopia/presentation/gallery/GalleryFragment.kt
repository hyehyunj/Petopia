package com.android.petopia.presentation.gallery

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.android.petopia.databinding.FragmentGalleryBinding


class GalleryFragment : DialogFragment() {

    companion object {
        private const val TAG = "GalleryFragment"
    }

    private lateinit var galleryRecyclerViewAdapter: GalleryRecyclerViewAdapter
    private val _binding: FragmentGalleryBinding by lazy {
        FragmentGalleryBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val gallerySharedViewModel by viewModels<GallerySharedViewModel> {
        GallerySharedViewModelFactory()
    }
//    private val gallerySharedViewModel by viewModels<GallerySharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //버튼 클릭이벤트
        galleryButtonClickListener()
        //데이터 변화감지
        galleryDataObserver()
        gallerySharedViewModel.loadGalleryList()


        initDialog()
    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun galleryButtonClickListener() {

        //뒤로가기버튼 클릭이벤트 : 갤러리 종료
        binding.apply {
            galleryIvBack.setOnClickListener {
                dismiss()
            }
            //삭제버튼 클릭이벤트 : 체크박스 활성화
            galleryIvRemove.setOnClickListener {
                Log.d(TAG, "")
                gallerySharedViewModel.changeRemoveMode()
            }
            //
            //추가버튼 클릭이벤트 : 상세페이지로 이동하여 사진 추가
            galleryIvAdd.setOnClickListener {
                gallerySharedViewModel.changeLayoutMode("ADD")
                showPhoto()
            }
        }
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun galleryDataObserver() {

        //갤러리 리스트 변화감지
        gallerySharedViewModel.galleryListLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "갤러리추가완료${gallerySharedViewModel.galleryListLiveData.value}")
            galleryRecyclerViewAdapter.updateList(it)
        }
        //삭제모드 변화감지
        gallerySharedViewModel.removeModeLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "${gallerySharedViewModel.removeModeLiveData.value}")
            galleryRecyclerViewAdapter.updateRemoveMode(it)
            if (it == "COMPLETE") gallerySharedViewModel.updateRemovedGalleryList()
        }
    }

    //어댑터 초기화 함수 : 사용자 입력 사진을 리사이클러뷰로 보여주는 함수. 사진 클릭시 상세페이지로 이동.
    private fun initAdapter() {
        galleryRecyclerViewAdapter = GalleryRecyclerViewAdapter(
            gallerySharedViewModel.galleryListLiveData.value ?: listOf(),
            //사진 클릭이벤트 : 상세페이지로 이동하여 사진 편집,삭제모드인 경우 삭제할 항목에 추가
            itemClickListener = { item, position ->
                Log.d(TAG, "${position}")
                when(gallerySharedViewModel.removeModeLiveData.value) {
                    "COMPLETE" -> {
                        gallerySharedViewModel.changeLayoutMode("Read")
                        showPhoto()
                        gallerySharedViewModel.updateGalleryList(item, position)
                    }
                    "REMOVE" -> {
                        gallerySharedViewModel.updateGalleryList(item.copy(checked = true), position)
                    }
                }
                gallerySharedViewModel.updateGalleryList(item, position)

            },
            itemLongClickListener = { item, position -> },
        )
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

    //다이얼로그 초기화 함수 : 화면에 맞춰 갤러리 표현
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
        params?.height = (deviceWidth * 1.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}