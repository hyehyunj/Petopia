package com.djhb.petopia.presentation.letter

import android.content.Context
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
import com.djhb.petopia.data.LetterModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.remote.LetterRepositoryImpl
import com.djhb.petopia.databinding.FragmentLetterWriteBinding

class LetterWriteFragment : DialogFragment() {
    private var _binding: FragmentLetterWriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var letterPadViewModel: LetterPadViewModel
    private lateinit var letterViewModel: LetterViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLetterWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //편지지 선택
        letterPadViewModel =
            ViewModelProvider(requireActivity()).get(LetterPadViewModel::class.java)
        letterPadViewModel.selectBackgroundResId.observe(viewLifecycleOwner) { resId ->
            resId?.let {
                binding.letterWriteLayout.setBackgroundResource(it)
            }
        }

        initDialog()

        val letterRepository = LetterRepositoryImpl()
        letterViewModel = ViewModelProvider(
            requireParentFragment(),
            LetterViewModel.LetterViewModelFactory(letterRepository)
        ).get(LetterViewModel::class.java)

        binding.btnLetterWriteCheck.setOnClickListener {
            saveLetterData()
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }

        binding.btnLetterWriteExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
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

    private fun saveLetterData() {
        val title = binding.etLetterWriteTitle.text.toString()
        val content = binding.etLetterWriteContent.text.toString()

        Log.d("LetterWriteFragment", "제목: $title, 내용: $content")

        if (title.isNotEmpty() && content.isNotEmpty()) {
            val letterModel = LetterModel(title, content, LoginData.loginUser)
            letterViewModel.addLetterList(letterModel)

            (parentFragment as? LetterFragment)?.onLetterSaved(letterModel)
        } else {
            Log.d("LetterWriteFragment", "제목 또는 내용이 비어있습니다.")
        }
    }


}