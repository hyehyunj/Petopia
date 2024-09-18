package com.djhb.petopia

enum class BlockType (val typeName: String){
    NONE("차단 안함"),
    POST_BLOCK("게시물/댓글 차단"),
    USER_BLOCK("대상자 차단")

}