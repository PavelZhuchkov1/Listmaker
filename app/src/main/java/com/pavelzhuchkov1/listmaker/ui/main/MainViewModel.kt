package com.pavelzhuchkov1.listmaker.ui.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.pavelzhuchkov1.listmaker.TaskList

class MainViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    lateinit var list: TaskList

    lateinit var onTaskAdded: (() -> Unit)

    //добавили функцию, кооторая будет показывать, добавлен ли лист. Инициализирован будет после.
    lateinit var onListAdded: (() -> Unit)
    lateinit var onListDeleted: (() -> Unit)
    lateinit var onListsDeleted: (() -> Unit)
    lateinit var onTaskDeleted: (() -> Unit)
    lateinit var onTasksDeleted: (() -> Unit)

    //by lazy аналогичен lateinit, только для val.
    // lists будет вызван тогда, когда он понадобится. Если же мы не будем писать by lazy, то будет
    // создан объект lists.
    // т.е. можно было сделать так
    // private val lists: MutableList<TaskList> = retrieveLists()
    // но тогда бы создался объект
    val lists: MutableList<TaskList> by lazy {
        retrieveLists()
    }

    private fun retrieveLists(): MutableList<TaskList> {

        val sharedPreferencesContents = sharedPreferences.all
        val taskLists = ArrayList<TaskList>()

        for (taskList in sharedPreferencesContents) {
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>) //?????
            val list = TaskList(taskList.key, itemsHashSet)
            taskLists.add(list)
        }
        Log.d("MyActivity", "$taskLists")
        return taskLists
    }

    fun saveList(list: TaskList) {
        sharedPreferences.edit().putStringSet(list.name, list.tasks.toHashSet()).apply()
        lists.add(list)
        onListAdded.invoke()
    }

    fun updateList(list: TaskList) {
        sharedPreferences.edit().putStringSet(list.name, list.tasks.toHashSet()).apply()
        lists.add(list)
    }

    fun refreshLists() {
        lists.clear()
        lists.addAll(retrieveLists())
    }

    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded.invoke()
    }

    fun deleteList(list: TaskList) {
        lists.remove(list)
        sharedPreferences.edit().remove(list.name).apply()
        onListDeleted.invoke()
    }

    fun deleteAllLists() {
        lists.clear()
        sharedPreferences.edit().clear().apply()
        onListsDeleted.invoke()
    }

    fun deleteTask(task: String) {
        list.tasks.remove(task)
        sharedPreferences.edit().remove(task).apply()
        onTaskDeleted.invoke()
    }

    fun deleteAllTasks() {
        list.tasks.clear()
        sharedPreferences.edit().clear().apply()
        onTasksDeleted.invoke()
    }
}