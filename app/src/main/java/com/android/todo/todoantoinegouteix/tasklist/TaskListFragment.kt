package com.android.todo.todoantoinegouteix.tasklist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.todo.todoantoinegouteix.R
import com.android.todo.todoantoinegouteix.network.Api
import com.android.todo.todoantoinegouteix.network.TasksRepository
import com.android.todo.todoantoinegouteix.task.TaskActivity
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {

    private val taskList = mutableListOf<Task>()

    // VARIABLES
    val taskListAdapter = TaskListAdapter(taskList)
    val tasksRepository = TasksRepository()


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

        // Edition de la liste
        taskListAdapter.onAddClickListener = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.ADD_KEY, task)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }

        // Abonnement du fragment à la live data
        tasksRepository.taskList.observe(this, Observer {
            taskList.clear()
            taskList.addAll(it)
            taskListAdapter.notifyDataSetChanged()
        })

        taskListAdapter.onDeleteClickListener = {
            lifecycleScope.launch {
                tasksRepository.deleteTask(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val task = data!!.getSerializableExtra(TaskActivity.ADD_KEY) as Task
        if (requestCode == ADD_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && task != null) {
                coroutineScope.launch {
                    tasksRepository.createTask(task)
                }
            }
        }
        if (requestCode == EDIT_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && task != null) {
                coroutineScope.launch {
                    tasksRepository.updateTask(task)
                }
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
        lifecycleScope.launch {
            tasksRepository.refresh()
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