package com.android.petopia.presentation.memory

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.android.petopia.R
import com.android.petopia.data.LoginData
import com.android.petopia.data.Memory
import com.android.petopia.data.UserModel
import com.android.petopia.data.remote.MemoryRepository
import com.android.petopia.data.remote.MemoryRepositoryImpl
import com.android.petopia.databinding.FragmentMemoryBinding
import com.android.petopia.databinding.FragmentMemoryWriteBinding
import com.android.petopia.presentation.memory.ViewModel.MemoryViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class MemoryWriteFragment : DialogFragment() {

    // 여기서 작성한 데이터를 받아서 memoryFragment의 리사이클러뷰에 전달해야함

    private var _binding: FragmentMemoryWriteBinding? = null
    private val binding get() = _binding!!

    private val memoryRepository: MemoryRepository by lazy {
        MemoryRepositoryImpl()
    }

    private lateinit var memoryViewModel: MemoryViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoryWriteBinding.inflate(inflater, container, false)

        memoryViewModel = ViewModelProvider(requireActivity()).get(MemoryViewModel::class.java)
        memoryViewModel.memoryTitle.observe(viewLifecycleOwner) { title ->
            binding.tvMemoryWriteQuestion.text = title
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()

        val memoryRepository = MemoryRepositoryImpl()
        memoryViewModel = ViewModelProvider(
            requireParentFragment(),
            MemoryViewModel.MemoryViewModelFactory(memoryRepository)
        ).get(MemoryViewModel::class.java)

        val currentTime: Long = System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        binding.tvMemoryWriteDate.text = simpleDateFormat.format(currentTime)

        binding.btnMemoryWriteCheck.setOnClickListener {
            saveData()
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }

        binding.btnMemoryWriteExit.setOnClickListener {
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

    private fun saveData() {
        val title = binding.tvMemoryWriteQuestion.text.toString()
        val content = binding.etMemoryWriteContent.text.toString()

        Log.d("MemoryWriteFragment", "제목: $title, 내용: $content")
        // 여기서 작성한 데이터를 받아서 memoryFragment의 리사이클러뷰에 전달해야함
        if (title.isNotEmpty() && content.isNotEmpty()) {
            val memory = Memory(title, content, LoginData.loginUser)
            memoryViewModel.addMemoryList(memory)

            (parentFragment as? MemoryFragment)?.onMemorySaved(memory)
        } else {
            Log.d("MemoryWriteFragment", "제목 또는 내용이 비어있습니다.")
        }


    }

}