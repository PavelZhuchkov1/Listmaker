package com.pavelzhuchkov1.listmaker.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelzhuchkov1.listmaker.TaskList
import com.pavelzhuchkov1.listmaker.databinding.ListSelectionViewHolderBinding

class ListSelectionRecyclerViewAdapter(
        private val lists: MutableList<TaskList>,
        val deleteListClickListener: DeleteListClickListener,
        val showTasksClickListener: ListSelectionRecyclerViewClickListener) :
        RecyclerView.Adapter<ListSelectionViewHolder>() {

    private var positionOfDeletedList: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {

        val binding = ListSelectionViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListSelectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        holder.binding.itemNumber.text = (position + 1).toString()
        holder.binding.itemString.text = lists[position].name
        holder.binding.deleteListCheckBox.isChecked = false
        holder.itemView.setOnClickListener {
            showTasksClickListener.listItemClicked(lists[position])
        }

        holder.binding.deleteListCheckBox.setOnClickListener {
            positionOfDeletedList = position
            deleteListClickListener.deleteListCheckBoxClicked(lists[position])
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    fun listsUpdated() {
        notifyItemInserted(lists.size-1)
    }

    fun listsDeleted() {
        notifyDataSetChanged()
    }

    fun listDeleted() {
        notifyItemRemoved(positionOfDeletedList)
        notifyItemRangeChanged(positionOfDeletedList,lists.size)
    }

    interface ListSelectionRecyclerViewClickListener {
        fun listItemClicked(list: TaskList)
    }

    interface DeleteListClickListener {
        fun deleteListCheckBoxClicked(list: TaskList)
    }
}