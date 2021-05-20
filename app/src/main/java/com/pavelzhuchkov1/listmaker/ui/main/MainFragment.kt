package com.pavelzhuchkov1.listmaker.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelzhuchkov1.listmaker.TaskList
import com.pavelzhuchkov1.listmaker.databinding.MainFragmentBinding
import com.pavelzhuchkov1.listmaker.ui.MainViewModelFactory

class MainFragment() : Fragment(),
ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener,
ListSelectionRecyclerViewAdapter.DeleteListClickListener{

    private lateinit var binding: MainFragmentBinding
    lateinit var mainFragmentInteractionListener: MainFragmentInteractionListener

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = MainFragmentBinding.inflate(inflater, container, false)

        binding.listsRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        return binding.root


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (::mainFragmentInteractionListener.isInitialized)
            Log.d("MyActivity","mainFragmentInteractionListener is initialized")
        else
            Log.d("MyActivity","mainFragmentInteractionListener isn't initialized")

        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity()))).get(MainViewModel::class.java)

        val recyclerViewAdapter = ListSelectionRecyclerViewAdapter(viewModel.lists, this, this)

        binding.listsRecyclerview.adapter = recyclerViewAdapter

        viewModel.onListAdded = {recyclerViewAdapter.listsUpdated()}

        viewModel.onListsDeleted = {recyclerViewAdapter.listsDeleted()}

        viewModel.onListDeleted = {recyclerViewAdapter.listDeleted()}
    }

    override fun listItemClicked(list: TaskList) {
        mainFragmentInteractionListener.listItemTapped(list)
    }

    override fun deleteListCheckBoxClicked(list: TaskList) {
        mainFragmentInteractionListener.deleteListCheckBoxTapped(list)
    }

    interface MainFragmentInteractionListener {
        fun listItemTapped(list: TaskList)
        fun deleteListCheckBoxTapped(list: TaskList)
    }

}