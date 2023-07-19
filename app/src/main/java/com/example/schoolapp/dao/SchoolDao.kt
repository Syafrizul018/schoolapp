package com.example.schoolapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.schoolapp.model.School
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
    @Query("SELECT * FROM  `school_table` ORDER BY name ASC")
    fun getAllSchool(): Flow<List<School>>

    @Insert
    suspend fun insertSchool(school: School)

    @Delete
    suspend fun deleteSchool(school: School)

    @Update fun updateSchool(school: School)

}
