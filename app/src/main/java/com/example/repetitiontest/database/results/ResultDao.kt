package com.example.repetitiontest.database.results

import androidx.room.*
import com.example.repetitiontest.database.users.UserEntity

@Dao
interface ResultDao {

    @Insert(entity = ResultEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addResult(resultEntity: ResultEntity)

    @Insert(entity = ResultEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addResults(resultList: List<ResultEntity>)

    @Update
    fun updateResult(resultEntity: ResultEntity)

    @Delete
    fun deleteResult(resultEntity: ResultEntity)

    @Query("select * from results_table")
    fun getDatabaseResults(): List<ResultEntity>

    @Query("select count(*) from results_table")
    fun countResults(): Int

    @Query("select * from results_table where id = :id")
    fun getResultById(id: Long): ResultEntity
}