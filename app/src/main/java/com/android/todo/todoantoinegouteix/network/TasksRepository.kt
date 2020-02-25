package com.android.todo.todoantoinegouteix.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.todo.todoantoinegouteix.tasklist.Task

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée: [_taskList] est modifiable et privée. On va l'utiliser seulement dans le contexte de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable. On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    // Fonction pour le rafraichir
    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks
        }
    }

    // Fonciton pour la création
    suspend fun createTask(task: Task){
        val createdTaskResponse = tasksWebService.createTask(task)
        val createdTask = createdTaskResponse.body()
        val editableList = _taskList.value.orEmpty().toMutableList()
        if (createdTask != null) editableList.add(createdTask)
        _taskList.value = editableList
    }

    // Fonciton pour la modification
    suspend fun updateTask(task: Task) {
        tasksWebService.updateTask(task)
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { task.id == it.id }
        editableList[position] = task
        _taskList.value = editableList
    }

    // Fonction pour la suppression
    suspend fun deleteTask(task: Task){
        tasksWebService.deleteTask(task.id)
        val editableList = _taskList.value.orEmpty().toMutableList()
        if (editableList.contains(task)) editableList.remove(task)
        _taskList.value = editableList
    }

}