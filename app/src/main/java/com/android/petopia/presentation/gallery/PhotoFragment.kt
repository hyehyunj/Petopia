package com.android.petopia.presentation.gallery

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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.android.petopia.data.GalleryModel
import com.android.petopia.databinding.FragmentPhotoBinding

//포토프래그먼트 : 갤러리에서 사진 조회, 추가, 수정할 때 나타나는 프래그먼트
class PhotoFragment : DialogFragment() {
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
            if (it == "ADD" || it == "EDIT") {//추가 or 편집모드
                addOrEditMode(
                    it, sharedViewModel.currentPhotoLiveData.value ?: GalleryModel("","","")
                )
                }
             else {//읽기전용모드
                sharedViewModel.currentPhotoLiveData.value?.let { currentPhoto ->
                    readOnlyMode(
                        currentPhoto
                    )
                }
            }

        }
        //닫기 버튼이벤트 : 클릭시 갤러리로 이동

        binding.photoTvExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }
    }

    //추가 or 편집모드 함수 : 레이아웃을 입력가능한 모드로 구성한다.
    private fun addOrEditMode(layoutMode: String, item: GalleryModel) {
        //편집모드는 이전 데이터를 불러온다.
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
            //사진 클릭이벤트 : 사용자의 갤러리에서 선택한 사진으로 사진을 업로드
            photoIvTitle.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            //달력 클릭이벤트 : 사용자가 선택한 날짜로 사진의 날짜를 업로드
            photoTvCalendar.setOnClickListener {
                Log.d("현재 인덱스는", "${sharedViewModel.currentPhotoLiveData.value?.index}")

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
            //완료버튼 : 새로운 사진으로 등록 또는 변경 후 읽기전용모드로 전환한다.
            photoTvEdit.apply {
                text = "완료"
                setOnClickListener {
                    sharedViewModel.updateNewGallery(
                        binding.photoEtTitle.text.toString(),
                        binding.photoTvCalendar.text.toString(),
                        item.index
                    )
                    Log.d("포토에서는", "${sharedViewModel.galleryListLiveData.value}")
                    sharedViewModel.changeLayoutMode("READ")

                }
            }
        }
    }

        //읽기전용 모드 함수 : 레이아웃을 입력 불가능한 모드로 구성한다.
        private fun readOnlyMode(item: GalleryModel) {
            binding.apply {
                photoEtTitle.isVisible = false
                photoIvTitle.setImageURI(item.titleImage.toUri())
                photoTvTitle.apply {
                    isVisible = true
                    text = item.titleText
                }
                photoTvCalendar.apply {
                    isVisible = true
                    text = item.date
                    isClickable = false
                }
                //수정버튼 : 현재사진을 수정할 수 있도록 편집모드로 전환한다.
                photoTvEdit.apply {
                    text = "수정"
                    setOnClickListener {
                        sharedViewModel.changeLayoutMode("EDIT")
                    }
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
    params?.height = (deviceWidth * 1.4).toInt()
    dialog?.window?.attributes = params as WindowManager.LayoutParams
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}


    override fun onResume() {
        super.onResume()
        initDialog()
    }


}