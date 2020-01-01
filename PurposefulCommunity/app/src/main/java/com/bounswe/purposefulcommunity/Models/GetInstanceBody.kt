package com.bounswe.purposefulcommunity.Models


data class GetInstanceBody(
    val createdDate: String,
    val id: String,
    val creator: CreatorBody,
    val template: GetTempBody,
    val fieldNameValueTypes: StructureBody
)