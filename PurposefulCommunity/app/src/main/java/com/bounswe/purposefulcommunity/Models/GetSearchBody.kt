package com.bounswe.purposefulcommunity.Models

import com.google.gson.JsonObject

data class GetInstanceLDBody(
    val createdDate: String,
    val id: String,
    val creator: CreatorBody,
    val instanceFields: JsonObject,
    val template: GetTempBody,
    val name: String
)