package com.djhb.petopia.data

import com.djhb.petopia.R

object GuideLocalDataSource {
    private var guideModel = GuideModel()
    fun getGuideData(pageNumber: Int): GuideModel {

        val progressBarLocalData = listOf(
            "0%",
            "20%",
            "50%",
            "100%"
        )
        val progressTextLocalData = listOf("보호자님 맞이하는 중...", "이름 불러보는 중...", "비슷한 꼬리 쫓아가는 중...", "화면 앞으로 데려오는 중...")

        val guideStoryLocalData = mapOf(
            0 to "누구를 찾아오셨나요?",
            2 to "이름만 불러서는 안되겠어요.\n" +
                    "어떤 외모인지 알려주시겠어요?",
            4 to "보호자님처럼 불러볼게요.\n" +
                    "둘은 어떤 사이였나요?",
            6 to "도와주신 덕분에 금방 찾았네요!\n" +
                    "카메라 조정 좀 하겠습니다.",
            8 to "가장 예쁘고 건강했던 상태로\n" +
                    "펫토피아에 무사히 입국했답니다.",
            9 to "적응하는데 방해가 될 수 있어\n" +
                    "직접 소통하는 건 불가능하지만,",
            10 to "편지나 사진은 전해줄 수 있으니\n" +
                    "언제든지 이용해보세요.",
            11 to "소중한 날짜를 알려주시면\n" +
                    "잊지않게 알려드릴 수도 있습니다.",
            12 to "화면을 아래로 내려서\n" +
                    "메모리 브릿지로 이동해볼까요?",
            14 to "매일 새로운 질문에 답변하며\n" +
                    "추억을 떠올려보기도 하고,",
            15 to "풍선을 클릭해 오늘의 감정을\n" +
                    "알아차리고 나를 이해해보세요.",
            16 to "화면을 아래로 내려서\n" +
                    "지구로 이동해볼까요?",
            18 to "구름을 클릭해 도움이 될만한\n" +
                    "이야기도 만나보고,",
            19 to "나무를 클릭해 여러사람들과\n" +
                    "마음을 나눠보세요.",
            20 to "화살표를 누르면 펫토피아로\n" +
                    "바로 이동하실 수 있답니다.",
            21 to "마이페이지에서는 편의를 제공할\n" +
                    "여러 설정이 가능합니다.",
            22 to "그럼, 치유가 가득\n" +
                    "피어나시기를 바랍니다."
        )

        val guideDialogLocalData = mapOf(
            1 to "NAME", 3 to "APPEARANCE", 5 to "RELATION"
        )

        val guideFunctionLocalData = mapOf(
        9 to "PETOPIA",
        10 to "GALLERY_LETTER",
        11 to "D_DAY",
        13 to "MOVE_MEMORY_BRIDGE",
        14 to "MEMORY_BRIDGE",
        15 to "EMOTION",
        17 to "MOVE_EARTH",
        18 to "CLOUD",
        19 to "COMMUNITY",
        20 to "MOVE_UPPER",
        21 to "MY",
        22 to "END")

        var progressBar = ""
        var progressText = ""
        var guideStory = ""
        var guideDialog = ""
        var guideFunction = ""

        if (guideStoryLocalData[pageNumber] != null) guideStory =
            guideStoryLocalData[pageNumber].toString()
        if (guideDialogLocalData[pageNumber] != null) guideDialog =
            guideDialogLocalData[pageNumber].toString()
        if (guideFunctionLocalData[pageNumber] != null) guideFunction =
            guideFunctionLocalData[pageNumber].toString()

        //페이지에 따라 상단 진행바, 진행문구 변경
        when (pageNumber) {
            in 0..1 -> progressText = progressTextLocalData[0]
            in 2..3 -> {
                progressBar = progressBarLocalData[0]
                progressText = progressTextLocalData[1]
            }

            in 4..5 -> {
                progressBar = progressBarLocalData[1]
                progressText = progressTextLocalData[2]
            }

            in 6..7 -> {
                progressBar = progressBarLocalData[2]
                progressText = progressTextLocalData[3]
            }
        }

        guideModel = GuideModel(
            progressBar,
            progressText,
            guideStory,
            guideDialog,
            guideFunction
        )
        if (pageNumber == 0) {
            guideModel = guideModel.copy(status = "ESSENTIAL")
        }
        if (pageNumber == 8) {
            guideModel = guideModel.copy(status = "ESSENTIAL_DONE")
        }
        if (pageNumber == 9) {
            guideModel = guideModel.copy(status = "OPTIONAL")
        }
        if (pageNumber == 23) {
            guideModel = guideModel.copy(status = "DONE")
        }

        return guideModel
    }

    //가이드 상태를 업데이트 해주는 함수
    fun updateStatusData(pageNumber: Int, updateStatus: String) : GuideModel {
        guideModel = getGuideData(pageNumber).copy(status = updateStatus)
        return guideModel
    }




    fun getPetListData(breed: String): List<PetAppearanceModel> {
        var petList = listOf<PetAppearanceModel>()
        when (breed) {
            "DOG" -> petList =
                listOf(
                    PetAppearanceModel("말티즈"),
                    PetAppearanceModel("푸들"),
                    PetAppearanceModel("치와와"),
                    PetAppearanceModel("포메라니안"),
                    PetAppearanceModel("웰시코기"),
                    PetAppearanceModel("시츄"),
                    PetAppearanceModel("시바"),
                    PetAppearanceModel("비숑"),
                    PetAppearanceModel("리트리버")
                )
            "CAT" -> petList = listOf(
                PetAppearanceModel("코리안숏헤어"),
                PetAppearanceModel("페르시안"),
                PetAppearanceModel("터키쉬앙고라"),
                PetAppearanceModel("샴"),
                PetAppearanceModel("노르웨이숲"),
                PetAppearanceModel("러시안블루"),
                PetAppearanceModel("스코티쉬폴드"),
                PetAppearanceModel("아비시니안"),
                PetAppearanceModel("아메리칸숏헤어")
            )
        }
        return petList
    }
}




