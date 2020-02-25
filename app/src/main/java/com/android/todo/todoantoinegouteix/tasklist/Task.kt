package com.android.todo.todoantoinegouteix.tasklist

import com.squareup.moshi.Json
import java.io.Serializable

data class Task(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String)
    : Serializable