package com.bounswe.purposefulcommunity.Models


data class CommunityBody(
    val id: String,
    val createdDate: String,
    val lastModifiedDate: String,
    val name: String,
    val description: String,
    val size: Int,
    val isPrivate: Boolean,
    val followers: List<CreatorBody>,
    val builders: List<CreatorBody>,
    val creatorUser: CreatorBody
    )