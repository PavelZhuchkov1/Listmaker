package com.pavelzhuchkov1.listmaker.ui.detail.ui.detail

import androidx.lifecycle.ViewModel
import com.pavelzhuchkov1.listmaker.TaskList

class ListDetailViewModel : ViewModel() {
    lateinit var onTaskAdded: (() -> Unit)

    lateinit var list: TaskList

    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded.invoke()
    }
}