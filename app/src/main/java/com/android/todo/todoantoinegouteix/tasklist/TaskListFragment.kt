package com.android.todo.todoantoinegouteix.tasklist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.todo.todoantoinegouteix.R
import com.android.todo.todoantoinegouteix.network.Api
import com.android.todo.todoantoinegouteix.task.TaskActivity
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {

    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    // VARIABLES
    val taskListAdapter = TaskListAdapter(taskList)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycle_view
        recycle_view.layoutManager = LinearLayoutManager(activity)
        recycle_view.adapter = taskListAdapter

        // Ajout d'une liste
        add_button.setOnClickListener{
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.ADD_KEY, "C'est ajouté !")
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        // Suppression d'une liste
        taskListAdapter.onDeleteClickListener = { task ->
            taskList.remove(task)
            (recycle_view.adapter as TaskListAdapter).notifyDataSetChanged()
        }

        // Bouton d'édition de la liste
        taskListAdapter.onAddClickListener = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.ADD_KEY, task)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val task = data!!.getSerializableExtra(TaskActivity.ADD_KEY) as Task
        if (requestCode == ADD_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                taskList.add(task)
            }
        }
        if (requestCode == EDIT_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
               val idx = taskList.indexOfFirst {
                   it.id == task.id
               }
                taskList[idx] = task
            }
        }
        taskListAdapter.notifyDataSetChanged()

    }

    // VARIABLES
    private val coroutineScope = MainScope()

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        coroutineScope.launch {
            val userInfo = Api.userService.getInfo().body()
            info_user.text = "${userInfo?.firstName} ${userInfo?.lastName}"
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }


    companion object {
        const val ADD_TASK_REQUEST_CODE = 256
        const val EDIT_TASK_REQUEST_CODE = 356
    }

}