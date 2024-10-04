package com.djhb.petopia.presentation.guide

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentDialogBinding
import com.djhb.petopia.presentation.home.MainHomeGuideSharedViewModel

//가이드 취소 다이얼로그
class GuideCancelDialogFragment : DialogFragment() {

    private val _binding: FragmentDialogBinding by lazy {
        FragmentDialogBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var homePetopiaGuideSharedViewModel: MainHomeGuideSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homePetopiaGuideSharedViewModel =
            ViewModelProvider(requireActivity()).get(MainHomeGuideSharedViewModel::class.java)
        binding.dialogTvAction.text = getString(R.string.common_end)

        homePetopiaGuideSharedViewModel.guideStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "ESSENTIAL" -> binding.dialogTvTitle.text =
                    getString(R.string.guide_cancel_incomplete)
                "ESSENTIAL_DONE","OPTIONAL" -> binding.dialogTvTitle.text =
                    getString(R.string.guide_cancel_complete)
            }
        }

        //종료버튼 클릭이벤트
        binding.dialogTvAction.setOnClickListener {
            when (homePetopiaGuideSharedViewModel.guideStateLiveData.value) {
                "ESSENTIAL" -> homePetopiaGuideSharedViewModel.updateGuideState("NONE")
                "ESSENTIAL_DONE","OPTIONAL" -> homePetopiaGuideSharedViewModel.updateGuideState("DONE")
            }
            parentFragmentManager.beginTransaction()
                .remove(GuideFragment())
                .commit()
            dismiss()
        }

        //취소버튼 클릭이벤트
        binding.dialogTvCancel.setOnClickListener {
            dismiss()
        }
        return binding.root
    }


    override fun onResume() {
        super.onResume()
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
        params?.height = (deviceWidth * 0.5).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


}
