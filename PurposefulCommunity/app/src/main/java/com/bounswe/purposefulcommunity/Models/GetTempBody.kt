package com.bounswe.purposefulcommunity.Models

import com.google.gson.JsonObject


data class GetTempBody(
    val id: String,
    val createdDate: String,
    val lastModifiedDate: String,
    val name: String,
    val fieldResources: List<GetFieldsBody>,
    val templatesNameId: JsonObject,
    val instanceContext: JsonObject
)