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
import com.djhb.petopia.R
import com.djhb.petopia.data.LetterModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.remote.LetterRepositoryImpl
import com.djhb.petopia.databinding.FragmentLetterWriteBinding
import io.github.muddz.styleabletoast.StyleableToast

class LetterWriteFragment(
    private val isEditMode: Boolean = false,
    private val onLetterSaved: ((LetterModel) -> Unit)? = null
) : DialogFragment() {

    private val _binding: FragmentLetterWriteBinding by lazy {
        FragmentLetterWriteBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    private lateinit var letterViewModel: LetterViewModel
    private var letterToEdit: LetterModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val letterRepository = LetterRepositoryImpl()
        val factory = LetterViewModel.LetterViewModelFactory(letterRepository)
        letterViewModel =
            ViewModelProvider(requireActivity(), factory).get(LetterViewModel::class.java)

        //수정하기를 통해 왔을경우
        if (isEditMode) {
            letterViewModel.selectedLetter.observe(viewLifecycleOwner) { selectedLetter ->
                letterToEdit = selectedLetter
                binding.etLetterWriteTitle.setText(selectedLetter.title)
                binding.etLetterWriteContent.setText(selectedLetter.content)
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //편지지 선택


        initDialog()


        val letterRepository = LetterRepositoryImpl()
        letterViewModel = ViewModelProvider(
            requireParentFragment(),
            LetterViewModel.LetterViewModelFactory(letterRepository)
        ).get(LetterViewModel::class.java)

        binding.btnLetterWriteCheck.setOnClickListener {

            if (isEditMode) {
                updateData()
            } else {
                saveLetterData()
            }
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
        params?.height = (deviceWidth * 1.8).toInt()
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
            letterViewModel.setLetterSaved(true)

            (parentFragment as? LetterFragment)?.onLetterSaved(letterModel)
            parentFragmentManager.beginTransaction().remove(this).commit()
        } else {
            StyleableToast.makeText(
                requireActivity(),
                "항목을 모두 입력해주세요",
                R.style.toast_warning
            ).show()
        }
    }

    private fun updateData() {
        val title = binding.etLetterWriteTitle.text.toString()
        val content = binding.etLetterWriteContent.text.toString()

        letterToEdit?.let {
            it.title = title
            it.content = content
            letterViewModel.updateLetterList(it)
            letterViewModel.setLetterSaved(true)
            onLetterSaved?.invoke(it)
            (parentFragment as? LetterFragment)?.onLetterSaved(it)
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }


}