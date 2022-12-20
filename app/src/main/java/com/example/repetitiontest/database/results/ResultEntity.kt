package com.example.repetitiontest.database.results

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results_table")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "book_id")
    var bookId: Long? = null,
    @ColumnInfo(name = "user_id")
    var userId: Long? = null,
    @ColumnInfo(name = "point")
    var point: Double? = null,
    @ColumnInfo(name = "primary_subject_id")
    var primarySubjectId: Long? = null,
    @ColumnInfo(name = "secondary_subject_id")
    var secondarySubjectId: Long? = null,
    @ColumnInfo(name = "hour")
    var hour: Int? = null,
    @ColumnInfo(name = "minute")
    var minute: Int? = null,
    @ColumnInfo(name = "date")
    var date: Int? = null,
    @ColumnInfo(name = "month")
    var month: Int? = null,
    @ColumnInfo(name = "year")
    var year: Int? = null
)