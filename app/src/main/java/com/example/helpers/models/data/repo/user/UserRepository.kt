package com.example.helpers.models.data.repo.user

import com.example.helpers.models.data.request.*
import com.example.helpers.models.data.response.*
import com.example.helpers.util.Resource

interface UserRepository {
    suspend fun getUserInfo(): Resource<UserResponse>
    suspend fun getUserInfo(id:String): Resource<UserResponse>
    suspend fun login(userModelRequest: LoginRequest): Resource<LoginResponse>
    suspend fun signUp(signUpRequest: SignUpRequest): Resource<SignUpResponse>
    suspend fun editAccount(id:String, editAccountRequest: EditAccountRequest): Resource<EditAccountResponse>
    suspend fun depositCoins(depositCoinsRequest: DepositCoinsRequest): Resource<MomoResponse>
    suspend fun withdrawCoins(withdrawCoinsResponse: WithdrawCoinsRequest): Resource<WithdrawCoinsResponse>
    suspend fun joinGroups(listGroupId:List<String>):Resource<JoinGroupResponse>
}