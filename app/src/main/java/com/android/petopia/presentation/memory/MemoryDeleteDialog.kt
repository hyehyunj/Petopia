package com.android.petopia.presentation.memory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petopia.data.Memory
import com.android.petopia.databinding.FragmentDialogBinding
import com.android.petopia.presentation.dialog.DialogFragment

class MemoryDeleteDialog(
    private val memory: Memory,
    private val delete: (Memory) -> Unit
) : DialogFragment() {
    private lateinit var binding: FragmentDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDialogBinding.bind(view!!)

        binding.dialogTvAction.setOnClickListener {
            delete(memory)
            dismiss()
        }
        binding.dialogTvCancel.setOnClickListener {
            dismiss()
        }
        return view
    }


}