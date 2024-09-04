package com.djhb.petopia.presentation.letter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djhb.petopia.data.LetterModel
import com.djhb.petopia.databinding.FragmentDialogBinding
import com.djhb.petopia.presentation.dialog.DialogFragment

class LetterDeleteDialog(
    private val letterModel: LetterModel,
    private val delete: (LetterModel) -> Unit
) : DialogFragment() {
    private lateinit var binding: FragmentDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDialogBinding.bind(view!!)

        binding.dialogTvAction.setOnClickListener {
            delete(letterModel)
            dismiss()
        }
        binding.dialogTvCancel.setOnClickListener {
            dismiss()
        }
        return view
    }
}