package com.android.petopia.presentation.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.petopia.DateFormatUtils
import com.android.petopia.data.remote.SignRepository
import com.android.petopia.data.remote.SignRepositoryImpl
import com.android.petopia.databinding.FragmentCommunityBinding
import com.android.petopia.network.FirebaseReference
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.async
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



        val user1 = User("id1", "password1", "name1", "nickname1", "email1@gmail.com")

        this.lifecycleScope.launch {
            signRepository.createUser(user1)
        }

        this.lifecycleScope.launch {
            val selectUser1 = signRepository.selectUser("id1")
            Log.i("CommunityFragment", "selectUser1 = ${selectUser1}")

//            val selectUser2 = signRepository.selectUser("id2")
//            Log.i("CommunityFragment", "selectUser2 = ${selectUser2}")
        }

//        val user2 = User("id2", "password2", "name2", "nickname2", "email2@gmail.com")
//        val user3 = User("id3", "password3", "name3", "nickname3", "email3@gmail.com")
//        val user4 = User("id4", "password4", "name4", "nickname4", "email4@gmail.com")
//        val user5 = User("id5", "password5", "name5", "nickname5", "email5@gmail.com")
//
//        val post1 = Post(1, "title1", "content1", user1, 0)
//        val post2 = Post(2, "title2", "content2", user2, 0)
//        val post3 = Post(3, "title3", "content3", user3, 0)
//        val post4 = Post(4, "title4", "content4", user4, 0)
//        val post5 = Post(5, "title5", "content5", user5, 0)
//
//
//        FirebaseReference.reference.child("users").child(user1.id).setValue(user1)
//        FirebaseReference.reference.child("users").child(user2.id).setValue(user2)
//        FirebaseReference.reference.child("users").child(user3.id).setValue(user3)
//        FirebaseReference.reference.child("users").child(user4.id).setValue(user4)
//        FirebaseReference.reference.child("users").child(user5.id).setValue(user5)
//
//        FirebaseReference.reference.child("post").child(post1.index.toString()).setValue(post1)
//        FirebaseReference.reference.child("post").child(post2.index.toString()).setValue(post2)
//        FirebaseReference.reference.child("post").child(post3.index.toString()).setValue(post3)
//        FirebaseReference.reference.child("post").child(post4.index.toString()).setValue(post4)
//        FirebaseReference.reference.child("post").child(post5.index.toString()).setValue(post5)
//        database.child("board").push().setValue(board5)




//        FirebaseReference.reference.child("board").orderByChild("createdDate").get().addOnSuccessListener {
////        database.child("users").orderByChild("id").get().addOnSuccessListener {
//
//            val children = it.children
//
//            for (child in children) {
//                val hashMap = child.value as HashMap<*, *>
//                val gson = Gson()
//                val toJson = gson.toJson(hashMap)
//                val convertedPost = gson.fromJson(toJson, Post::class.java)
//
////                Log.i("CommunityFragment", "now json = ${gson.toJson(LocalDateTime.now())}")
//
////                Log.i("CommunityFragment", "createdDate = ${gsonLocalDateTime.}")
//                Log.i("CommunityFragment", "updatedDate = ${DateFormatUtils.convertToPostFormat(convertedPost.updatedDate)}")
//            }
//
//        }.addOnFailureListener{
//            Log.e("CommunityFragment", "Error getting data", it)
//        }

//        FirebaseReference.reference.child("user").orderByChild("createdDate").get().addOnSuccessListener {
//        FirebaseReference.reference.child("users").orderByChild("id").equalTo("id1").get().addOnSuccessListener {
//
//            val children = it.children
//
//            for (child in children) {
//                val hashMap = child.value as HashMap<*, *>
//                val gson = Gson()
//                val toJson = gson.toJson(hashMap)
//                val convertedUser = gson.fromJson(toJson, User::class.java)
//
////                Log.i("CommunityFragment", "now json = ${gson.toJson(LocalDateTime.now())}")
//
////                Log.i("CommunityFragment", "createdDate = ${gsonLocalDateTime.}")
//                Log.i("CommunityFragment", "updatedDate = ${convertedUser}")
//            }
//
////            Log.i("CommunityFragment", "selected user = ${fromJson}")
//
//        }.addOnFailureListener{
//            Log.e("CommunityFragment", "Error getting data", it)
//        }



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