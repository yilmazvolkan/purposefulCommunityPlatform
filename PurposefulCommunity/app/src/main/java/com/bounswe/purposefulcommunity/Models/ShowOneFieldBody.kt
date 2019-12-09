package com.bounswe.purposefulcommunity.Models


data class ShowOneFieldBody(
    val id: String,
    val createdDate: String,
    val lastModifiedDate: String,
    val fieldType: String,
    val name: String
)