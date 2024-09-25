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
import androidx.lifecycle.lifecycleScope
import com.djhb.petopia.FilteringType
import com.djhb.petopia.R
import com.djhb.petopia.Table
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.databinding.FragmentCommunityEditBinding
import com.djhb.petopia.presentation.community.adapter.CreateImageAdapter
import com.djhb.petopia.presentation.community.adapter.OnclickImage
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val POST_TYPE = "postType"

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityEditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var post: PostModel
    private lateinit var postType: Table

    private val binding: FragmentCommunityEditBinding by lazy {
        FragmentCommunityEditBinding.inflate(layoutInflater)
    }

    private lateinit var checkboxToFiltering: MutableMap<CheckBox, FilteringType>

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
//                Log.d("CommunityEditFragment", "clipData.size = ${clipData?.itemCount}")
//                if (clipData == null) {
//                    val uri = result.data?.data
//                    imageUris.add(uri.toString())
//                } else {
//                    for (itemIndex in 0 until clipData.itemCount) {
//                        imageUris.add(clipData.getItemAt(itemIndex).uri.toString())
//                    }
//                }
//                viewModel.postImageUris.value = imageUris
//            }
//        }

    private val createImageAdapter: CreateImageAdapter by lazy {
        CreateImageAdapter(object : OnclickImage {
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

//                val intent = Intent(Intent.ACTION_PICK)
//                intent.setDataAndType(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
//                )
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                imageLauncher.launch(intent)
                imageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            override fun onClickDeleteImage(uri: String) {
                Log.i("CommunityEditFragment", "click delete image")
                imageUris.removeIf {
                    it == uri
                }
                viewModel.postImageUris.value = imageUris
            }
        })
    }

    private val viewModel: CommunityViewModel by lazy {
        CommunityViewModel(postType)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            post = it.getParcelable(ARG_PARAM1, PostModel::class.java)?:PostModel()
            postType = it.getParcelable(POST_TYPE, Table::class.java)?:Table.NONE
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
        initObserveModel()
        initListener()
        initAdapter()

    }

    private fun initView(){

        checkboxToFiltering = mutableMapOf(
            binding.cbDog to FilteringType.DOG
            ,binding.cbCat to FilteringType.CAT
        )

        for (filteringType in post.filteringTypes) {
            val checkbox = getCheckboxByFiltering(filteringType)
            checkbox.isChecked = true
        }

        binding.etTitle.setText(post.title)
        binding.etContent.setText(post.content)
//        Log.i("CommunityEditFragment", "post.key = ${post.key}")
//        Log.i("CommunityEditFragment", "post.imageUris.size = ${post.imageUris.size}")
//        Log.i("CommunityEditFragment", "post.imageUris = ${post.imageUris}")

        if(post.imageUris.size == 0)
            post.imageUris.add("")

        viewModel.postImageUris.value = post.imageUris

        binding.header.ivSearch.visibility = ImageView.INVISIBLE




    }

    private fun initAdapter(){
        binding.recyclerViewAddImage.adapter = createImageAdapter
//        createImageAdapter.submitList(imageUris)
//        viewModel.postImageUris.value = imageUris
    }

    private fun initListener(){
        binding.btnComplete.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()

            if(title.isBlank() && content.isBlank()) {
                Toast.makeText(requireActivity(), "제목, 본문을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(title.isBlank()) {
                Toast.makeText(requireActivity(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(content.isBlank()){
                Toast.makeText(requireActivity(), "본문을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(postType == Table.GALLERY_POST && imageUris.size <= 1){
                Toast.makeText(requireActivity(), "이미지를 1개 이상 첨부해주세요.", Toast.LENGTH_SHORT).show()
            }else {

                if(imageUris.size > 1) {
//                    for (imageUri in imageUris) {
//                        Log.i("CommunityEditFragment", "imageUri = ${imageUri}")
//                    }
                    lifecycleScope.launch {
                        viewModel.deletePostImages(post.key)
                        imageUris.removeAt(0)
                        viewModel.createPostImages(post, imageUris)
                    }
                }

                val filteringCategories = addFilteringCategories()

                viewModel.updatePost(post.copy(
                    title = title,
                    content = content,
                    filteringTypes = filteringCategories
                    )
                )

//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_sub_frame, CommunityMainFragment())
//                    .commit()
                val intent = Intent(requireActivity(), CommunityActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }
        }

        binding.btnCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.header.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


    }

    private fun initObserveModel(){
        viewModel.postImageUris.observe(viewLifecycleOwner) {
            Log.i("CommunityCreateFragment", "observe postImageUris")
//            Log.i("CommunityCreateFragment", "postImageUris.size = ${viewModel.postImageUris.value?.size}")
//            for (imageUris1 in viewModel.postImageUris.value?: mutableListOf()) {
//                Log.i("CommunityCreateFragment", "uri = ${imageUris1}")
//            }
//            createImageAdapter.submitList(viewModel.postImageUris.value?.toMutableList())
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

    private fun getCheckboxByFiltering(filtering: FilteringType): CheckBox {
        for (entry in checkboxToFiltering) {
            val currentFiltering = entry.value
            if(currentFiltering == filtering)
                return entry.key
        }
        return binding.cbDog
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommunityEditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(post: PostModel, postType: Table) =
            CommunityEditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, post)
                    putParcelable(POST_TYPE, postType)
                }
            }

        @JvmStatic
        fun newInstance(postType: Table) =
            CommunityDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(POST_TYPE, postType)
                }
            }
    }
}