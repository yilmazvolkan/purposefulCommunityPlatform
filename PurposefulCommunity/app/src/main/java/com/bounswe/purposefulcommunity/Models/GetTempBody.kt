package com.bounswe.purposefulcommunity.Models


data class GetTempBody(
    val id: String,
    val createdDate: String,
    val lastModifiedDate: String,
    val name: String,
    val fieldResources: List<ShowOneFieldBody>
)