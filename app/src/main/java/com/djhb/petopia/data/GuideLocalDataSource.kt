package com.djhb.petopia.data

class GuideLocalDataSource {

    fun getGuideData(pageNumber: Int): GuideModel {
        val progressBarLocalData = listOf(0, 1, 2)
        val progressTextLocalData = listOf("보호자", "이름", "비슷한", "화면")
        val guideStoryLocalData = mapOf(
            0 to "누구를 찾아오셨나요?",
            2 to "어떤 외모",
            4 to "처럼",
            6 to "도와",
            8 to "보시는",
            9 to "펫토피아",
            10 to "편지나 사진은",
            11 to "소중한 날짜를",
            12 to "메모리 브릿지로",
            14 to "매일 새로운 질문에",
            15 to "풍선을",
            16 to "지구로",
            18 to "구름을",
            19 to "나무는",
            20 to "이동버튼",
            21 to "설정",
            22 to "그럼"
        )
        val guideDialogLocalData = mapOf(
            1 to 0, 3 to 1, 5 to 2
        )

        var progressBar = 9
        var progressText = ""
        var guideStory = ""
        var guideDialog = 9

        if (guideStoryLocalData[pageNumber] != null) guideStory =
            guideStoryLocalData[pageNumber].toString()
        if (guideDialogLocalData[pageNumber] != null) guideDialog =
            guideDialogLocalData[pageNumber]!!

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

        var guideModel = GuideModel(
            progressBar,
            progressText,
            guideStory,
            guideDialog
        )

        if (pageNumber == 8) {
            guideModel = guideModel.copy(completeFirstGuide = true)
        }

        return guideModel
    }



    fun getPetListData(breed: String): List<String> {
        var petList = listOf<String>()
        when (breed) {
            "DOG" -> petList =
                listOf("말티즈", "푸들", "치와와", "포메라니안", "웰시코기", "시츄", "시바", "비숑", "리트리버")

            "CAT" -> petList = listOf(
                "코리안숏헤어",
                "페르시안",
                "터키쉬앙고라",
                "샴",
                "노르웨이숲",
                "러시안블루",
                "스코티쉬폴드",
                "아비시니안",
                "아메리칸숏헤어"
            )
        }
        return petList
    }
}




