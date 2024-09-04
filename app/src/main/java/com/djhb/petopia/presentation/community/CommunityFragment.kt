package com.djhb.petopia.presentation.community

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
import com.djhb.petopia.databinding.FragmentImageTestBinding
import com.bumptech.glide.Glide
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.GalleryRepository
import com.djhb.petopia.data.remote.GalleryRepositoryImpl
import com.djhb.petopia.data.remote.MemoryRepository
import com.djhb.petopia.data.remote.MemoryRepositoryImpl
import com.djhb.petopia.data.remote.SignRepository
import com.djhb.petopia.data.remote.SignRepositoryImpl
import com.google.firebase.storage.StorageReference


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

//    private val binding: FragmentCommunityBinding by lazy {
//        FragmentCommunityBinding.inflate(layoutInflater)
//    }

    private val binding: FragmentImageTestBinding by lazy {
        FragmentImageTestBinding.inflate(layoutInflater)
    }

    private val signRepository: SignRepository by lazy{
        SignRepositoryImpl()
    }

    private val memoryRepository: MemoryRepository by lazy{
        MemoryRepositoryImpl()
    }

    private val galleryRepository: GalleryRepository by lazy {
        GalleryRepositoryImpl()
    }

    private var imageUri: Uri? = null

    private val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            imageUri = result.data?.data
        }
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
        val user = UserModel("id1", "password1", "name1", "nickname1")
//        val gallery = GalleryModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initView()




    }

//    private fun initView(){
//        binding.header.ivBack.setOnClickListener{
//            Toast.makeText(requireActivity(), "click back", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.header.ivSearch.setOnClickListener {
//            Toast.makeText(requireActivity(), "click search", Toast.LENGTH_SHORT).show()
//        }
//    }


    private fun initImageView(reference: StorageReference) {
        binding.ivUpload.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                requireActivity().requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }

            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
            )
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            imageLauncher.launch(intent)
        }

        binding.btnOk.setOnClickListener {


            val uploadTask = reference.child("1").child("test2.png")
                .putFile(imageUri ?: throw Exception("image Uri is null"))

            uploadTask.addOnSuccessListener {
                Toast.makeText(requireActivity(), "upload success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireActivity(), "upload fail", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDownload.setOnClickListener {
            val downloadTask = reference.child("1").child("test2.png").downloadUrl
            downloadTask.addOnSuccessListener { uri ->
                Glide.with(requireActivity())
                    .load(uri)
                    .centerCrop()
                    .into(binding.ivDownload)
            }.addOnFailureListener {
                Log.e("FirebaseTest", "Download fail : ${it.message}")
            }
        }
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