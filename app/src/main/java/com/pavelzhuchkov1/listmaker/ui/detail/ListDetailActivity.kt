package com.pavelzhuchkov1.listmaker.ui.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.pavelzhuchkov1.listmaker.MainActivity
import com.pavelzhuchkov1.listmaker.R
import com.pavelzhuchkov1.listmaker.TaskList
import com.pavelzhuchkov1.listmaker.databinding.ListDetailActivityBinding
import com.pavelzhuchkov1.listmaker.ui.MainViewModelFactory
import com.pavelzhuchkov1.listmaker.ui.detail.ui.detail.ListDetailFragment
import com.pavelzhuchkov1.listmaker.ui.detail.ui.detail.ListDetailViewModel
import com.pavelzhuchkov1.listmaker.ui.main.MainViewModel

class ListDetailActivity : AppCompatActivity() {

    lateinit var binding: ListDetailActivityBinding
    lateinit var viewModel: MainViewModel
    lateinit var fragment: ListDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListDetailActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }

        viewModel = ViewModelProvider(this,
        MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this)))
            .get(MainViewModel::class.java)

        viewModel.list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!
        title = viewModel.list.name
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ListDetailFragment.newInstance())
                    .commitNow()
        }
    }

    private fun showCreateTaskDialog() {

        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this).apply {
            setTitle(R.string.task_to_add)
            setView(taskEditText)
            setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                viewModel.addTask(task)
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, viewModel.list)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}