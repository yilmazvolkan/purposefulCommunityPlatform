package com.bounswe.purposefulcommunity.Models

data class CreateTemplateBody(
    val communityId: String,
    val fields: List<AddTempBody>,
    val name: String,
    val userId: String
)
