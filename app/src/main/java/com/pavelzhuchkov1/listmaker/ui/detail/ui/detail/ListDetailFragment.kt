package com.pavelzhuchkov1.listmaker.ui.detail.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelzhuchkov1.listmaker.MainActivity
import com.pavelzhuchkov1.listmaker.R
import com.pavelzhuchkov1.listmaker.TaskList
import com.pavelzhuchkov1.listmaker.databinding.ListDetailFragmentBinding
import com.pavelzhuchkov1.listmaker.ui.MainViewModelFactory
import com.pavelzhuchkov1.listmaker.ui.main.MainViewModel

class ListDetailFragment : Fragment(), ListItemsRecyclerViewAdapter.DeteleTaskClickListener {

    lateinit var binding: ListDetailFragmentBinding
    lateinit var listDetailFragmentInteractionListener: ListDetailFragmentInteractionListener

    //надо еще почитать, зачем эта штуковина нужна.
    companion object {
        fun newInstance() = ListDetailFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = ListDetailFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    //onActivityCreated is deprecated. Все что тут вроде лучше делать в onViewCreated
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity())))
            .get(MainViewModel::class.java)

        val list: TaskList? = arguments?.getParcelable(MainActivity.INTENT_LIST_KEY)
        if (list != null) {
            viewModel.list = list
            requireActivity().title = list.name
        }

        val recyclerAdapter = ListItemsRecyclerViewAdapter(viewModel.list, this)
        binding.listItemsRecyclerView.adapter = recyclerAdapter
        binding.listItemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        if (::listDetailFragmentInteractionListener.isInitialized)
            Log.d("MyActivity","listDetailFragmentInteractionListener is initialized")
        else
            Log.d("MyActivity","listDetailFragmentInteractionListener isn't initialized")

        //Надо почитать, зачем нужен notifyDataSetChanged()
        //Inside the lambda, you notify the adapter that the list of tasks has updated. This
        //causes the RecyclerView to be redrawn, showing any new items.
        viewModel.onTaskAdded = { recyclerAdapter.notifyDataSetChanged() }
        viewModel.onTaskDeleted = { recyclerAdapter.taskDeleted() }
        viewModel.onTasksDeleted = { recyclerAdapter.notifyDataSetChanged()}
    }

    override fun deleteTaskCheckBoxClicked(task: String) {
        listDetailFragmentInteractionListener.deleteTaskCheckBoxTapped(task)
    }

    interface ListDetailFragmentInteractionListener {
        fun deleteTaskCheckBoxTapped(task: String)
    }

}