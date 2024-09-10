package com.djhb.petopia.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.djhb.petopia.R
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.databinding.FragmentHomeEarthBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.admin.AdminFragment
import com.djhb.petopia.presentation.admin.AdminViewModel
import com.djhb.petopia.presentation.community.Authority
import com.djhb.petopia.presentation.community.CommunityMainFragment
import com.djhb.petopia.presentation.guide.GuideFragment
import com.djhb.petopia.presentation.setting.SettingFragment
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

class HomeEarthFragment : Fragment() {
    private val _binding: FragmentHomeEarthBinding by lazy {
        FragmentHomeEarthBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var mainHomeGuideViewModel: MainHomeGuideSharedViewModel
    private val adminViewModel: AdminViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainHomeGuideViewModel =
            ViewModelProvider(requireActivity()).get(MainHomeGuideSharedViewModel::class.java)

        homeEarthButtonClickListener()
        homeEarthDataObserver()

    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun homeEarthButtonClickListener() {
        //설정버튼 클릭이벤트 : 클릭시 설정 이동
        binding.homeEarthIvMy.setOnClickListener {

            if (LoginData.loginUser.authority == Authority.ADMIN) {
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_sub_frame, AdminFragment()
                    )
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit()
            } else {
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_sub_frame, SettingFragment()
                    )
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit()
            }
        }

        //좌측구름버튼 클릭이벤트 : 클릭시 관리자 추천글 배웅하기 이동
        binding.homeEarthIvCloudLeft.setOnClickListener {
            showUndoToast()
        }

        //우측구름버튼 클릭이벤트 : 클릭시 관리자 추천글 잘지내기 이동
        binding.homeEarthIvCloudRight.setOnClickListener {
            showUndoToast()
        }

        //커뮤니티버튼 클릭이벤트 : 클릭시 커뮤니티 이동
        binding.homeEarthIvCommunity.setOnClickListener {
            if (mainHomeGuideViewModel.guideStateLiveData.value != "DONE") StyleableToast.makeText(
                requireActivity(),
                "가이드 종료 후 이용 가능합니다.",
                R.style.toast_common
            )
                .show() else

            childFragmentManager.beginTransaction()
                .replace(
                    R.id.home_earth_container, CommunityMainFragment()
                )
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()

        }

        //최상단 이동버튼 클릭이벤트 :
        binding.homeEarthFabTop.setOnClickListener {
            (activity as MainActivity).moveToPetopia()
        }


    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun homeEarthDataObserver() {
        //가이드 상태 변화감지 : 가이드 상태에 따라 화면구성 변경


    }

    fun showUndoToast() {
        StyleableToast.makeText(
            requireActivity(),
            getString(R.string.messege_undo),
            R.style.toast_undo
        ).show()
    }


}