package com.djhb.petopia.presentation.community

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.djhb.petopia.FilteringType
import com.djhb.petopia.R
import com.djhb.petopia.Table
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.databinding.FragmentCommunityCreateBinding
import com.djhb.petopia.presentation.community.adapter.CreateImageAdapter
import com.djhb.petopia.presentation.community.adapter.OnclickImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val POST_TYPE = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CommunityCreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var postType: Table

    private val binding: FragmentCommunityCreateBinding by lazy {
        FragmentCommunityCreateBinding.inflate(layoutInflater)
    }

//    private val postRepository: PostRepository by lazy {
//        PostRepositoryImpl()
//    }

//    private val mainActivity: MainActivity by lazy {
//        requireActivity() as MainActivity
//    }

//    private val checkboxToFiltering = mutableMapOf(
//        binding.cbDog to FilteringType.DOG
//        ,binding.cbCat to FilteringType.CAT
//    )

    private lateinit var checkboxToFiltering: MutableMap<CheckBox, FilteringType>

    private val viewModel: CommunityViewModel by viewModels()
//    private val viewModel: CommunityViewModel by lazy {
//        CommunityViewModel(postType)
//    }
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

//                    val intent = Intent(Intent.ACTION_PICK)
//                    intent.setDataAndType(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
//                    )
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                    imageLauncher.launch(intent)

                    imageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                override fun onClickDeleteImage(uri: String) {
                    Log.i("CommunityCreatefragment", "click delete image")
                    imageUris.removeIf {
                        it == uri
                    }
                    viewModel.postAddImageUris.value = imageUris
                }
            })
    }

    private val imageUris = mutableListOf("")

    private val imageLauncher =

        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(6)) { uri ->
            if (uri.isNotEmpty()) {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                uri.forEach{context?.contentResolver?.takePersistableUriPermission(it, flag)}
                imageUris.clear()
                imageUris.add("")
                uri.forEach{
                    imageUris.add(it.toString())
                    Log.i("CommunityCreateFragment", "uri = ${it}")
                }
                viewModel.postAddImageUris.value = imageUris
            }
        }

//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK && result.data != null) {
//                imageUris.clear()
//                imageUris.add("")
//                val clipData = result.data?.clipData
//                Log.d("CommunityCreateFragment", "clipData.size = ${clipData?.itemCount}")
//                if (clipData == null) {
//                    val uri = result.data?.data
//                    imageUris.add(uri.toString())
//                } else {
//                    for (itemIndex in 0 until clipData.itemCount) {
//                        imageUris.add(clipData.getItemAt(itemIndex).uri.toString())
//                    }
//                }
//                viewModel.postAddImageUris.value = imageUris
//            }
//        }

//    private val loginUser =
//        UserModel("testId", "testPassword", "name1", "nickname1", "email@gmail.com")


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postType = it.getParcelable(POST_TYPE, Table::class.java)?:Table.NONE
        }
        viewModel.state.set("postType", postType)
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

        val imageFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        Log.i("CommunityCreateFragment", "currentDate = ${imageFormat.format(System.currentTimeMillis())}")

    }

    private fun initView() {

        checkboxToFiltering = mutableMapOf(
            binding.cbDog to FilteringType.DOG
            ,binding.cbCat to FilteringType.CAT
        )

        binding.header.tvTitle.text = when(postType) {
            Table.QUESTION_POST -> "질문 게시판"
            Table.INFORMATION_POST -> "정보 공유 게시판"
            else -> "갤러리 게시판"
        }

//        binding.header.tvTitle.text = "글쓰기(질문 게시판)"
//        binding.header.ivSearch.isVisible =
        binding.header.ivSearch.visibility = ImageView.INVISIBLE
//        val title = binding.etTitle.text.toString()
//        val content = binding.etContent.text.toString()
        binding.recyclerViewAddImage.adapter = createImageAdapter
//        binding.recyclerViewAddImage.layoutManager = LinearLayoutManager(requireActivity())
//        Log.i("CommunityCreateFragment", "imageUris.size = ${imageUris.size}")
        viewModel.postAddImageUris.value = imageUris

//        createImageAdapter.submitList(imageUris)

    }

    //    private fun initImageView(reference: StorageReference) {
    private fun initListener() {

        val communityActivity = requireActivity() as CommunityActivity
        binding.btnComplete.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            if (title.isBlank() && content.isBlank())
                Toast.makeText(requireActivity(), "제목, 내용을 확인해주세요.", Toast.LENGTH_SHORT).show()
            else if (title.isBlank())
                Toast.makeText(requireActivity(), "제목을 확인 해주세요.", Toast.LENGTH_SHORT).show()
            else if (content.isBlank())
                Toast.makeText(requireActivity(), "내용을 확인 해주세요.", Toast.LENGTH_SHORT).show()
            else if(postType == Table.GALLERY_POST && imageUris.size <= 1){
                Toast.makeText(requireActivity(), "이미지를 1개 이상 첨부해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val selectedCategories = addFilteringCategories()
                    imageUris.removeAt(0)
                    async { viewModel.createPost(PostModel(title, content, LoginData.loginUser, selectedCategories), imageUris)}.await()
                    viewModel.selectRankList(mutableListOf())
                    viewModel.selectInitPostList(mutableListOf())
                    Toast.makeText(requireActivity(), "게시물 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
//                    communityActivity.showViewPager()
//                    requireActivity().supportFragmentManager.popBackStack()
//                    requireActivity().supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.layoutCommunity, CommunityMainFragment())
//                        .commit()
                    val intent = Intent(requireActivity(), CommunityActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()

                }
            }
        }

        binding.header.ivBack.setOnClickListener{
            Toast.makeText(requireActivity(), "click back", Toast.LENGTH_SHORT).show()
            communityActivity.showViewPager()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initObserveModel(){
        viewModel.postAddImageUris.observe(viewLifecycleOwner) {
            Log.i("CommunityCreateFragment", "observe postImageUris")
            Log.i("CommunityCreateFragment", "postImageUris.size = ${viewModel.postAddImageUris.value?.size}")
            for (imageUris1 in viewModel.postAddImageUris.value?: mutableListOf()) {
                Log.i("CommunityCreateFragment", "uri = ${imageUris1}")
            }
            createImageAdapter.submitList(it.toMutableList())
        }
    }

    private fun addFilteringCategories(): MutableList<FilteringType> {
        val selectedCategories = mutableListOf<FilteringType>()

        for (entry in checkboxToFiltering) {
            val checkbox = entry.key
            val category = entry.value
            if(checkbox.isChecked)
                selectedCategories.add(category)
        }

        return selectedCategories
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
                    putString(POST_TYPE, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        @JvmStatic
        fun newInstance(postType: Table) =
            CommunityCreateFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(POST_TYPE, postType)
                }
            }
    }
}