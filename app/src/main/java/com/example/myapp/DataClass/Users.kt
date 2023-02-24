package com.example.myapp.DataClass

data class Users(
    val Name:String ?=null,
    val Email:String?=null,
    val Password:String?=null,
    val Number:String?=null,
    val Status: String?,
    val imageurl: String?,
    val CoverPhoto:String?
)
