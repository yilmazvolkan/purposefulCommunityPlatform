package com.bounswe.purposefulcommunity.Models

import com.google.gson.JsonObject

data class CreateTemplateBody(
    val communityId: String,
    val fields: List<AddTempBody>,
    val name: String,
    val userId: String,
    val templatesNameTemplateId: JsonObject
)
