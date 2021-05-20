package com.pavelzhuchkov1.listmaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.pavelzhuchkov1.listmaker.databinding.MainActivityBinding
import com.pavelzhuchkov1.listmaker.ui.MainViewModelFactory
import com.pavelzhuchkov1.listmaker.ui.main.MainFragment
import com.pavelzhuchkov1.listmaker.ui.main.MainViewModel
import androidx.preference.PreferenceManager
import com.pavelzhuchkov1.listmaker.ui.detail.ListDetailActivity
import com.pavelzhuchkov1.listmaker.ui.detail.ui.detail.ListDetailFragment

class MainActivity : AppCompatActivity(),
MainFragment.MainFragmentInteractionListener{

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
        ).get(MainViewModel::class.java)

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            mainFragment.mainFragmentInteractionListener = this

            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.detail_container
            } else {
                R.id.main_fragment_container
            }

            //Попробовать использовать beginTransaction(). Почему-то не сработало:(
            supportFragmentManager.commit {
                // о setReorderingAllowed нужно будет почитать
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }

        }

        binding.addListButton.setOnClickListener{
            showCreateListDialog()
        }

        binding.deleteAllListsButton.setOnClickListener {
            deleteAllLists()
        }
    }

    private fun showCreateListDialog() {

        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
//        The inputType of the EditText is set to TYPE_CLASS_TEXT. Specifying the input
//        type gives Android a hint as to what the most appropriate keyboard to show is. In
//        this case, a text-based keyboard, since you want the list to have a name.
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

//        You pass in positiveButtonTitle as the label for the button and implement an
//        onClickListener. For now, you dismiss the Dialog. You’ll handle the resulting
//        actions behind the button in the next section.
        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()

            val taskList = TaskList(listTitleEditText.text.toString())
            viewModel.saveList(taskList)
            //Мне не нравится, что сразу после того, как создаешь list, открывается list_detail,
            //поэтому я за коментил строку ниже.
            //showListDetail(taskList)

        } //не очень понятно что за _

        builder.create().show()
    }

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                viewModel.addTask(task)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showListDetail(list: TaskList) {

        if (binding.mainFragmentContainer == null) {
            val listDetailIntent = Intent(this,
                ListDetailActivity::class.java)
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)
            startActivityForResult(listDetailIntent, LIST_DETAIL_REQUEST_CODE)
        } else {
            //bundle работает как intent, только для фрагментов (как я понял)
            val bundle = bundleOf(INTENT_LIST_KEY to list)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.list_detail_fragment_container, ListDetailFragment::class.java, bundle, null)
            }

            binding.addListButton.setOnClickListener {
                showCreateTaskDialog()
            }
        }
    }

    private fun deleteList(list: TaskList) {
        viewModel.deleteList(list)
    }

    private fun deleteAllLists() {
        viewModel.deleteAllLists()
    }

    override fun listItemTapped(list: TaskList) {
        showListDetail(list)
    }

    override fun deleteListCheckBoxTapped(list: TaskList) {
        deleteList(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LIST_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //надо почитать как работает let. Надо будет сделать тоже самое без let.
            data?.let {
                viewModel.updateList(data.getParcelableExtra(INTENT_LIST_KEY)!!)
                viewModel.refreshLists()
            }
        }
    }

    override fun onBackPressed() {

        val listDetailFragment = supportFragmentManager.findFragmentById(R.id.list_detail_fragment_container)

        if (listDetailFragment == null) {
            super.onBackPressed()
        } else {
            title = resources.getString(R.string.app_name)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                remove(listDetailFragment)
            }
        }

        binding.addListButton.setOnClickListener {
            showCreateListDialog()
        }
    }

    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }
}