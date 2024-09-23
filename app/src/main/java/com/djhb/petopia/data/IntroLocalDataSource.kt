package com.djhb.petopia.data

import com.djhb.petopia.R

object IntroLocalDataSource {
    fun getIntroData(): List<IntroModel> {


        val introTitleList = listOf(
            "화면소개",
            "펫토피아",
            "메모리브릿지",
            "어스",
            "이용방법",
            "시작")

        val introImageList = listOf(
            R.drawable.icon_up_down,
            R.drawable.icon_paw,
            R.drawable.icon_rainbow,
            R.drawable.icon_tree,
            R.drawable.icon_petopia_call,
            R.drawable.logo,)

        val introInfoList = listOf(
            "화면이 3층 구조 되어있습니다.\n\n위 아래로 이동해\n여러가지 기능을\n이용해보세요!",
            "사랑하는 나의 반려동물은\n유토피아처럼 완벽하고 이상적인\n펫토피아(Petopia)에서 지내고 있어요.\n\n최상층 펫토피아에 머물며\n희망을 채워보세요.",
            "펫토피아와 어스를 연결해주는\n메모리브릿지(Memory Bridge)에서\n추억과 감정을 돌아보며\n회복하는 시간을 보내보세요.",
            "우리가 살고 있는 지구,\n어스(Us and Earth)에서\n나처럼 무지개다리 너머로\n반려동물을 배웅한 사람들과\n마음을 나눠보세요.",
            "반려동물을 만나고\n더 많은 기능을 이용하시려면\n펫토피아 입국사무소에 전화해\n화면을 연결해보세요.",
            "아직 마음의 준비가 되지 않았다면\n메모리브릿지나 어스에서 지내시다가\n언제든지 전화하셔도 된답니다."
        )

        return introTitleList.indices.map { index ->
            IntroModel(
                introTitleList[index],
                introImageList[index],
                introInfoList[index]
            )
        }
    }
}




