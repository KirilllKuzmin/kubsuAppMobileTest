package com.kubsu.kubsuappmobile.data.model

data class User(

    val token: String,

    val userId: Long,

    val username: String,

    val roles: List<String>
)
