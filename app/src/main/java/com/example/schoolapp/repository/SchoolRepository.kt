package com.example.sekolahan.repository

import com.example.schoolapp.dao.SchoolDao
import com.example.schoolapp.model.School
import kotlinx.coroutines.flow.Flow

class SchoolRepository(private val schoolDao: SchoolDao) {
    val allSchool: Flow<List<School>> = schoolDao.getAllSchool()

    suspend fun insertSchool(school: School){
        schoolDao.insertSchool(school)
    }
    suspend fun deleteSchool(school: School){
        schoolDao.deleteSchool(school)
    }
    suspend fun updateSchool(school: School){
        schoolDao.updateSchool(school)
    }
}