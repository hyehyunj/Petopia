package com.djhb.petopia.presentation.memory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djhb.petopia.data.Memory
import com.djhb.petopia.databinding.FragmentDialogBinding
import com.djhb.petopia.presentation.dialog.DialogFragment

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