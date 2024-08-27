package com.android.petopia.presentation.gallery

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.android.petopia.data.GalleryModel
import com.android.petopia.databinding.FragmentPhotoBinding


class PhotoFragment : Fragment() {
    private val _binding: FragmentPhotoBinding by lazy {
        FragmentPhotoBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var sharedViewModel: GallerySharedViewModel
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.photoIvTitle.setImageURI(uri)
                sharedViewModel.considerNewPhoto(uri.toString())
            }
        }

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

            if (it == "ADD" || it == "EDIT") {
                addOrEditMode(
                    it, sharedViewModel.currentPhotoLiveData.value ?: GalleryModel("","","")
                )
                }
             else {
                sharedViewModel.currentPhotoLiveData.value?.let { currentPhoto ->
                    readOnlyMode(
                        currentPhoto
                    )
                }
            }

        }

        binding.photoTvExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }

    }

    //추가 or 편집모드 함수 : 레이아웃을 입력가능한 모드로 구성한다.
    private fun addOrEditMode(layoutMode: String, item: GalleryModel) {
        if (layoutMode == "EDIT") {
            binding.apply {
                photoIvTitle.setImageURI(item.titleImage.toUri())
                photoEtTitle.setText(item.titleText)
                photoTvCalendar.text = item.date
            }
        }
        binding.apply {
            photoTvTitle.isVisible = false
            photoEtTitle.isVisible = true
            //사진 클릭이벤트 : 사용자의 갤러리에서 사진 업로드
            photoIvTitle.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            //달력 클릭이벤트 : 사진 날짜 업로드
            photoTvCalendar.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val listener = DatePickerDialog.OnDateSetListener { datePicker, yy, mm, dd ->
                        binding.photoTvCalendar.text = "${yy}년 ${mm + 1}월 ${dd}일"
                    }
                    val picker = DatePickerDialog(requireContext(), listener, year, month, day)
                    picker.show()
            }
            //완료버튼 : 새로운 사진 등록 또는 변경
            photoTvEdit.apply {
                text = "완료"
                setOnClickListener {
                    sharedViewModel.updateNewPhoto(
                        binding.photoEtTitle.text.toString(),
                        binding.photoTvCalendar.text.toString()
                    )
                    sharedViewModel.changeLayoutMode("READ")
                }
            }
        }

    }

        //읽기전용 모드 함수 : 레이아웃을 읽기전용으로 변경해준다.
        private fun readOnlyMode(item: GalleryModel) {
            binding.apply {
                photoEtTitle.isVisible = false
                photoIvTitle.setImageURI(item.titleImage.toUri())
                photoEtTitle.setText(item.titleText)
                photoTvCalendar.text = item.date
                photoTvTitle.apply {
                    isVisible = true
                    text = item.titleText
                }
                photoTvCalendar.apply {
                    isVisible = true
                    text = item.date
                }
                //수정버튼 : 현재사진 수정
                photoTvEdit.apply {
                    text = "수정"
                    setOnClickListener {
                        sharedViewModel.changeLayoutMode("EDIT")
                    }
                }
            }
        }



}