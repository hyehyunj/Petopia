package com.android.petopia.presentation.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.petopia.data.Memory
import com.android.petopia.data.UserModel
import com.android.petopia.data.remote.MemoryRepository
import com.android.petopia.data.remote.MemoryRepositoryImpl
import com.android.petopia.data.remote.SignRepository
import com.android.petopia.data.remote.SignRepositoryImpl
import com.android.petopia.databinding.FragmentCommunityBinding
import kotlinx.coroutines.launch


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

    private val signRepository: SignRepository by lazy{
        SignRepositoryImpl()
    }

    private val memoryRepository: MemoryRepository by lazy{
        MemoryRepositoryImpl()
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



//        val user1 = UserModel("id1", "password1", "name1", "nickname1", "email1@gmail.com")

//        this.lifecycleScope.launch {
//            signRepository.createUser(user1)
//        }

//        this.lifecycleScope.launch {
//            val selectUser1 = signRepository.selectUser("id1")
//            Log.i("CommunityFragment", "selectUser1 = ${selectUser1}")
//
////            val selectUser2 = signRepository.selectUser("id2")
////            Log.i("CommunityFragment", "selectUser2 = ${selectUser2}")
//        }

        val user2 = UserModel("id2", "password2", "name2", "nickname2", "email2@gmail.com")
        val user3 = UserModel("id3", "password3", "name3", "nickname3", "email3@gmail.com")
        val user4 = UserModel("id4", "password4", "name4", "nickname4", "email4@gmail.com")
        val user5 = UserModel("id5", "password5", "name5", "nickname5", "email5@gmail.com")
//
//        val post1 = Post(1, "title1", "content1", user1, 0)
//        val post2 = Post(2, "title2", "content2", user2, 0)
//        val post3 = Post(3, "title3", "content3", user3, 0)
//        val post4 = Post(4, "title4", "content4", user4, 0)
//        val post5 = Post(5, "title5", "content5", user5, 0)

        val memory1 = Memory("title1","content1", user2)
        val memory2 = Memory("title2","content2", user2)
        val memory3 = Memory("title3","content3", user2)
        val memory4 = Memory("title4","content4", user2)

        lifecycleScope.launch {
//            signRepository.createUser(user2)
//            signRepository.createUser(user3)
//            signRepository.createUser(user4)
//            signRepository.createUser(user5)

            //만들어진 순서 : 1, 4, 2, 3
//            memoryRepository.createMemory(memory1)
//            memoryRepository.createMemory(memory4)
//            memoryRepository.createMemory(memory2)
//            memoryRepository.createMemory(memory3)

            val memoryList = memoryRepository.selectMemoryList(user2)
//            memoryList.sortByDescending { it.createdDate }
//
//            for (memory in memoryList) {
//                Log.i("CommunityFragment", "${memory}")
//            }

        }



//
//        FirebaseReference.reference.child("post").child(post1.index.toString()).setValue(post1)
//        FirebaseReference.reference.child("post").child(post2.index.toString()).setValue(post2)
//        FirebaseReference.reference.child("post").child(post3.index.toString()).setValue(post3)
//        FirebaseReference.reference.child("post").child(post4.index.toString()).setValue(post4)
//        FirebaseReference.reference.child("post").child(post5.index.toString()).setValue(post5)
//        database.child("board").push().setValue(board5)





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