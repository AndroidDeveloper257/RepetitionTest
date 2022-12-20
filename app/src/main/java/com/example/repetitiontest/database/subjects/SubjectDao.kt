package com.example.repetitiontest.database.subjects

import androidx.room.*

@Dao
interface SubjectDao {

    @Insert(entity = SubjectEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSubject(subjectEntity: SubjectEntity)

    @Insert(entity = SubjectEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSubjects(subjectList: List<SubjectEntity>)

    @Update
    fun updateSubject(subjectEntity: SubjectEntity)

    @Delete
    fun deleteSubject(subjectEntity: SubjectEntity)

    @Query("select * from subjects_table")
    fun getDatabaseSubjects(): List<SubjectEntity>

    @Query("select count(*) from subjects_table")
    fun countSubjects(): Int

    @Query("select * from subjects_table where id = :id")
    fun getSubjectById(id: Long): SubjectEntity

}