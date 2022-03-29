package com.example.helpers.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpers.models.data.repo.user.UserRepository
import com.example.helpers.models.data.response.UserInfo
import com.example.helpers.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository):ViewModel(){
    private  val _user= MutableLiveData<UserInfo>()
    val user = _user
    var searchBarState = mutableStateOf(false)
    var searchValue = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    fun onSearchBarStateChange(newValue:Boolean){
        searchBarState.value=newValue
    }

    fun onSearchChange(newValue: String){
        searchValue.value = newValue
    }
    fun getUserInfo(){
        viewModelScope.launch {
            isLoading.value=true
            val result =userRepository.getUserInfo()
            when(result){
                is Resource.Success -> {
                    if (result.data != null)
                        _user.value=result.data.user
                }
                else ->{

                }
            }
            isLoading.value=false
        }
    }

}