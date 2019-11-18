package com.bounswe.purposefulcommunity.Models


data class SignInRes(
    val email: String,
    val id: String,
    val name: String,
    val surname: String,
    val token: String)