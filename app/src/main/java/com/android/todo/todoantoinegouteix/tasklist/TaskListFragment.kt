package com.android.todo.todoantoinegouteix.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.todo.todoantoinegouteix.R
import com.android.todo.todoantoinegouteix.task.TaskActivity
import kotlinx.android.synthetic.main.fragment_task_list.*
import java.util.*

class TaskListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val taskListAdapter = TaskListAdapter(taskList)
        recycle_view
        recycle_view.layoutManager = LinearLayoutManager(activity)
        recycle_view.adapter = taskListAdapter

        // Ajout d'une liste
        add_button.setOnClickListener{
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.ADD_KEY, "C'est bon !")
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        // Suppression d'une liste
        taskListAdapter.onDeleteClickListener = { task ->
            taskList.remove(task)
            (recycle_view.adapter as TaskListAdapter).notifyDataSetChanged()
        }

        // Bouton ajout un élément de la liste
        taskListAdapter.onAddClickListener = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.EDIT_KEY, "C'est bon !")
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val task = data!!.getSerializableExtra(TaskActivity.EDIT_KEY) as Task
        if (requestCode == ADD_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.taskList.add(task)
            }
        }

    }

    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    companion object {
        const val ADD_TASK_REQUEST_CODE = 256
        const val EDIT_TASK_REQUEST_CODE = 356
    }

}