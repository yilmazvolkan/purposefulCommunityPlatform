package com.bounswe.purposefulcommunity.Models

data class AddTempSelectedBody(
    val fieldType: String,
    val isRequired: Boolean,
    val name: String,
    val parent: String
)