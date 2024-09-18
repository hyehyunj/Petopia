package com.djhb.petopia.data

import com.djhb.petopia.R
import java.util.UUID

data class AdminPostModel(
    val category: String,
    val page: Int,
    val title: String,
    val background: Int,
    val post: String,
    val uid: String = UUID.randomUUID().toString()
)

fun getAdminPostLeftItems(): List<AdminPostModel> = listOf(
    AdminPostModel(
        "배웅하기",
        0,
        "무지개다리를 건너기 전 증상",
        R.drawable.bg_gradient_1,
        "어딘가 달라진 행동을 보고\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n조금 더 후회없는 인사를 할 수 있도록\n임종 직전에 많이 나타나는\n신체적 증상을 알려드릴게요."
    ),
    AdminPostModel(
        "배웅하기",

        1, "마지막 인사",
        R.drawable.bg_gradient_2,
        "어딘가 달라진 행동을 보고\n조금 더 후회없는 인사를 할 수 있도록\n임종 직전에 많이 나타나는\n신체적 증상을 알려드릴게요."
    ),
    AdminPostModel(
        "배웅하기",

        2, "먼 여행을 떠난 직후",
        R.drawable.bg_gradient_3,
        "어딘가 달라진 행동을 보고\n조금 더 후회없는 인사를 할 수 있도록\n임종 직전에 많이 나타나는\n신체적 증상을 알려드릴게요."
    ),
    AdminPostModel(
        "배웅하기",

        3, "먼 여행을 떠난 직후",
        R.drawable.bg_gradient_4,
        "어딘가 달라진 행동을 보고\n조금 더 후회없는 인사를 할 수 있도록\n임종 직전에 많이 나타나는\n신체적 증상을 알려드릴게요."
    ),
    AdminPostModel(
        "배웅하기",

        4, "먼 여행을 떠난 직후",
        R.drawable.bg_gradient_5,
        "어딘가 달라진 행동을 보고\n조금 더 후회없는 인사를 할 수 있도록\n임종 직전에 많이 나타나는\n신체적 증상을 알려드릴게요."
    )
)

fun getAdminPostRightItems(): List<AdminPostModel> = listOf(
    AdminPostModel(
        "잘지내기",

        0,
        "펫로스 증후군이란",
        R.drawable.bg_gradient_1,
        "어딘가 달라진 행동을 보고\n조금 더 후회없는 인사를 할 수 있도록\n임종 직전에 많이 나타나는\n신체적 증상을 알려드릴게요."
    ),
    AdminPostModel(
        "잘지내기",

        1, "보고싶을 때 들으면 위로되는 노래",
        R.drawable.bg_gradient_2,
        "어딘가 달라진 행동을 보고\n조금 더 후회없는 인사를 할 수 있도록\n임종 직전에 많이 나타나는\n신체적 증상을 알려드릴게요."
    ),
    AdminPostModel(
        "잘지내기",

        2, "상실감, 어떻게 돌봐야 할까",
        R.drawable.bg_gradient_3,
        "어딘가 달라진 행동을 보고\n조금 더 후회없는 인사를 할 수 있도록\n임종 직전에 많이 나타나는\n신체적 증상을 알려드릴게요."
    ),
    AdminPostModel(
        "잘지내기",

        3, "상실감, 어떻게 돌봐야 할까",
        R.drawable.bg_gradient_4,
        "어딘가 달라진 행동을 보고\n조금 더 후회없는 인사를 할 수 있도록\n임종 직전에 많이 나타나는\n신체적 증상을 알려드릴게요."
    ),
    AdminPostModel(
        "잘지내기",

        4, "상실감, 어떻게 돌봐야 할까",
        R.drawable.bg_gradient_5,
        "어딘가 달라진 행동을 보고\n조금 더 후회없는 인사를 할 수 있도록\n임종 직전에 많이 나타나는\n신체적 증상을 알려드릴게요."
    )
)