package com.djhb.petopia.presentation.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.SimpleItemAnimator
import com.djhb.petopia.databinding.FragmentAlbumListBinding


class AlbumListFragment : Fragment() {

    private lateinit var galleryRecyclerViewAdapter: GalleryRecyclerViewAdapter
    private val _binding: FragmentAlbumListBinding by lazy {
        FragmentAlbumListBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

//private lateinit var galleryViewPager2 : ViewPager2


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

    }




    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun galleryButtonClickListener() {

        //뒤로가기버튼 클릭이벤트 : 갤러리 종료
        binding.apply {
            galleryIvBack.setOnClickListener {
            }
            //삭제버튼 클릭이벤트 : 체크박스 활성화
            galleryIvRemove.setOnClickListener {
                gallerySharedViewModel.changeRemoveMode()
//                when(gallerySharedViewModel.removeModeLiveData.value) {
//                    "REMOVE" -> galleryRecyclerViewAdapter.updateRemoveMode("REMOVE")
//                    "COMPLETE" -> galleryRecyclerViewAdapter.updateRemoveMode("COMPLETE")
//                }
            }
            //
            //추가버튼 클릭이벤트 : 상세페이지로 이동하여 사진 추가
            galleryIvAdd.setOnClickListener {
                gallerySharedViewModel.changeLayoutMode("ADD")
                GalleryEditFragment().show(childFragmentManager, "GALLERY_ADD_FRAGMENT")
                if(gallerySharedViewModel.removeModeLiveData.value == "REMOVE") gallerySharedViewModel.cancelRemoveMode()
                  }


            galleryRv.addOnScrollListener(object: OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!recyclerView.canScrollVertically(1)) {
//                        gallerySharedViewModel.loadGalleryList()
                    }
                }
            })

        }
    }




    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun galleryDataObserver() {

        //갤러리 리스트 변화감지
        gallerySharedViewModel.galleryListLiveData.observe(viewLifecycleOwner) {

            galleryRecyclerViewAdapter.updateList(it)
        }
//
//        gallerySharedViewModel.removeModeLiveData.observe(viewLifecycleOwner) {
//            when(it) {
//                "ADD","EDIT" -> GalleryEditFragment().show(childFragmentManager, "GALLERY_EDIT_FRAGMENT")
//                "READ" -> GalleryReadFragment().show(childFragmentManager, "GALLERY_READ_FRAGMENT")
//            }
//        }

        //삭제모드 변화감지
        gallerySharedViewModel.removeModeLiveData.observe(viewLifecycleOwner) {
            galleryRecyclerViewAdapter.updateRemoveMode(it)
            gallerySharedViewModel.updateRemoveGalleryList(it)
        }

        //삭제할 사진 클릭 감지
        gallerySharedViewModel.checkedPhotoLiveData.observe(viewLifecycleOwner) {
            galleryRecyclerViewAdapter.updateCheckedList(gallerySharedViewModel.removePhotoList.toList(), it)
        }

        gallerySharedViewModel.isProcessing.observe(viewLifecycleOwner) { isProcessing ->
            if(isProcessing)
                binding.pbGallery.visibility = ProgressBar.VISIBLE
            else
                binding.pbGallery.visibility = ProgressBar.GONE
        }


    }


//    private fun initGalleryViewPager2(){
//        galleryViewPager2 = findViewById(R.id.myViewPager2)
//        val pagerAdapter2 = MyPagerAdapter(this)
//        myViewPager2.adapter = pagerAdapter2
//    }

    //어댑터 초기화 함수 : 사용자 입력 사진을 리사이클러뷰로 보여주는 함수. 사진 클릭시 상세페이지로 이동.
    private fun initAdapter() {
        galleryRecyclerViewAdapter = GalleryRecyclerViewAdapter(
            gallerySharedViewModel.galleryListLiveData.value ?: listOf(),
            //사진 클릭이벤트 : 상세페이지로 이동하여 사진 편집,삭제모드인 경우 삭제할 항목에 추가
            itemClickListener = { item, position ->
                gallerySharedViewModel.updateGalleryList(item, position)
                when(gallerySharedViewModel.removeModeLiveData.value) {
                    "COMPLETE" -> {
                        gallerySharedViewModel.changeLayoutMode("READ")
                        GalleryReadFragment().show(childFragmentManager, "GALLERY_READ_FRAGMENT")
                    }
                }
            },
            itemLongClickListener = { item, position -> },
        )
        val animator = binding.galleryRv.itemAnimator
        if(animator is SimpleItemAnimator){
            animator.supportsChangeAnimations = false
        }
        binding.galleryRv.adapter = galleryRecyclerViewAdapter
        binding.galleryRv.layoutManager = GridLayoutManager(requireContext(), 1)
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



}