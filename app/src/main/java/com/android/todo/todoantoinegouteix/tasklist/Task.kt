package com.android.todo.todoantoinegouteix.tasklist

import java.io.Serializable


data class Task(val id: String, val title: String, val description: String = "Salut je suis une description") : Serializable {

}