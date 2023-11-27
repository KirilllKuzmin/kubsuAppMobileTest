package com.kubsu.kubsuappmobile.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    val token = MutableLiveData<String>()
}