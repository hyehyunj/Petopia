package com.djhb.petopia.presentation.gallery

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.R
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.databinding.FragmentGlleryEditBinding
import io.github.muddz.styleabletoast.StyleableToast
import java.time.LocalDateTime

//포토프래그먼트 : 갤러리에서 사진 조회, 추가, 수정할 때 나타나는 프래그먼트
class GalleryEditFragment : DialogFragment() {

    private val _binding: FragmentGlleryEditBinding by lazy {
        FragmentGlleryEditBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var sharedViewModel: GallerySharedViewModel
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(1)) { uri ->
            if (uri.isNotEmpty()) {
                binding.galleryEditIvTitle.setImageURI(uri[0])
                sharedViewModel.considerNewPhoto(uri)
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
           sharedViewModel.currentPhotoLiveData.value?.let { currentPhoto ->
                    addOrEditMode(
                        it, currentPhoto
                    )
                }
        }
        //닫기 버튼이벤트 : 클릭시 갤러리로 이동

        binding.galleryEditTvExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }
    }

    //추가 or 편집모드 함수 : 레이아웃을 입력가능한 모드로 구성한다.
    private fun addOrEditMode(layoutMode: String, item: GalleryModel) {

        //편집모드는 이전 데이터를 불러온다.
        if (layoutMode == "EDIT") {
            binding.apply {
                galleryEditIvTitle.setImageURI(item.imageUris[0].toUri())
                galleryEditEtTitle.setText(item.titleText)
//                photoTvCalendar.text = item.date
            }
        }
        binding.apply {
            //사진 클릭이벤트 : 사용자의 갤러리에서 선택한 사진으로 사진을 업로드
            galleryEditIvTitle.setOnClickListener {
//                if (uriList.count() == 6) {
//                    Toast.makeText(requireContext(),"최대 6장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            //달력 클릭이벤트 : 사용자가 선택한 날짜로 사진의 날짜를 업로드
            galleryEditIvCalendar.setOnClickListener {

                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val listener = DatePickerDialog.OnDateSetListener { datePicker, yy, mm, dd ->
                    binding.galleryEditIvCalendar.text = "${yy}. ${mm + 1}. ${dd}"
                }
                sharedViewModel.considerNewPhoto(LocalDateTime.of(year, month + 1, day, 0, 0, 0))
                val picker = DatePickerDialog(requireContext(), listener, year, month, day)
                picker.show()
            }

            //완료버튼 : 새로운 사진으로 등록 또는 변경 후 읽기전용모드로 전환한다.
            galleryEditTvComplete.apply {
                setOnClickListener {
                    sharedViewModel.considerNewPhoto(binding.galleryEditEtTitle.text.toString())
                    if (sharedViewModel.prepared()) {
                        sharedViewModel.updateNewGallery(
                            item.index
                        )
                        setBackgroundResource(R.drawable.icon_write)
                        sharedViewModel.changeLayoutMode("READ")
                        dismiss()
                    } else StyleableToast.makeText(
                        requireActivity(),
                        "항목이 비어있습니다.",
                        R.style.toast_common
                    ).show()
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
        params?.height = (deviceWidth * 1.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    override fun onResume() {
        super.onResume()
        initDialog()
    }


}