package com.pavelzhuchkov1.listmaker.ui.detail.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelzhuchkov1.listmaker.TaskList
import com.pavelzhuchkov1.listmaker.databinding.ListItemViewHolderBinding
import com.pavelzhuchkov1.listmaker.ui.main.ListSelectionRecyclerViewAdapter

class ListItemsRecyclerViewAdapter(
    var list: TaskList,
    val deleteTaskClickListener: DeteleTaskClickListener) :
    RecyclerView.Adapter<ListItemViewHolder>() {

    private var positionOfDeletedTask: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.binding.textViewTask.text = list.tasks[position]
        holder.binding.deleteTaskCheckBox.isChecked = false
        holder.binding.deleteTaskCheckBox.setOnClickListener {
            positionOfDeletedTask = position
            deleteTaskClickListener.deleteTaskCheckBoxClicked(list.tasks[position])
        }
    }

    override fun getItemCount(): Int {
        return list.tasks.size
    }

    fun taskDeleted() {
        notifyItemRemoved(positionOfDeletedTask)
        notifyItemRangeChanged(positionOfDeletedTask, list.tasks.size)
    }

    interface DeteleTaskClickListener {
        fun deleteTaskCheckBoxClicked(task : String)
    }

}