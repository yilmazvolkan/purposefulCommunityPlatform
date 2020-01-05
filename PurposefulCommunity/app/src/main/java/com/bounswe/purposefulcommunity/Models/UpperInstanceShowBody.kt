package com.bounswe.purposefulcommunity.Models


data class UpperInstanceShowBody(
    val name: String,
    val createdDate: String,
    val fieldList: ArrayList<ShowInstanceBody>,
    val tempFieldList: ArrayList<ShowInstanceBody>
)