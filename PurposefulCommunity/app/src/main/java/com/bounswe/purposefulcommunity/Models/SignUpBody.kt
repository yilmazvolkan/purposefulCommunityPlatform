package com.bounswe.purposefulcommunity.Models


data class SignUpBody(
    val email: String,
    val name: String,
    val password: String,
    val surname: String)