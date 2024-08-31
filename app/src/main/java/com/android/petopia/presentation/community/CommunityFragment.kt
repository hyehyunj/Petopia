package com.android.petopia.presentation.community

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.petopia.data.Gallery
import com.android.petopia.data.GalleryModel
import com.android.petopia.data.Memory
import com.android.petopia.data.UserModel
import com.android.petopia.data.remote.GalleryRepository
import com.android.petopia.data.remote.GalleryRepositoryImpl
import com.android.petopia.data.remote.MemoryRepository
import com.android.petopia.data.remote.MemoryRepositoryImpl
import com.android.petopia.data.remote.SignRepository
import com.android.petopia.data.remote.SignRepositoryImpl
import com.android.petopia.databinding.FragmentCommunityBinding
import com.android.petopia.databinding.FragmentImageTestBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val binding: FragmentCommunityBinding by lazy {
        FragmentCommunityBinding.inflate(layoutInflater)
    }

//    private val binding: FragmentImageTestBinding by lazy {
//        FragmentImageTestBinding.inflate(layoutInflater)
//    }

    private val signRepository: SignRepository by lazy{
        SignRepositoryImpl()
    }

    private val memoryRepository: MemoryRepository by lazy{
        MemoryRepositoryImpl()
    }

    private val galleryRepository: GalleryRepository by lazy {
        GalleryRepositoryImpl()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommunityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommunityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}