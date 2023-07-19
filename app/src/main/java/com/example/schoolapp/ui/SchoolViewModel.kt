package com.example.schoolapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.schoolapp.model.School
import com.example.sekolahan.repository.SchoolRepository
import kotlinx.coroutines.launch

class SchoolViewModel (private val repository: SchoolRepository): ViewModel() {
    val allSchool: LiveData<List<School>> = repository.allSchool.asLiveData()

    fun insert(scholl: School) = viewModelScope.launch {
        repository.insertSchool(scholl)
    }

    fun delete(scholl: School) = viewModelScope.launch {
        repository.deleteSchool(scholl)
    }

    fun update(scholl: School) = viewModelScope.launch {
        repository.updateSchool(scholl)
    }
}

    class SchoolViewModelFactory(private val repository: SchoolRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom((SchoolViewModel::class.java))){
                return SchoolViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}