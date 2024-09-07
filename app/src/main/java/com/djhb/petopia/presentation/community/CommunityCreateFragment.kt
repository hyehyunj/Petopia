package com.djhb.petopia.presentation.community

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.PostRepository
import com.djhb.petopia.data.remote.PostRepositoryImpl
import com.djhb.petopia.databinding.FragmentCommunityCreateBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.community.adapter.CreateImageAdapter
import com.djhb.petopia.presentation.community.adapter.OnclickImage
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityCreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val binding: FragmentCommunityCreateBinding by lazy {
        FragmentCommunityCreateBinding.inflate(layoutInflater)
    }

//    private val postRepository: PostRepository by lazy {
//        PostRepositoryImpl()
//    }

    private val mainActivity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    private val viewModel: CommunityViewModel by activityViewModels()
    private val detailViewModel: CommunityDetailViewModel by activityViewModels()

    private val createImageAdapter: CreateImageAdapter by lazy {
            CreateImageAdapter(object : OnclickImage{
                override fun onClickAddImage() {
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

                override fun onClickDeleteImage(uri: String) {
                    Log.i("CommunityCreatefragment", "click delete image")
                    imageUris.removeIf {
                        it == uri
                    }
                    viewModel.postImageUris.value = imageUris
                }
            })
    }

    private val imageUris = mutableListOf("")

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                imageUris.clear()
                imageUris.add("")
                val clipData = result.data?.clipData
                Log.d("CommunityCreateFragment", "clipData.size = ${clipData?.itemCount}")
                if (clipData == null) {
                    val uri = result.data?.data
                    imageUris.add(uri.toString())
                } else {
                    for (itemIndex in 0 until clipData.itemCount) {
                        imageUris.add(clipData.getItemAt(itemIndex).uri.toString())
                    }
                }
                viewModel.postImageUris.value = imageUris
            }
        }

    private val loginUser =
        UserModel("testId", "testPassword", "name1", "nickname1", "email@gmail.com")


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initObserveModel()

//        val user = UserModel("devTest", "11qqaa..")
//
//        val comment1 = CommentModel("254d2520-dd53-45f9-9753-55c28e9b2414", LoginData.loginUser, "content1")
//        val comment2 = CommentModel("254d2520-dd53-45f9-9753-55c28e9b2414", user, "content2")
//        val comment3 = CommentModel("254d2520-dd53-45f9-9753-55c28e9b2414", LoginData.loginUser, "content3")
//        val comment4 = CommentModel("254d2520-dd53-45f9-9753-55c28e9b2414", user, "content4")
//        val comment5 = CommentModel("254d2520-dd53-45f9-9753-55c28e9b2414", user, "content5")
//        val comment6 = CommentModel("254d2520-dd53-45f9-9753-55c28e9b2414", LoginData.loginUser, "content6")
//
//        lifecycleScope.launch {
//            detailViewModel.createComment(comment1)
//            detailViewModel.createComment(comment2)
//            detailViewModel.createComment(comment3)
//            detailViewModel.createComment(comment4)
//            detailViewModel.createComment(comment5)
//            detailViewModel.createComment(comment6)
//        }


    }

    private fun initView() {
        binding.header.tvTitle.text = "글쓰기(질문 게시판)"
        binding.header.ivSearch.isVisible = false
//        val title = binding.etTitle.text.toString()
//        val content = binding.etContent.text.toString()
        binding.recyclerViewAddImage.adapter = createImageAdapter
//        binding.recyclerViewAddImage.layoutManager = LinearLayoutManager(requireActivity())
        Log.i("CommunityCreateFragment", "imageUris.size = ${imageUris.size}")
        viewModel.postImageUris.value = imageUris

//        createImageAdapter.submitList(imageUris)

    }

    //    private fun initImageView(reference: StorageReference) {
    private fun initListener() {

            val mainActivity = requireActivity() as MainActivity
        binding.btnComplete.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            Log.i("CommunityCreateFragment", "click complete")
            Log.i("CommunityCreateFragment", "title = ${title}")
            Log.i("CommunityCreateFragment", "content = ${content}")
            if (title.isBlank() && content.isBlank())
                Toast.makeText(requireActivity(), "제목, 내용을 확인해주세요.", Toast.LENGTH_SHORT).show()
            else if (title.isBlank())
                Toast.makeText(requireActivity(), "제목을 확인 해주세요.", Toast.LENGTH_SHORT).show()
            else if (content.isBlank())
                Toast.makeText(requireActivity(), "내용을 확인 해주세요.", Toast.LENGTH_SHORT).show()
            else {
                lifecycleScope.launch {
                    imageUris.removeAt(0)
                    Log.i("CommunityCreateFragment", "imageUris.size = ${imageUris.size}")
//                    postRepository.createPost(PostModel(title, content, loginUser), imageUris)
                    viewModel.createPost(PostModel(title, content, loginUser), imageUris)
                    Toast.makeText(requireActivity(), "게시물 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    mainActivity.showViewPager()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

        binding.header.ivBack.setOnClickListener{
            Toast.makeText(requireActivity(), "click back", Toast.LENGTH_SHORT).show()
            mainActivity.showViewPager()
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnCancel.setOnClickListener {
            mainActivity.showViewPager()
            requireActivity().supportFragmentManager.popBackStack()
        }

//        binding.ivAddImage.setOnClickListener {
//
//            if (ContextCompat.checkSelfPermission(
//                    requireActivity(),
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//                == PackageManager.PERMISSION_DENIED
//            ) {
//                requireActivity().requestPermissions(
//                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                    1
//                )
//            }
//
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.setDataAndType(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
//            )
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            imageLauncher.launch(intent)
//        }

//        binding.btnOk.setOnClickListener {
//
//
//            val uploadTask = reference.child("1").child("test2.png")
//                .putFile(imageUri ?: throw Exception("image Uri is null"))
//
//            uploadTask.addOnSuccessListener {
//                Toast.makeText(requireActivity(), "upload success", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                Toast.makeText(requireActivity(), "upload fail", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.btnDownload.setOnClickListener {
//            val downloadTask = reference.child("1").child("test2.png").downloadUrl
//            downloadTask.addOnSuccessListener { uri ->
//                Glide.with(requireActivity())
//                    .load(uri)
//                    .centerCrop()
//                    .into(binding.ivDownload)
//            }.addOnFailureListener {
//                Log.e("FirebaseTest", "Download fail : ${it.message}")
//            }
//        }
    }

    private fun initObserveModel(){
        viewModel.postImageUris.observe(viewLifecycleOwner) {
            Log.i("CommunityCreateFragment", "observe postImageUris")
            Log.i("CommunityCreateFragment", "postImageUris.size = ${viewModel.postImageUris.value?.size}")
            for (imageUris1 in viewModel.postImageUris.value?: mutableListOf()) {
                Log.i("CommunityCreateFragment", "uri = ${imageUris1}")
            }
//            createImageAdapter.submitList(viewModel.postImageUris.value?.toMutableList())
            createImageAdapter.submitList(it.toMutableList())
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommunityCreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommunityCreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}