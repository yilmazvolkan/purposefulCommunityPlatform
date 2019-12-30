package com.bounswe.purposefulcommunity.Models

import com.google.gson.JsonObject


data class GetInstanceBody(
    val createdDate: String,
    val id: String,
    val creator: CreatorBody,
    val instanceFields: JsonObject
)