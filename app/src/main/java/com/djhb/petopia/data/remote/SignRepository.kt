package com.djhb.petopia.data.remote

import com.djhb.petopia.data.UserModel

interface SignRepository {
    
    // 회원가입
    suspend fun createUser(user: UserModel)
    
    // 로그인(id로 user 조회)
    suspend fun selectUser(id: String): UserModel?

    // 정보 수정(user 받아서 수정)

    suspend fun updateUser(user: UserModel)

    suspend fun deleteUser(id: String)
    
}