package com.android.petopia.data.remote

import com.android.petopia.presentation.community.User

interface SignRepository {
    
    // 회원가입
    suspend fun createUser(user: User)
    
    // 로그인(id로 user 조회)
    suspend fun selectUser(id: String): User?

    // 정보 수정(user 받아서 수정)

    suspend fun updateUser(user: User)
    
}