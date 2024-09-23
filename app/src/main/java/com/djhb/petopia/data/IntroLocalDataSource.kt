package com.djhb.petopia.data

import android.content.Context
import com.djhb.petopia.R

object IntroLocalDataSource {
    //인트로 화면 구성요소를 만들어주는 함수
    fun getIntroData(): List<IntroModel> {
        val introTitleList = listOf(
            "화면소개",
            "1. 펫토피아",
            "2. 메모리브릿지",
            "3. 어스",
            "이용방법",
            "시작!")

        val introImageList = listOf(
            R.drawable.icon_up_down,
            R.drawable.icon_paw,
            R.drawable.icon_rainbow,
            R.drawable.icon_tree,
            R.drawable.icon_petopia_call,
            R.drawable.logo,)

        val introInfoList = listOf(
            "화면은 3층 구조입니다.\n\n위 아래로 이동해\n여러가지 기능을\n이용해보세요!",
            "무지개다리를 건넌 반려동물은\n유토피아처럼 완벽하고 이상적인\n펫토피아(Petopia)에서 지내고 있대요.\n\n펫토피아를 바라보며\n다시만날 희망을 채워보세요.",
            "펫토피아와 어스를 연결해주는\n메모리브릿지(Memory Bridge)에서\n추억과 감정을 돌아보며\n회복하는 시간을 보내보세요.",
            "우리가 살고 있는 지구,\n어스(Us and Earth)에서\n무지개다리 너머로\n반려동물을 배웅한 사람들과\n마음을 나눠보세요.",
            "펫토피아를 들여다보고\n더 많은 기능을 이용하시려면,\n입국사무소 연결버튼을 눌러보세요.",
            "아직 마음의 준비가 되지 않았다면\n메모리브릿지와 어스에서 지내시다가\n언제든지 연결하셔도 된답니다."
        )

        return introTitleList.indices.map { index ->
            IntroModel(
                introTitleList[index],
                introImageList[index],
                introInfoList[index]
            )
        }
    }

//인트로 생략여부를 업데이트하는 함수
    fun updateSkipIntroData(context: Context, introSkip: Boolean): Boolean {

        val pref = context.getSharedPreferences("pref", 0)
        val edit = pref.edit()
        edit.putString("INTRO_SKIP", introSkip.toString())
        edit.apply()


        return loadSkipIntroData(context)
    }
    //인트로 생략여부를 불러오는 함수
    fun loadSkipIntroData(context: Context): Boolean {
        val pref = context.getSharedPreferences("pref", 0)
        val introSkipData = pref.getString("INTRO_SKIP", "").toBoolean()

        return introSkipData
    }

}




