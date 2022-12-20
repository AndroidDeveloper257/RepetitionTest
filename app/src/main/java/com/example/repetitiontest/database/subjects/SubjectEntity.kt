package com.example.repetitiontest.database.subjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects_table")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "subject_name")
    var subjectName: String? = null
)
