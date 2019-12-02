package com.bounswe.purposefulcommunity.Models


data class GetOneCommBody(
    val id: String,
    val createdDate: String,
    val lastModifiedDate: String,
    val name: String,
    val description: String,
    val size: Int,
    val isPrivate: Boolean,
    val creatorUser: ShowUserBody,
    val builders: List<ShowUserBody>,
    val followers: List<ShowUserBody>
)