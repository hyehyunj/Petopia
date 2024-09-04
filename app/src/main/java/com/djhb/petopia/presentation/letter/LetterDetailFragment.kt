package com.djhb.petopia.presentation.letter

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
import com.djhb.petopia.data.remote.LetterRepositoryImpl
import com.djhb.petopia.databinding.FragmentLetterDetailBinding


class LetterDetailFragment : DialogFragment() {
    private var _binding: FragmentLetterDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var letterDetailViewModel: LetterViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLetterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialog()

        val letterRepository = LetterRepositoryImpl()
        val factory = LetterViewModel.LetterViewModelFactory(letterRepository)

        letterDetailViewModel =
            ViewModelProvider(requireActivity(), factory).get(LetterViewModel::class.java)

        letterDetailViewModel.selectedLetter.observe(viewLifecycleOwner) { selectedLetter ->
            binding.tvLetterDetailTitle.text = selectedLetter.title
            binding.tvLetterDetailContent.text = selectedLetter.content
        }

        binding.btnLetterDetailExit.setOnClickListener {
            dismiss()
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


}