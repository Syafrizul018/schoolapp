package com.example.schoolapp.application

import android.app.Application
import com.example.sekolahan.application.SchoolDatabase
import com.example.sekolahan.repository.SchoolRepository

class SchoolApp: Application() {
    val database by lazy {SchoolDatabase.getDatabase(this) }
    val repository by lazy { SchoolRepository(database.schoolDao()) }
}