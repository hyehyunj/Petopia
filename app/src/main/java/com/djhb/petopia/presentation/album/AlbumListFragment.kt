package com.djhb.petopia.presentation.album

import android.os.Bundle
import android.util.Log
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
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentAlbumListBinding
import com.djhb.petopia.presentation.MainActivity

//앨범리스트 프래그먼트 : 앨범 두번째 페이지
class AlbumListFragment : Fragment() {

    private lateinit var albumListRecyclerViewAdapter: AlbumListRecyclerViewAdapter
    private val _binding: FragmentAlbumListBinding by lazy {
        FragmentAlbumListBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val albumSharedViewModel by viewModels<AlbumSharedViewModel> {
        AlbumSharedViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initAlbumListRecyclerViewAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //버튼 클릭이벤트
        albumButtonClickListener()
        //데이터 변화감지
        albumDataObserver()
        //앨범 리스트 데이터 불러오기
        albumSharedViewModel.loadAlbumList()
    }


    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun albumButtonClickListener() {
        binding.apply {
            //뒤로가기버튼 클릭이벤트 : 앨범 종료
            albumIvBack.setOnClickListener {
                (activity as MainActivity).removeAlbumFragment()
            }

            //삭제버튼 클릭이벤트 : 체크박스 활성화
            albumIvRemove.setOnClickListener {
                albumSharedViewModel.changeRemoveMode()
            }

            //추가버튼 클릭이벤트 : 상세페이지로 이동하여 사진 추가
            albumIvAdd.setOnClickListener {
                albumSharedViewModel.changeLayoutMode("ADD")
                albumSharedViewModel.prepareNewAlbumList()
                AlbumEditFragment().show(childFragmentManager, "ALBUM_EDIT_FRAGMENT")
                if (albumSharedViewModel.removeModeLiveData.value == "REMOVE") albumSharedViewModel.cancelRemoveMode()
            }


            albumRv.addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
//                        album
                        //                       SharedViewModel.loadGalleryList()
                    }
                }
            })

        }
    }


    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun albumDataObserver() {

        //앨범 리스트 변화감지
        albumSharedViewModel.albumListLiveData.observe(viewLifecycleOwner) {
            albumListRecyclerViewAdapter.updateList(it)
        }

        //삭제모드 변화감지
        albumSharedViewModel.removeModeLiveData.observe(viewLifecycleOwner) {
            Log.d("클릭", "it")
            albumListRecyclerViewAdapter.updateRemoveMode(it)
            albumSharedViewModel.updateRemovedAlbumList(it)
        }

        //삭제할 사진 클릭 감지
        albumSharedViewModel.checkedAlbumLiveData.observe(viewLifecycleOwner) {
            albumListRecyclerViewAdapter.updateCheckedList(
                albumSharedViewModel.removeAlbumList.toList(),
                it
            )
        }

        albumSharedViewModel.isProcessing.observe(viewLifecycleOwner) { isProcessing ->
            if (isProcessing)
                binding.pbGallery.visibility = ProgressBar.VISIBLE
            else
                binding.pbGallery.visibility = ProgressBar.GONE
        }
    }


    //어댑터 초기화 함수 : 사용자 입력 사진을 리사이클러뷰로 보여주는 함수. 사진 클릭시 상세페이지로 이동.
    private fun initAlbumListRecyclerViewAdapter() {
        albumListRecyclerViewAdapter = AlbumListRecyclerViewAdapter(
            albumSharedViewModel.albumListLiveData.value ?: listOf(),
            //사진 클릭이벤트 : 상세페이지로 이동하여 사진 편집, 삭제모드인 경우 삭제할 항목에 추가
            itemClickListener = { item, position ->
                when (albumSharedViewModel.removeModeLiveData.value) {
                    "COMPLETE" -> {
                        albumSharedViewModel.loadUriList(item)
                        albumSharedViewModel.changeLayoutMode("READ")
//                        showAlbumReadFragment()
                        AlbumReadFragment().show(childFragmentManager, "ALBUM_READ_FRAGMENT")
                    }
                    "REMOVE" -> {
                        albumSharedViewModel.considerRemoveAlbumList(item, position)
                    }
                }
            },
            itemLongClickListener = { item, position -> },
        )
        val animator = binding.albumRv.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        binding.albumRv.adapter = albumListRecyclerViewAdapter
        binding.albumRv.layoutManager = GridLayoutManager(requireContext(), 1)
    }

//fun showAlbumReadFragment() {
//
//    childFragmentManager.beginTransaction()
//        .replace(R.id.album_frame, AlbumReadFragment())
//        .setReorderingAllowed(true)
//        .addToBackStack(null)
//        .commit()
//}

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