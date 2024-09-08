package com.djhb.petopia.presentation.gallery

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.R
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.databinding.FragmentGalleryReadBinding
import io.github.muddz.styleabletoast.StyleableToast
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

//포토프래그먼트 : 갤러리에서 사진 조회, 추가, 수정할 때 나타나는 프래그먼트
class GalleryReadFragment : DialogFragment() {

    private val _binding: FragmentGalleryReadBinding by lazy {
        FragmentGalleryReadBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var sharedViewModel: GallerySharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel =
            ViewModelProvider(requireParentFragment()).get(GallerySharedViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //갤러리에서 선택한 모드에 따라 레이아웃 변경
        sharedViewModel.layoutModeLiveData.observe(viewLifecycleOwner) {
            sharedViewModel.currentPhotoLiveData.value?.let { currentPhoto ->
                readOnlyMode(
                    currentPhoto
                )
            }


        }
        //닫기 버튼이벤트 : 클릭시 갤러리로 이동
        binding.galleryReadTvExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }
    }

    //읽기전용 모드 함수 : 레이아웃을 입력 불가능한 모드로 구성한다.
    private fun readOnlyMode(item: GalleryModel) {

        binding.apply {
            galleryReadIvTitle.setImageURI(item.imageUris[0].toUri())
            galleryReadTvTitle.text = item.titleText

//            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
//            val date = dateFormat.format(item.updatedDate)
            galleryReadTvCalendar.text = item.photoDate

            //수정버튼 : 현재사진을 수정할 수 있도록 편집모드로 전환한다.
            galleryReadIvAction.setOnClickListener {
                sharedViewModel.changeLayoutMode("EDIT")
                dismiss()
                GalleryEditFragment().show(parentFragmentManager, "")

            }
        }
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
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceWidth * 1.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    override fun onResume() {
        super.onResume()
        initDialog()
    }


}