package com.example.workoutapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Facilitates interaction between repository and UI
class UserViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<Log>>
    private val repository: LogRepository

    init {
        val logDao = LogDatabase.getDatabase(application).logDao()
        repository = LogRepository(logDao)
        readAllData = repository.readAllData
    }

    fun addLog(log: Log) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLog(log)
        }
    }
}