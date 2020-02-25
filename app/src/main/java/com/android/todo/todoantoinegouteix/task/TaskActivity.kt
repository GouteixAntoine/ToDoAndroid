package com.android.todo.todoantoinegouteix.task

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.todo.todoantoinegouteix.R
import com.android.todo.todoantoinegouteix.tasklist.Task
import kotlinx.android.synthetic.main.activity_task.*
import java.io.Serializable
import java.util.*

class TaskActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)


        save_button.setOnClickListener{
            val task = Task(id = UUID.randomUUID().toString(), title = edit_title.text.toString(), description = edit_description.text.toString())
            intent.putExtra(EDIT_KEY,task as Serializable)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

    }

    companion object {
        const val EDIT_KEY = "key_edit"
        const val ADD_KEY = "key_add"
    }

}