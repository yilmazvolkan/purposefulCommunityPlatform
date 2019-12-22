package com.bounswe.purposefulcommunity.Models


data class GetFieldsBody(
    val fieldType: String,
    val id: String,
    val isRequired: Boolean,
    val name: String
)