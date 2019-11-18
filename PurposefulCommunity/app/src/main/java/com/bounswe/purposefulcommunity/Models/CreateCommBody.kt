package com.bounswe.purposefulcommunity.Models

data class CreateCommBody(
    val description: String,
    val isPrivate: Boolean,
    val name: String,
    val size: Int
)
