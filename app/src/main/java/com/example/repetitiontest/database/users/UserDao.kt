package com.example.repetitiontest.database.users

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert(entity = UserEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addUser(userEntity: UserEntity)

    @Insert(entity = UserEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(userList: List<UserEntity>)

    @Update
    fun updateUser(userEntity: UserEntity)

    @Delete
    fun deleteUser(userEntity: UserEntity)

    @Query("select * from users_table")
    fun getDatabaseUsers(): List<UserEntity>

    @Query("select count(*) from users_table")
    fun countUsers(): Int

    @Query("select * from users_table where id = :id")
    fun getUserById(id: Long): UserEntity

}