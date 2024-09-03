package com.android.petopia.presentation.letter

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
import androidx.recyclerview.widget.GridLayoutManager
import com.android.petopia.R
import com.android.petopia.databinding.FragmentLetterWritingPadBinding


class LetterWritingPadFragment : DialogFragment() {

    private var _binding: FragmentLetterWritingPadBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLetterWritingPadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt("selectedBackground")?.let { resId ->
            binding.root.setBackgroundResource(resId)
        }

        initDialog()
        selectBackground()
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

    private fun selectBackground() {
        val backgroungList = listOf(
            R.drawable.testpad1,
            R.drawable.testpad2,
            R.drawable.testpad3
        )
        val adapter = LetterWritepadAdapter(backgroungList) { selectedBackground ->
            val viewModel = ViewModelProvider(requireActivity()).get(LetterPadViewModel::class.java)
            viewModel.selectBackgroundResId = selectedBackground
            dismiss()

        }
        binding.rvWritepadList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvWritepadList.adapter = adapter

    }

    private fun showLetterWriteFragment() {
        val fragment = LetterWriteFragment().apply {
            arguments = Bundle().apply {
                ViewModelProvider(requireActivity()).get(LetterPadViewModel::class.java).selectBackgroundResId?.let {
                    putInt(
                        "selectedBackground",
                        it
                    )
                }
            }
        }
        fragment.show(parentFragmentManager, "LETTER_WRITE_FRAGMENT")
    }
}
// 동등한 수준의 프래그먼트로 호출하여(자식이 아닌) 뒤로가기 하거나 완료했을때 편지지 선택이 다시 뜨지 않게 함
}