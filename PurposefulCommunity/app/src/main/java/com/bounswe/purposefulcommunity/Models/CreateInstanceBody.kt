package com.bounswe.purposefulcommunity.Models

import com.google.gson.JsonObject

data class CreateInstanceBody(
    val instanceFields: JsonObject,
    val templateId: String,
    val name: String
)
