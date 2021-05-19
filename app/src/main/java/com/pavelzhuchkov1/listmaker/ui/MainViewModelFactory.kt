package com.pavelzhuchkov1.listmaker.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pavelzhuchkov1.listmaker.ui.main.MainViewModel

class MainViewModelFactory(private val sharedPreferences: SharedPreferences): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(sharedPreferences) as T
    }
}