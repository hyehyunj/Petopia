package com.djhb.petopia.presentation.guide

import android.content.Context
import android.content.DialogInterface
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.djhb.petopia.R
import com.djhb.petopia.data.PetAppearance
import com.djhb.petopia.data.PetAppearanceModel
import com.djhb.petopia.databinding.FragmentGuideAppearanceDialogBinding
import io.github.muddz.styleabletoast.StyleableToast

//반려동물 외형 다이얼로그 프래그먼트 : 사용자로부터 반려동물 외형정보를 입력받는 다이얼로그
class GuideAppearanceDialogFragment : DialogFragment() {
    private val _binding: FragmentGuideAppearanceDialogBinding by lazy {
        FragmentGuideAppearanceDialogBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var guideSharedViewModel: com.djhb.petopia.presentation.guide.GuideSharedViewModel
    private lateinit var guideAppearanceDialogRecyclerViewAdapter: com.djhb.petopia.presentation.guide.GuideAppearanceDialogRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        guideSharedViewModel =
            ViewModelProvider(requireParentFragment()).get(GuideSharedViewModel::class.java)

        initAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //이전으로버튼 클릭이벤트
        binding.guideAppearanceDialogTvBack.setOnClickListener {
            guideSharedViewModel.guideButtonClickListener("BACK")
            dismiss()
        }
        // 클릭이벤트
        guideSharedViewModel.breedLiveData.observe(viewLifecycleOwner) {
            guideSharedViewModel.changeAppearance()
        }

        guideSharedViewModel.appearanceLiveData.observe(viewLifecycleOwner) {
        when (it) {
            "아비시니안" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_abyssinian)
            "아메리칸숏헤어" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_americanshoerthair)
            "비숑" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_bichon)
            "치와와" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_chihuahua)
            "코리안숏헤어" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_koreanshorthair)
            "말티즈" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_maltese)
            "노르웨이숲" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_norwegianforest)
            "페르시안" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_persian)
            "포메라니안" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_pomeranian)
            "푸들" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_poodle)
            "리트리버" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_retriever)
            "러시안블루" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_russianblue)
            "스코티쉬폴드" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_scottishfold)
            "시바" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_shiba)
            "시츄" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_shihtzu)
            "샴" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_siamese)
            "터키쉬앙고라" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_turkishangora)
            "웰시코기" -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_welshcorgi)
            null -> binding.guideAppearanceDialogIvPet.setImageResource(R.drawable.img_shihtzu)
        }

        }

        guideSharedViewModel.appearanceListLiveData.observe(viewLifecycleOwner) {
//            guideAppearanceDialogRecyclerViewAdapter.notifyDataSetChanged(guideSharedViewModel.appearanceMutableListLiveData.toList())

            initAdapter()
        }

        binding.guideAppearanceDialogRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.guide_appearance_dialog_rb_dog -> guideSharedViewModel.changeBreed("DOG")
                R.id.guide_appearance_dialog_rb_cat -> guideSharedViewModel.changeBreed("CAT")
            }
        }

        binding.guideAppearanceDialogTvComplete.setOnClickListener {
            if (guideSharedViewModel.preparedPetData(1)) {
                guideSharedViewModel.guideButtonClickListener("NEXT")
                dismiss()
            } else StyleableToast.makeText(requireActivity(), "외형을 선택해주세요", R.style.toast_warning)
                .show()
        }

    }


    //어댑터 초기화 함수 : 클릭된 종 대분류에 따라 소분류를 리사이클러뷰로 보여주는 함수
    private fun initAdapter() {
        guideAppearanceDialogRecyclerViewAdapter =
            com.djhb.petopia.presentation.guide.GuideAppearanceDialogRecyclerViewAdapter(
                guideSharedViewModel.appearanceListLiveData.value ?: listOf(),
                itemClickListener = { item, position ->
                    guideSharedViewModel.updateSelected(item.copy(selected = true),position)
                    guideSharedViewModel.setPetAppearance(item.name)
                })
        binding.guideAppearanceDialogRv.adapter = guideAppearanceDialogRecyclerViewAdapter
        binding.guideAppearanceDialogRv.layoutManager = GridLayoutManager(requireContext(), 3)
    }


    override fun onResume() {
        super.onResume()

        //다이얼로그 사용자 폰에 맞춰 크기조정
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val size = Point()
        display.getSize(size)
        size.x
        size.y
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceHeight * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
//            requireContext().dialogFragmentResize(this, 0.9f, 0.8f)
//        }
//        private fun Context.dialogFragmentResize(
//            dialogFragment: DialogFragment,
//            width: Float,
//            height: Float,
//        ) {
//            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//            if (Build.VERSION.SDK_INT < 30) {
//                val display = windowManager.defaultDisplay
//                val size = Point()
//                display.getSize(size)
//                val window = dialogFragment.dialog?.window
//                val x = (size.x * width).toInt()
//                val y = (size.y * height).toInt()
//                window?.setLayout(x, y)
//            } else {
//                val rect = windowManager.currentWindowMetrics.bounds
//                val window = dialogFragment.dialog?.window
//                val x = (rect.width() * width).toInt()
//                val y = (rect.height() * height).toInt()
//                window?.setLayout(x, y)
//            }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        requireParentFragment().onResume()
    }
}
