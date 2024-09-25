package com.djhb.petopia.presentation.album

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.djhb.petopia.R
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.databinding.FragmentAlbumEditBinding
import com.djhb.petopia.presentation.gallery.AlbumEditRecyclerViewAdapter
import io.github.muddz.styleabletoast.StyleableToast

//포토프래그먼트 : 갤러리에서 사진 조회, 추가, 수정할 때 나타나는 프래그먼트
class AlbumEditFragment : DialogFragment() {

    private val _binding: FragmentAlbumEditBinding by lazy {
        FragmentAlbumEditBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var albumEditRecyclerViewAdapter: AlbumEditRecyclerViewAdapter
    private lateinit var albumSharedViewModel: AlbumSharedViewModel
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(6)) { uri ->
            if (uri.isNotEmpty()) {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    uri.forEach{context?.contentResolver?.takePersistableUriPermission(it, flag)}
                albumSharedViewModel.considerNewAlbumListUri(uri)
            }
        }
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) result.uriContent?.let { albumSharedViewModel.changeCropUri(it) }
        else StyleableToast.makeText(
            requireActivity(),
            "사진을 편집할 수 없습니다.",
            R.style.toast_warning
        ).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        albumSharedViewModel =
            ViewModelProvider(requireParentFragment()).get(AlbumSharedViewModel::class.java)
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //갤러리에서 선택한 모드에 따라 레이아웃 변경
        albumSharedViewModel.layoutModeLiveData.observe(viewLifecycleOwner) {
           albumSharedViewModel.currentAlbumLiveData.value?.let { currentPhoto ->
                    addOrEditMode(
                        it, currentPhoto
                    )
                }
        }

        //사용자가 선택한 사진으로 변경
        albumSharedViewModel.newUriListLiveData.observe(viewLifecycleOwner) {
            albumEditRecyclerViewAdapter.updateList(it)
        }

        //닫기 버튼이벤트 : 클릭시 갤러리로 이동
        binding.galleryEditTvExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }

    }


    //어댑터 초기화 함수 : 사용자 입력 사진을 리사이클러뷰로 보여주는 함수.
    private fun initAdapter() {
        albumEditRecyclerViewAdapter = AlbumEditRecyclerViewAdapter(
            albumSharedViewModel.newUriListLiveData.value ?: listOf(),
            removeItemClickListener = { item, position ->
                albumSharedViewModel.removeNewUriList(position)
            },
            cropItemClickListener = { item, position ->
                if(albumSharedViewModel.layoutModeLiveData.value == "EDIT") StyleableToast.makeText(
                    requireActivity(),
                    "저장된 사진은 편집할 수 없습니다.",
                    R.style.toast_warning
                ).show()
                else cropImage(item, position)}
        )
        binding.galleryEditRv.adapter = albumEditRecyclerViewAdapter
        binding.galleryEditRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun cropImage(uri: Uri, position: Int) {
        albumSharedViewModel.updateCropPosition(position)
        cropImage.launch(
            CropImageContractOptions(
            uri = uri,
            cropImageOptions = CropImageOptions()
        )
        )
    }



    //추가 or 편집모드 함수 : 레이아웃을 입력가능한 모드로 구성한다.
    private fun addOrEditMode(layoutMode: String, item: GalleryModel) {
        //편집모드는 이전 데이터를 불러온다.
        if (layoutMode == "EDIT") {
            binding.apply {
                albumSharedViewModel.prepareNewAlbumList()
                galleryEditEtTitle.setText(item.titleText)
                galleryEditTvCalendarInput.text = if(item.photoDate == "")getString(R.string.album_select_date) else item.photoDate
            }
        }
        binding.apply {
            //사진 클릭이벤트 : 사용자의 갤러리에서 선택한 사진으로 사진을 업로드
            galleryEditIvTitleBackground.setOnClickListener {
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
                    albumSharedViewModel.considerNewAlbumListDate("${yy}.${mm + 1}.${dd}")
                }
                val picker = DatePickerDialog(requireContext(), listener, year, month, day)
                picker.show()
            }

            //완료버튼 : 새로운 사진으로 등록 또는 변경 후 읽기전용모드로 전환한다.
            galleryEditTvComplete.apply {
                setOnClickListener {
                    albumSharedViewModel.considerNewUriListTitle(binding.galleryEditEtTitle.text.toString())
                    if (albumSharedViewModel.checkPreparedNewAlbumList()) {
                        albumSharedViewModel.updateAlbumList(
                            item.index
                        )
                        setBackgroundResource(R.drawable.icon_write)
                        albumSharedViewModel.changeLayoutMode("READ")
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