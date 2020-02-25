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

    companion object {
        const val ADD_KEY = "key_add"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)


        val task = intent.getSerializableExtra(ADD_KEY) as? Task
        edit_title.setText(task?.title)
        edit_description.setText(task?.description)



        save_button.setOnClickListener{
            val newTask = Task(
                id = task?.id ?: UUID.randomUUID().toString(),
                title = edit_title.text.toString(),
                description = edit_description.text.toString()
            )
            intent.putExtra(ADD_KEY,newTask as Serializable)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }



    }

}