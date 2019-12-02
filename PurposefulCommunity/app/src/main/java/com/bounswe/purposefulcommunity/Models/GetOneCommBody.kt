package com.bounswe.purposefulcommunity.Models


data class GetOneCommBody(
    val builders: List<ShowUserBody>,
    val createdDate: String,
    val creatorUser: List<ShowUserBody>,
    val description: String,
    val followers: List<ShowUserBody>,
    val id: String,
    val isPrivate: Boolean,
    val lastModifiedDate: String,
    val name: String,
    val size: Int
)