package com.djhb.petopia.data

data class GuideModel(
    val progressBar: Int = 3,
    val progressText: String = "",
    val guideStory: String,
    val dialog: Int,
    val completeFirstGuide: Boolean = false
)

object Guide {
    fun getGuideData(index: Int) = initGuideData(index)

    private fun initGuideData(index: Int): GuideModel {
        val progressBarData = listOf(0, 1, 2)
        val progressTextData = listOf("보호자", "이름", "비슷한", "화면")
        val guideStoryData = mapOf(
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
        val guideDialogData = mapOf(
            1 to 0, 3 to 1, 5 to 2
        )

        val guideModel = GuideModel(
            progressBarData[index],
            progressTextData[index],
            guideStoryData[index] ?: "",
            guideDialogData[index] ?: 3
        )

        return guideModel

    }}


