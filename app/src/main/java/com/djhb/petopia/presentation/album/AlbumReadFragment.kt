package com.djhb.petopia.presentation.album

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.djhb.petopia.R
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.databinding.FragmentAlbumReadBinding
import com.wenchao.cardstack.CardStack

//읽기전용 프래그먼트 : 사진 조회할 때 나타나는 프래그먼트
class AlbumReadFragment : DialogFragment() {

    private val _binding: FragmentAlbumReadBinding by lazy {
        FragmentAlbumReadBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var albumSharedViewModel: AlbumSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        albumSharedViewModel =
            ViewModelProvider(requireParentFragment()).get(AlbumSharedViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        albumSharedViewModel.currentAlbumLiveData.observe(viewLifecycleOwner) {
            readOnlyMode(
                it
            )
        }


        //닫기 버튼이벤트 : 클릭시 갤러리로 이동
        binding.albumReadTvExit.setOnClickListener {
       dismiss()
        }
    }

    //읽기전용 모드 함수 : 레이아웃을 입력 불가능한 모드로 구성한다.
    private fun readOnlyMode(item: GalleryModel) {
        binding.apply {
            initAdapter(item)

            albumReadTvTitle.text = item.titleText
            albumReadTvCalendar.text = item.photoDate


            //수정버튼 : 현재사진을 수정할 수 있도록 편집모드로 전환한다.
            albumReadIvAction.setOnClickListener {
                albumSharedViewModel.changeLayoutMode("EDIT")
                AlbumEditFragment().show(parentFragmentManager, "ALBUM_EDIT_FRAGMENT")
            }
        }
    }

    private fun initAdapter(item: GalleryModel) {
        val cardStack: CardStack = binding.albumReadLayoutInside

        cardStack.setContentResource(R.layout.layout_album_read)
        cardStack.setStackMargin(40)
        val readAdapter = AlbumReadAdapter(requireContext(),item.imageUris)

// 어댑터를 CardStack에 설정
        cardStack.setAdapter(readAdapter)
    }

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
        params?.width = (deviceWidth * 1).toInt()
        params?.height = (deviceWidth * 2).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onResume() {
        super.onResume()
        initDialog()
    }

}