package com.djhb.petopia.presentation.my

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.asksira.dropdownview.OnDropDownSelectionListener
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentMyPetEditBinding
import com.djhb.petopia.presentation.guide.GuideSharedViewModel
import com.djhb.petopia.presentation.guide.GuideSharedViewModelFactory
import io.github.muddz.styleabletoast.StyleableToast


//반려동물 정보 수정 프래그먼트
class MyPetEditFragment : DialogFragment() {
    private val _binding: FragmentMyPetEditBinding by lazy {
        FragmentMyPetEditBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val myPetEditViewModel by viewModels<MyPetEditViewModel> {
        MyPetEditViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myPetEditClickListener()
        setDropDownData()
    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun myPetEditClickListener() {
        binding.apply {
            //반려동물 외모 드롭다운 클릭 이벤트
            myPetEditDdvAppearance.onSelectionListener =
                OnDropDownSelectionListener { dropDownView, position ->
                    when (position) {
                        0 -> {myPetEditViewModel.setPetAppearance("리트리버")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_retriever)
                        }
                        1 -> {myPetEditViewModel.setPetAppearance("말티즈")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_maltese)
                        }
                        2 -> {myPetEditViewModel.setPetAppearance("비숑")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_bichon)
                        }
                        3 -> {myPetEditViewModel.setPetAppearance("시바")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_shiba)
                        }
                        4 -> {myPetEditViewModel.setPetAppearance("시츄")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_shihtzu)
                        }
                        5 -> {myPetEditViewModel.setPetAppearance("웰시코기")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_welshcorgi)
                        }
                        6 -> {myPetEditViewModel.setPetAppearance("치와와")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_chihuahua)
                        }
                        7 -> {myPetEditViewModel.setPetAppearance("포메라니안")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_pomeranian)
                        }
                        8 -> {myPetEditViewModel.setPetAppearance("푸들")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_poodle)
                        }
                        9 -> {myPetEditViewModel.setPetAppearance("노르웨이숲")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_norwegianforest)
                        }
                        10 -> {myPetEditViewModel.setPetAppearance("러시안블루")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_russianblue)
                        }
                        11 -> {myPetEditViewModel.setPetAppearance("샴")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_abyssinian)
                        }
                        12 -> {myPetEditViewModel.setPetAppearance("스코티쉬폴드")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_scottishfold)
                        }
                        13 -> {myPetEditViewModel.setPetAppearance("아메리칸숏헤어")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_americanshoerthair)
                        }
                        14 -> {myPetEditViewModel.setPetAppearance("아비시니안")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_abyssinian)
                        }
                        15 -> {myPetEditViewModel.setPetAppearance("코리안숏헤어")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_koreanshorthair)
                        }
                        16 -> {myPetEditViewModel.setPetAppearance("터키쉬앙고라")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_turkishangora)
                        }
                        17 -> {myPetEditViewModel.setPetAppearance("페르시안")
                            binding.myPetEditIvAppearance.setImageResource(R.drawable.img_persian)
                        }
                    }
                }
            //반려동물 관계 드롭다운 클릭 이벤트
            myPetEditDdvRelation.onSelectionListener =
                OnDropDownSelectionListener { dropDownView, position ->
                    when (position) {
                        0 -> myPetEditViewModel.setPetRelation("CHILD")
                        1 -> myPetEditViewModel.setPetRelation("YOUNGER")
                        2 -> myPetEditViewModel.setPetRelation("FRIEND")
                    }
                }
            //완료버튼 클릭 이벤트
            myPetEditBtnComplete.setOnClickListener {
                binding.myPetEditEtName.text
                if (myPetEditViewModel.preparedPetData()) {
                    dismiss()
                } else StyleableToast.makeText(requireActivity(), "빈 항목을 확인해주세요.", R.style.toast_warning)
                    .show()

            }

            myPetEditBtnBack.setOnClickListener {
                dismiss()
            }
        }

    }


    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun myPetEditDataObserver() {

    }


    private fun setDropDownData() {



        binding.apply {
            myPetEditDdvAppearance.setDropDownListItem(myPetEditViewModel.getAppearanceListData())
            myPetEditDdvRelation.setDropDownListItem(myPetEditViewModel.getRelationListData())
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