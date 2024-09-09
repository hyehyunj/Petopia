package com.djhb.petopia

enum class ReportReasonType(val typeName: String) {

    UNPLEASANT_CONTENT("불쾌한 내용"),
    INCORRECT_INFORMATION("잘못된 내용"),
    LEGAL_PROBLEM("법적 문제"),
    SPAM_OR_ADD("스팸 또는 광고"),
    ETC("기타")

}