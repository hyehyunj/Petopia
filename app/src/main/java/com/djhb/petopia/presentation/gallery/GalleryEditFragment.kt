package com.djhb.petopia.presentation.gallery

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.djhb.petopia.DateFormatUtils
import com.djhb.petopia.R
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.databinding.FragmentGlleryEditBinding
import io.github.muddz.styleabletoast.StyleableToast

//포토프래그먼트 : 갤러리에서 사진 조회, 추가, 수정할 때 나타나는 프래그먼트
class GalleryEditFragment : DialogFragment() {

    private val _binding: FragmentGlleryEditBinding by lazy {
        FragmentGlleryEditBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
//    private lateinit var galleryEditRecyclerViewAdapter: GalleryEditRecyclerViewAdapter
    private lateinit var gallerySharedViewModel: GallerySharedViewModel
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(2)) { uri ->
            if (uri.isNotEmpty()) {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    uri.forEach{context?.contentResolver?.takePersistableUriPermission(it, flag)}
                binding.galleryEditIvTitle.setImageURI(uri[0])
                if(uri.size == 2)binding.galleryEditIvTitle2.setImageURI(uri[1]) else
                    binding.galleryEditIvTitle2.setImageResource(R.drawable.bg_translucent_white_square)

                gallerySharedViewModel.considerNewPhoto(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        initAdapter()
        gallerySharedViewModel =
            ViewModelProvider(requireParentFragment()).get(GallerySharedViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //갤러리에서 선택한 모드에 따라 레이아웃 변경
        gallerySharedViewModel.layoutModeLiveData.observe(viewLifecycleOwner) {
           gallerySharedViewModel.currentPhotoLiveData.value?.let { currentPhoto ->
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


    //어댑터 초기화 함수 : 사용자 입력 사진을 리사이클러뷰로 보여주는 함수. 사진 클릭시 상세페이지로 이동.
//    private fun initAdapter() {
//        galleryEditRecyclerViewAdapter = GalleryEditRecyclerViewAdapter(
//            gallerySharedViewModel.galleryListLiveData.value ?: listOf(),
//            //사진 클릭이벤트 : 상세페이지로 이동하여 사진 편집,삭제모드인 경우 삭제할 항목에 추가
//            itemClickListener = { item, position ->
//            },
//            itemLongClickListener = { item, position ->  },
//        )
//        binding.galleryEditRv.adapter = galleryEditRecyclerViewAdapter
//        binding.galleryEditRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//    }





    //추가 or 편집모드 함수 : 레이아웃을 입력가능한 모드로 구성한다.
    private fun addOrEditMode(layoutMode: String, item: GalleryModel) {
        //편집모드는 이전 데이터를 불러온다.
        if (layoutMode == "EDIT") {
            binding.apply {
                galleryEditEtTitle.setText(item.titleText)
                if(item.imageUris.size>1) {
                Glide.with(requireParentFragment())
                    .load(item.imageUris[1].toUri())
                    .centerCrop()
                    .into(galleryEditIvTitle2) } else {
                    Glide.with(requireParentFragment())
                        .load(item.imageUris[0].toUri())
                        .centerCrop()
                        .into(galleryEditIvTitle)

                   binding.galleryEditIvTitle2.setImageResource(R.drawable.bg_translucent_white_square)

                    }
                galleryEditTvCalendarInput.text = item.photoDate


            }
        }
        binding.apply {
            //사진 클릭이벤트 : 사용자의 갤러리에서 선택한 사진으로 사진을 업로드
            galleryEditTvAdd.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            //달력 클릭이벤트 : 사용자가 선택한 날짜로 사진의 날짜를 업로드
            galleryEditTvCalendar.setOnClickListener {

                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val listener = DatePickerDialog.OnDateSetListener { datePicker, yy, mm, dd ->
                    binding.galleryEditTvCalendarInput.text = "${yy}. ${mm + 1}. ${dd}"
                }
                gallerySharedViewModel.considerNewPhotoDate("${year}.${month + 1}.${day}")
                val picker = DatePickerDialog(requireContext(), listener, year, month, day)
                picker.show()
            }

            //완료버튼 : 새로운 사진으로 등록 또는 변경 후 읽기전용모드로 전환한다.
            galleryEditTvComplete.apply {
                setOnClickListener {
                    gallerySharedViewModel.considerNewPhotoTitle(binding.galleryEditEtTitle.text.toString())
                    if (gallerySharedViewModel.prepared()) {
                        gallerySharedViewModel.updateNewGallery(
                            item.index
                        )
                        setBackgroundResource(R.drawable.icon_write)
                        gallerySharedViewModel.changeLayoutMode("READ")
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